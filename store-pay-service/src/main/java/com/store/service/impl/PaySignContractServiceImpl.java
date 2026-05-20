package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.auth.SecurityUtils;
import com.store.common.exception.BusinessException;
import com.store.config.WechatSubscriptionProperties;
import com.store.constans.SimplePayConstants;
import com.store.domain.MemberLevel;
import com.store.domain.PaySignContract;
import com.store.domain.UserMemberPurchase;
import com.store.domain.dto.WechatContractOrderDTO;
import com.store.domain.vo.WechatContractOrderVO;
import com.store.enums.AutoRenewStatusEnum;
import com.store.enums.ContractStatusEnum;
import com.store.enums.PayChannelEnum;
import com.store.mapper.PaySignContractMapper;
import com.store.service.MemberLevelService;
import com.store.service.PaySignContractService;
import com.store.service.UserMemberPurchaseService;
import com.store.wechat.WechatPayV2PartnerClient;
import com.store.wechat.dto.WechatContractOrderResponse;
import com.store.wechat.util.WechatPayV2XmlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 自动续费签约协议业务实现。
 *
 * <p>当前新增的是微信 V2 支付中签约：创建首期支付订单时，同时携带签约参数，
 * 用户在微信侧完成支付后再完成自动续费签约。</p>
 */
