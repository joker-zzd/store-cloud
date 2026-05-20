package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.exception.BusinessException;
import com.store.config.WechatSubscriptionProperties;
import com.store.constans.SimplePayConstants;
import com.store.domain.PayNotificationCallback;
import com.store.domain.PaySignContract;
import com.store.domain.UserMemberInfo;
import com.store.domain.UserMemberPurchase;
import com.store.enums.AutoRenewStatusEnum;
import com.store.enums.ContractStatusEnum;
import com.store.enums.PayChannelEnum;
import com.store.mapper.PayNotificationCallbackMapper;
import com.store.service.PayNotificationCallbackService;
import com.store.service.PaySignContractService;
import com.store.service.UserMemberInfoService;
import com.store.service.UserMemberPurchaseService;
import com.store.wechat.util.WechatPayV2XmlUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 支付/签约回调处理。
 *
 * <p>微信 V2 回调是 XML 报文，生产环境必须做到：验签、幂等、落流水、事务更新业务状态，
 * 并按微信要求返回 SUCCESS/FAIL 的 XML。</p>
 */
@Service
public class PayNotificationCallbackServiceImpl
        extends ServiceImpl<PayNotificationCallbackMapper, PayNotificationCallback>
        implements PayNotificationCallbackService {

    private static final int EVENT_SIGN_SUCCESS = 1;
    private static final int EVENT_SIGN_FAILED = 2;
    private static final int EVENT_PAY_SUCCESS = 4;
    private static final int EVENT_PAY_FAILED = 5;

    private static final int PROCESS_SUCCESS = 2;
    private static final int PROCESS_FAILED = 3;
    private static final int PROCESS_DUPLICATE_IGNORE = 4;

    private final WechatSubscriptionProperties properties;
    private final UserMemberPurchaseService userMemberPurchaseService;
    private final UserMemberInfoService userMemberInfoService;
    private final PaySignContractService paySignContractService;

    public PayNotificationCallbackServiceImpl(WechatSubscriptionProperties properties,
                                              UserMemberPurchaseService userMemberPurchaseService,
                                              UserMemberInfoService userMemberInfoService,
                                              PaySignContractService paySignContractService) {
        this.properties = properties;
        this.userMemberPurchaseService = userMemberPurchaseService;
        this.userMemberInfoService = userMemberInfoService;
        this.paySignContractService = paySignContractService;
    }

    /**
     * 处理微信支付结果回调。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleWechatPayNotify(String rawXml) {
        Map<String, String> notifyMap = parseAndVerify(rawXml, "微信支付回调");
        String outTradeNo = notifyMap.get("out_trade_no");
        String transactionId = notifyMap.get("transaction_id");
        String resultCode = notifyMap.get("result_code");

        try {
            UserMemberPurchase purchase = getPurchaseByNo(outTradeNo);
            int eventType = "SUCCESS".equals(resultCode) ? EVENT_PAY_SUCCESS : EVENT_PAY_FAILED;

            if (isCallbackHandled(transactionId, outTradeNo, eventType)) {
                saveCallback(notifyMap, rawXml, purchase, null, eventType, PROCESS_DUPLICATE_IGNORE, "重复支付回调");
                return successXml();
            }

            if (SimplePayConstants.PAY_STATUS_SUCCESS.equals(purchase.getPayStatus())) {
                saveCallback(notifyMap, rawXml, purchase, null, eventType, PROCESS_DUPLICATE_IGNORE, "订单已支付成功");
                return successXml();
            }

            if ("SUCCESS".equals(resultCode)) {
                markPurchaseSuccess(purchase, notifyMap);
                saveCallback(notifyMap, rawXml, purchase, null, EVENT_PAY_SUCCESS, PROCESS_SUCCESS, null);
            } else {
                markPurchaseFailed(purchase);
                saveCallback(notifyMap, rawXml, purchase, null, EVENT_PAY_FAILED, PROCESS_SUCCESS,
                        notifyMap.get("err_code_des"));
            }
            return successXml();
        } catch (Exception exception) {
            saveFailedCallback(notifyMap, rawXml, EVENT_PAY_FAILED, exception.getMessage());
            return failXml(exception.getMessage());
        }
    }

    /**
     * 处理微信签约结果回调。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleWechatContractNotify(String rawXml) {
        Map<String, String> notifyMap = parseAndVerify(rawXml, "微信签约回调");
        String contractCode = notifyMap.get("contract_code");
        String requestSerial = notifyMap.get("request_serial");
        String contractId = notifyMap.get("contract_id");
        String resultCode = notifyMap.get("result_code");
        String callbackIdentity = firstText(contractId, requestSerial, contractCode);

        try {
            PaySignContract contract = getContract(contractCode, requestSerial);
            int eventType = "SUCCESS".equals(resultCode) ? EVENT_SIGN_SUCCESS : EVENT_SIGN_FAILED;

            if (isCallbackHandled(callbackIdentity, contractCode, eventType)) {
                saveCallback(notifyMap, rawXml, null, contract, eventType, PROCESS_DUPLICATE_IGNORE, "重复签约回调");
                return successXml();
            }

            if (ContractStatusEnum.SIGNED.getCode().equals(contract.getContractStatus())) {
                saveCallback(notifyMap, rawXml, null, contract, eventType, PROCESS_DUPLICATE_IGNORE, "协议已签约成功");
                return successXml();
            }

            if ("SUCCESS".equals(resultCode)) {
                markContractSigned(contract, notifyMap);
                saveCallback(notifyMap, rawXml, null, contract, EVENT_SIGN_SUCCESS, PROCESS_SUCCESS, null);
            } else {
                markContractFailed(contract, notifyMap);
                saveCallback(notifyMap, rawXml, null, contract, EVENT_SIGN_FAILED, PROCESS_SUCCESS,
                        notifyMap.get("err_code_des"));
            }
            return successXml();
        } catch (Exception exception) {
            saveFailedCallback(notifyMap, rawXml, EVENT_SIGN_FAILED, exception.getMessage());
            return failXml(exception.getMessage());
        }
    }

    private Map<String, String> parseAndVerify(String rawXml, String scene) {
        if (!StringUtils.hasText(rawXml)) {
            throw new BusinessException(scene + "报文为空");
        }
        Map<String, String> notifyMap = WechatPayV2XmlUtil.fromXml(rawXml);

        if (!"SUCCESS".equals(notifyMap.get("return_code"))) {
            throw new BusinessException(scene + "通信状态失败：" + notifyMap.get("return_msg"));
        }
        boolean validSign = WechatPayV2XmlUtil.verifySign(
                notifyMap,
                properties.getApiV2Key(),
                properties.getSignType()
        );
        if (!validSign) {
            throw new BusinessException(scene + "验签失败");
        }
        return notifyMap;
    }

    private UserMemberPurchase getPurchaseByNo(String outTradeNo) {
        if (!StringUtils.hasText(outTradeNo)) {
            throw new BusinessException("微信支付回调缺少 out_trade_no");
        }
        UserMemberPurchase purchase = userMemberPurchaseService.lambdaQuery()
                .eq(UserMemberPurchase::getPurchaseNo, outTradeNo)
                .one();
        if (purchase == null) {
            throw new BusinessException("本地购买订单不存在：" + outTradeNo);
        }
        return purchase;
    }

    private PaySignContract getContract(String contractCode, String requestSerial) {
        PaySignContract contract = null;
        if (StringUtils.hasText(contractCode)) {
            contract = paySignContractService.lambdaQuery()
                    .eq(PaySignContract::getContractNo, contractCode)
                    .one();
        }
        if (contract == null && StringUtils.hasText(requestSerial)) {
            contract = paySignContractService.lambdaQuery()
                    .eq(PaySignContract::getRequestSerial, requestSerial)
                    .one();
        }
        if (contract == null) {
            throw new BusinessException("本地签约协议不存在");
        }
        return contract;
    }

    private void markPurchaseSuccess(UserMemberPurchase purchase, Map<String, String> notifyMap) {
        Date payTime = parseWechatTime(notifyMap.get("time_end"));
        if (payTime == null) {
            payTime = new Date();
        }
        Date expireTime = addMonths(payTime, purchase.getRechargeMonths());

        purchase.setPayStatus(SimplePayConstants.PAY_STATUS_SUCCESS);
        purchase.setPayTime(payTime);
        purchase.setOutTradeNo(firstText(notifyMap.get("transaction_id"), purchase.getOutTradeNo()));
        purchase.setMemberEffectiveTime(payTime);
        purchase.setMemberExpireTime(expireTime);
        purchase.setMemberStatus(SimplePayConstants.MEMBER_STATUS_EFFECTIVE);
        purchase.setUpdateTime(new Date());
        userMemberPurchaseService.updateById(purchase);

        activateMemberInfo(purchase, payTime, expireTime);
    }

    private void markPurchaseFailed(UserMemberPurchase purchase) {
        purchase.setPayStatus(SimplePayConstants.PAY_STATUS_FAILED);
        purchase.setUpdateTime(new Date());
        userMemberPurchaseService.updateById(purchase);
    }

    private void activateMemberInfo(UserMemberPurchase purchase, Date startTime, Date endTime) {
        UserMemberInfo memberInfo = userMemberInfoService.lambdaQuery()
                .eq(UserMemberInfo::getUserId, purchase.getUserId())
                .one();
        if (memberInfo == null) {
            memberInfo = new UserMemberInfo();
            memberInfo.setUserId(purchase.getUserId());
            memberInfo.setCreateTime(new Date());
        }
        memberInfo.setLevelId(purchase.getLevelId());
        memberInfo.setStartTime(startTime);
        memberInfo.setEndTime(endTime);
        memberInfo.setMemberStatus(SimplePayConstants.MEMBER_STATUS_EFFECTIVE);
        memberInfo.setIntegral(purchase.getGiveIntegral());
        memberInfo.setUpdateTime(new Date());
        userMemberInfoService.saveOrUpdate(memberInfo);
    }

    private void markContractSigned(PaySignContract contract, Map<String, String> notifyMap) {
        contract.setThirdPartyContractNo(notifyMap.get("contract_id"));
        contract.setExternalUserId(firstText(notifyMap.get("openid"), notifyMap.get("sub_openid")));
        contract.setContractStatus(ContractStatusEnum.SIGNED.getCode());
        contract.setAutoRenewStatus(AutoRenewStatusEnum.OPEN.getCode());
        contract.setSignTime(new Date());
        contract.setChannelResponseSnapshot(notifyMap.toString());
        contract.setUpdateTime(new Date());
        paySignContractService.updateById(contract);
    }

    private void markContractFailed(PaySignContract contract, Map<String, String> notifyMap) {
        contract.setContractStatus(ContractStatusEnum.SIGN_FAILED.getCode());
        contract.setAutoRenewStatus(AutoRenewStatusEnum.CLOSE.getCode());
        contract.setChannelResponseSnapshot(notifyMap.toString());
        contract.setRemark(firstText(notifyMap.get("err_code_des"), contract.getRemark()));
        contract.setUpdateTime(new Date());
        paySignContractService.updateById(contract);
    }

    private boolean isCallbackHandled(String thirdCallbackNo, String outTradeNo, Integer eventType) {
        if (!StringUtils.hasText(thirdCallbackNo) && !StringUtils.hasText(outTradeNo)) {
            return false;
        }
        return this.lambdaQuery()
                .eq(PayNotificationCallback::getPayChannel, PayChannelEnum.WECHAT.getCode())
                .eq(PayNotificationCallback::getEventType, eventType)
                .eq(PayNotificationCallback::getProcessStatus, PROCESS_SUCCESS)
                .and(wrapper -> {
                    if (StringUtils.hasText(thirdCallbackNo)) {
                        wrapper.eq(PayNotificationCallback::getThirdCallbackNo, thirdCallbackNo);
                    }
                    if (StringUtils.hasText(outTradeNo)) {
                        wrapper.or().eq(PayNotificationCallback::getOutTradeNo, outTradeNo);
                    }
                })
                .count() > 0;
    }

    private void saveCallback(Map<String, String> notifyMap,
                              String rawXml,
                              UserMemberPurchase purchase,
                              PaySignContract contract,
                              Integer eventType,
                              Integer processStatus,
                              String failReason) {
        PayNotificationCallback callback = new PayNotificationCallback();
        callback.setCallbackNo(buildCallbackNo());
        callback.setThirdCallbackNo(firstText(notifyMap.get("transaction_id"),
                notifyMap.get("contract_id"),
                notifyMap.get("request_serial")));
        callback.setPayChannel(PayChannelEnum.WECHAT.getCode());
        callback.setEventType(eventType);
        if (purchase != null) {
            callback.setUserId(purchase.getUserId());
            callback.setLevelId(purchase.getLevelId());
            callback.setOutTradeNo(purchase.getPurchaseNo());
        }
        if (contract != null) {
            callback.setUserId(contract.getUserId() == null ? null : contract.getUserId().intValue());
            callback.setLevelId(contract.getLevelId());
            callback.setSignContractId(contract.getId());
            callback.setOutTradeNo(notifyMap.get("out_trade_no"));
        }
        callback.setNotifyTime(new Date());
        callback.setProcessStatus(processStatus);
        callback.setProcessTimes(1);
        callback.setProcessTime(new Date());
        callback.setFailReason(failReason);
        callback.setRawBody(rawXml);
        callback.setCreateTime(new Date());
        callback.setUpdateTime(new Date());
        this.save(callback);
    }

    private void saveFailedCallback(Map<String, String> notifyMap,
                                    String rawXml,
                                    Integer eventType,
                                    String failReason) {
        PayNotificationCallback callback = new PayNotificationCallback();
        callback.setCallbackNo(buildCallbackNo());
        callback.setThirdCallbackNo(firstText(notifyMap.get("transaction_id"),
                notifyMap.get("contract_id"),
                notifyMap.get("request_serial")));
        callback.setPayChannel(PayChannelEnum.WECHAT.getCode());
        callback.setEventType(eventType);
        callback.setOutTradeNo(notifyMap.get("out_trade_no"));
        callback.setNotifyTime(new Date());
        callback.setProcessStatus(PROCESS_FAILED);
        callback.setProcessTimes(1);
        callback.setProcessTime(new Date());
        callback.setFailReason(failReason);
        callback.setRawBody(rawXml);
        callback.setCreateTime(new Date());
        callback.setUpdateTime(new Date());
        this.save(callback);
    }

    private String successXml() {
        return responseXml("SUCCESS", "OK");
    }

    private String failXml(String message) {
        return responseXml("FAIL", StringUtils.hasText(message) ? message : "处理失败");
    }

    private String responseXml(String returnCode, String returnMsg) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("return_code", returnCode);
        response.put("return_msg", returnMsg);
        return WechatPayV2XmlUtil.toXml(response);
    }

    private Date parseWechatTime(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Date addMonths(Date startTime, Integer months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, months == null || months <= 0 ? 1 : months);
        return calendar.getTime();
    }

    private String firstText(String... values) {
        return List.of(values).stream()
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(null);
    }

    private String buildCallbackNo() {
        return "CB" + System.currentTimeMillis()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