@Service
public class PaySignContractServiceImpl extends ServiceImpl<PaySignContractMapper, PaySignContract>
        implements PaySignContractService {

    private final MemberLevelService memberLevelService;
    private final UserMemberPurchaseService userMemberPurchaseService;
    private final WechatPayV2PartnerClient wechatPayV2PartnerClient;
    private final WechatSubscriptionProperties properties;

    public PaySignContractServiceImpl(MemberLevelService memberLevelService,
                                      UserMemberPurchaseService userMemberPurchaseService,
                                      WechatPayV2PartnerClient wechatPayV2PartnerClient,
                                      WechatSubscriptionProperties properties) {
        this.memberLevelService = memberLevelService;
        this.userMemberPurchaseService = userMemberPurchaseService;
        this.wechatPayV2PartnerClient = wechatPayV2PartnerClient;
        this.properties = properties;
    }

    /**
     * 创建微信支付中签约订单。
     *
     * <p>这里做三件事：
     * 1. 校验会员套餐和金额；
     * 2. 生成本地购买单号、签约协议号，并调用微信 contractorder；
     * 3. 保存本地待支付购买记录和待签约协议记录，返回 APP 调起微信支付参数。</p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WechatContractOrderVO contractOrder(WechatContractOrderDTO dto) {
        Long userId = SecurityUtils.getMemberId();

        MemberLevel memberLevel = getValidWechatMemberLevel(dto.getLevelId());
        checkDuplicateActiveContract(userId, memberLevel.getId());
        String purchaseNo = buildNo("MP");
        String contractNo = buildNo("WC");
        String requestSerial = buildNo("RS");
        String body = StringUtils.hasText(dto.getBody()) ? dto.getBody() : "会员自动续费-" + memberLevel.getLevelName();
        String attach = StringUtils.hasText(dto.getAttach()) ? dto.getAttach() : "memberLevelId=" + memberLevel.getId();
        String clientIp = resolveClientIp(dto.getClientIp());

        WechatContractOrderResponse response = wechatPayV2PartnerClient.contractOrder(
                memberLevel.getWechatPlanId(),
                contractNo,
                requestSerial,
                dto.getContractDisplayAccount(),
                purchaseNo,
                body,
                dto.getDetail(),
                attach,
                toFen(memberLevel.getCurrentPrice()),
                clientIp
        );

        savePendingPurchase(userId, purchaseNo, memberLevel, dto);
        savePendingContract(userId, contractNo, requestSerial, memberLevel, response, dto);

        return buildAppPayVO(purchaseNo, contractNo, memberLevel, response);
    }

    private MemberLevel getValidWechatMemberLevel(Integer levelId) {
        MemberLevel memberLevel = memberLevelService.getById(levelId);
        if (memberLevel == null || memberLevel.getStatus() == null || memberLevel.getStatus() != 1) {
            throw new BusinessException("会员套餐不存在或已下架");
        }
        if (!StringUtils.hasText(memberLevel.getWechatPlanId())) {
            throw new BusinessException("会员套餐未配置微信自动续费模板");
        }
        if (memberLevel.getCurrentPrice() == null
                || memberLevel.getCurrentPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("会员套餐金额异常");
        }
        return memberLevel;
    }

    private void checkDuplicateActiveContract(Long userId, Integer levelId) {
        long activeContractCount = this.lambdaQuery()
                .eq(PaySignContract::getUserId, userId)
                .eq(PaySignContract::getLevelId, levelId)
                .in(PaySignContract::getContractStatus,
                        ContractStatusEnum.WAIT_SIGN.getCode(),
                        ContractStatusEnum.SIGNED.getCode())
                .count();
        if (activeContractCount > 0) {
            throw new BusinessException("当前会员套餐已有待签约或已生效的自动续费协议");
        }
    }

    private String resolveClientIp(String fallbackIp) {
        HttpServletRequest request = SecurityUtils.getRequest();
        String ip = firstIp(request.getHeader("X-Forwarded-For"));
        if (!StringUtils.hasText(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!StringUtils.hasText(ip)) {
            ip = fallbackIp;
        }
        if (!StringUtils.hasText(ip)) {
            throw new BusinessException("无法获取客户端IP");
        }
        return ip;
    }

    private String firstIp(String forwardedFor) {
        if (!StringUtils.hasText(forwardedFor) || "unknown".equalsIgnoreCase(forwardedFor)) {
            return null;
        }
        return forwardedFor.split(",")[0].trim();
    }

    private void savePendingPurchase(Long userId,
                                     String purchaseNo,
                                     MemberLevel memberLevel,
                                     WechatContractOrderDTO dto) {
        UserMemberPurchase purchase = new UserMemberPurchase();
        purchase.setUserId(userId.intValue());
        purchase.setPurchaseNo(purchaseNo);
        purchase.setLevelId(memberLevel.getId());
        purchase.setRechargeMonths(memberLevel.getRechargeMonths());
        purchase.setOriginalPrice(memberLevel.getOriginalPrice());
        purchase.setActualPayAmount(memberLevel.getCurrentPrice());
        purchase.setGiveIntegral(memberLevel.getGiveIntegral());
        purchase.setIntegralValidityDays(memberLevel.getIntegralValidityDays());
        purchase.setPayChannel("wechatAndroid");
        purchase.setPayType("weixin");
        purchase.setPayStatus(SimplePayConstants.PAY_STATUS_PENDING);
        purchase.setMemberStatus(SimplePayConstants.MEMBER_STATUS_UNUSED);
        purchase.setOutTradeNo(purchaseNo);
        purchase.setInvoiceStatus(0);
        purchase.setRemark(dto.getRemark());
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        userMemberPurchaseService.save(purchase);
    }

    private void savePendingContract(Long userId,
                                     String contractNo,
                                     String requestSerial,
                                     MemberLevel memberLevel,
                                     WechatContractOrderResponse response,
                                     WechatContractOrderDTO dto) {
        PaySignContract contract = new PaySignContract();
        contract.setUserId(userId);
        contract.setLevelId(memberLevel.getId());
        contract.setPayChannel(PayChannelEnum.WECHAT.getCode());
        contract.setContractNo(contractNo);
        contract.setThirdPartyContractNo(response.getContractId());
        contract.setContractStatus(ContractStatusEnum.WAIT_SIGN.getCode());
        contract.setAutoRenewStatus(AutoRenewStatusEnum.CLOSE.getCode());
        contract.setClientPlatform(SimplePayConstants.CLIENT_ANDROID);
        contract.setPeriodType(2);
        contract.setPeriodValue(memberLevel.getRechargeMonths());
        contract.setAmount(memberLevel.getCurrentPrice());
        contract.setWechatPlanId(memberLevel.getWechatPlanId());
        contract.setRequestSerial(requestSerial);
        contract.setChannelResponseSnapshot(response.getRawXml());
        contract.setRemark(dto.getRemark());
        contract.setCreateTime(new Date());
        contract.setUpdateTime(new Date());
        this.save(contract);
    }

    /**
     * 生成 APP 调起微信支付所需的二次签名参数。
     *
     * <p>注意字段名需要保持微信 OpenSDK 约定：partnerid、prepayid、noncestr、timestamp。
     * 返回给前端时为了 Java 命名规范，用 partnerId、prepayId 等字段承载。</p>
     */
    private WechatContractOrderVO buildAppPayVO(String purchaseNo,
                                                String contractNo,
                                                MemberLevel memberLevel,
                                                WechatContractOrderResponse response) {
        String timeStamp = String.valueOf(Instant.now().getEpochSecond());
        String nonceStr = UUID.randomUUID().toString().replace("-", "");

        Map<String, String> payParams = new LinkedHashMap<>();
        payParams.put("appid", properties.getAppId());
        payParams.put("partnerid", properties.getMchId());
        payParams.put("prepayid", response.getPrepayId());
        payParams.put("package", "Sign=WXPay");
        payParams.put("noncestr", nonceStr);
        payParams.put("timestamp", timeStamp);

        String sign = WechatPayV2XmlUtil.sign(payParams, properties.getApiV2Key(), properties.getSignType());

        WechatContractOrderVO vo = new WechatContractOrderVO();
        vo.setPurchaseNo(purchaseNo);
        vo.setContractNo(contractNo);
        vo.setPrepayId(response.getPrepayId());
        vo.setContractId(response.getContractId());
        vo.setAppId(properties.getAppId());
        vo.setPartnerId(properties.getMchId());
        vo.setPackageValue("Sign=WXPay");
        vo.setNonceStr(nonceStr);
        vo.setTimeStamp(timeStamp);
        vo.setSign(sign);
        vo.setSignType(properties.getSignType());
        vo.setLevelId(memberLevel.getId());
        vo.setLevelName(memberLevel.getLevelName());
        vo.setAmount(memberLevel.getCurrentPrice());
        vo.setMessage("请调起微信完成首期支付和自动续费签约");
        return vo;
    }

    private int toFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_UP)
                .intValueExact();
    }

    private String buildNo(String prefix) {
        return prefix + System.currentTimeMillis()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
