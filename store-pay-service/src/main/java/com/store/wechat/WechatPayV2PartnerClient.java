package com.store.wechat;

import com.store.common.exception.BusinessException;
import com.store.config.WechatSubscriptionProperties;
import com.store.wechat.dto.WechatContractOrderResponse;
import com.store.wechat.dto.WechatPartnerPreEntrustResponse;
import com.store.wechat.util.WechatPayV2XmlUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 微信支付 V2 委托代扣 Client。
 *
 * <p>这里统一封装 XML 组包、签名、HTTP 调用、响应验签和错误转换，
 * 业务 Service 只关心会员套餐、订单号、签约协议号这些业务参数。</p>
 */
@Component
public class WechatPayV2PartnerClient {

    private final RestTemplate restTemplate;
    private final WechatSubscriptionProperties properties;

    public WechatPayV2PartnerClient(RestTemplate restTemplate,
                                    WechatSubscriptionProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * 调用微信支付中签约接口。
     *
     * <p>文档地址：https://pay.weixin.qq.com/doc/v2/merchant/4011987320</p>
     * <p>接口地址：POST https://api.mch.weixin.qq.com/pay/contractorder</p>
     *
     * @param planId                 微信委托代扣模板 ID
     * @param contractCode           商户侧签约协议号，对应本地 contract_no
     * @param requestSerial          商户侧请求序列号，同一签约请求内保持唯一
     * @param contractDisplayAccount 签约页展示账号，不要传微信昵称、表情等特殊字符
     * @param outTradeNo             商户侧首期支付订单号
     * @param body                   商品描述
     * @param detail                 商品详情，可为空
     * @param attach                 附加数据，可为空，微信会在支付回调中原样返回
     * @param totalFee               首期支付金额，单位：分
     * @param clientIp               客户端 IP
     * @return 微信支付中签约响应
     */
    public WechatContractOrderResponse contractOrder(String planId,
                                                     String contractCode,
                                                     String requestSerial,
                                                     String contractDisplayAccount,
                                                     String outTradeNo,
                                                     String body,
                                                     String detail,
                                                     String attach,
                                                     Integer totalFee,
                                                     String clientIp) {
        validateContractOrderConfig();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("appid", properties.getAppId());
        params.put("mch_id", properties.getMchId());
        params.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        params.put("body", body);
        params.put("detail", detail);
        params.put("attach", attach);
        params.put("out_trade_no", outTradeNo);
        params.put("total_fee", String.valueOf(totalFee));
        params.put("spbill_create_ip", clientIp);
        params.put("notify_url", properties.getPayNotifyUrl());
        params.put("trade_type", properties.getTradeType());

        // 以下字段是“支付中签约”的签约信息，支付回调和签约回调是两条链路。
        params.put("plan_id", planId);
        params.put("contract_code", contractCode);
        params.put("request_serial", requestSerial);
        params.put("contract_display_account", contractDisplayAccount);
        params.put("contract_notify_url", properties.getContractNotifyUrl());

        // 项目默认使用 HMAC-SHA256；如果商户平台仍使用 MD5，可通过配置切换。
        params.put("sign_type", properties.getSignType());
        params.put("sign", WechatPayV2XmlUtil.sign(params, properties.getApiV2Key(), properties.getSignType()));

        Map<String, String> responseMap = postXml(properties.getContractOrderUrl(), params);
        assertContractOrderSuccessResponse(responseMap);

        WechatContractOrderResponse response = new WechatContractOrderResponse();
        response.setAppId(responseMap.get("appid"));
        response.setMchId(responseMap.get("mch_id"));
        response.setNonceStr(responseMap.get("nonce_str"));
        response.setTradeType(responseMap.get("trade_type"));
        response.setPrepayId(responseMap.get("prepay_id"));
        response.setContractId(responseMap.get("contract_id"));
        response.setRawXml(responseMap.get("_raw_xml"));
        return response;
    }

    /**
     * 保留已有的 APP 纯签约预签约能力，避免支付中签约改造影响旧接口。
     */
    public WechatPartnerPreEntrustResponse preEntrustWeb(String planId,
                                                         String contractCode,
                                                         String requestSerial,
                                                         String contractDisplayAccount) {
        validateBaseConfig();
        validateContractNotifyUrl();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("appid", properties.getAppId());
        params.put("mch_id", properties.getMchId());
        if (StringUtils.hasText(properties.getSubMchId()) && !"XXX".equals(properties.getSubMchId())) {
            params.put("sub_mch_id", properties.getSubMchId());
        }
        if (StringUtils.hasText(properties.getSubAppId()) && !"XXX".equals(properties.getSubAppId())) {
            params.put("sub_appid", properties.getSubAppId());
        }
        params.put("plan_id", planId);
        params.put("contract_code", contractCode);
        params.put("request_serial", requestSerial);
        params.put("contract_display_account", contractDisplayAccount);
        params.put("notify_url", properties.getContractNotifyUrl());
        params.put("version", "1.0");
        params.put("sign_type", properties.getSignType());
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("return_app", "Y");
        params.put("sign", WechatPayV2XmlUtil.sign(params, properties.getApiV2Key(), properties.getSignType()));

        Map<String, String> responseMap = postXml(properties.getPartnerPreEntrustWebUrl(), params);
        assertPreEntrustSuccessResponse(responseMap);

        WechatPartnerPreEntrustResponse response = new WechatPartnerPreEntrustResponse();
        response.setPreEntrustwebId(responseMap.get("pre_entrustweb_id"));
        response.setMiniProgramUsername(responseMap.get("miniprogram_username"));
        response.setMiniProgramPath(responseMap.get("miniprogram_path"));
        response.setRawXml(responseMap.get("_raw_xml"));
        return response;
    }

    private Map<String, String> postXml(String url, Map<String, String> params) {
        String requestXml = WechatPayV2XmlUtil.toXml(params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(List.of(MediaType.APPLICATION_XML, MediaType.TEXT_XML));

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestXml, headers),
                String.class
        );

        String responseXml = responseEntity.getBody();
        if (!StringUtils.hasText(responseXml)) {
            throw new BusinessException("微信支付接口返回为空");
        }

        Map<String, String> responseMap = WechatPayV2XmlUtil.fromXml(responseXml);
        responseMap.put("_raw_xml", responseXml);
        return responseMap;
    }

    private void validateContractOrderConfig() {
        validateBaseConfig();
        validatePayNotifyUrl();
        validateContractNotifyUrl();
        if (!StringUtils.hasText(properties.getContractOrderUrl())) {
            throw new BusinessException("微信支付中签约接口地址 contractOrderUrl 未配置");
        }
        if (!StringUtils.hasText(properties.getTradeType())) {
            throw new BusinessException("微信支付 tradeType 未配置");
        }
    }

    private void validateBaseConfig() {
        if (!StringUtils.hasText(properties.getAppId()) || "XXX".equals(properties.getAppId())) {
            throw new BusinessException("微信 appId 未配置");
        }
        if (!StringUtils.hasText(properties.getMchId()) || "XXX".equals(properties.getMchId())) {
            throw new BusinessException("微信商户号 mchId 未配置");
        }
        if (!StringUtils.hasText(properties.getApiV2Key()) || "XXX".equals(properties.getApiV2Key())) {
            throw new BusinessException("微信 APIv2 密钥未配置");
        }
    }

    private void validatePayNotifyUrl() {
        if (!StringUtils.hasText(properties.getPayNotifyUrl())
                || properties.getPayNotifyUrl().contains("你的域名")) {
            throw new BusinessException("微信支付回调地址 payNotifyUrl 未配置");
        }
    }

    private void validateContractNotifyUrl() {
        if (!StringUtils.hasText(properties.getContractNotifyUrl())
                || properties.getContractNotifyUrl().contains("你的域名")) {
            throw new BusinessException("微信签约回调地址 contractNotifyUrl 未配置");
        }
    }

    private void assertContractOrderSuccessResponse(Map<String, String> responseMap) {
        assertReturnSuccess(responseMap, "微信支付中签约");
        assertValidSign(responseMap, "微信支付中签约");
        assertResultSuccess(responseMap, "微信支付中签约");

        if (!StringUtils.hasText(responseMap.get("prepay_id"))) {
            throw new BusinessException("微信支付中签约成功但未返回 prepay_id，原始响应：" + responseMap.get("_raw_xml"));
        }
    }

    private void assertPreEntrustSuccessResponse(Map<String, String> responseMap) {
        assertReturnSuccess(responseMap, "微信预签约");
        assertValidSign(responseMap, "微信预签约");
        assertResultSuccess(responseMap, "微信预签约");

        if (!StringUtils.hasText(responseMap.get("pre_entrustweb_id"))) {
            throw new BusinessException("微信预签约成功但未返回 pre_entrustweb_id，原始响应：" + responseMap.get("_raw_xml"));
        }
    }

    private void assertReturnSuccess(Map<String, String> responseMap, String scene) {
        if (!"SUCCESS".equals(responseMap.get("return_code"))) {
            throw new BusinessException(scene + "通信失败：" + responseMap.get("return_msg"));
        }
    }

    private void assertValidSign(Map<String, String> responseMap, String scene) {
        Map<String, String> signParams = new LinkedHashMap<>(responseMap);
        signParams.remove("_raw_xml");
        boolean validSign = WechatPayV2XmlUtil.verifySign(
                signParams,
                properties.getApiV2Key(),
                properties.getSignType()
        );
        if (!validSign) {
            throw new BusinessException(scene + "响应验签失败");
        }
    }

    private void assertResultSuccess(Map<String, String> responseMap, String scene) {
        if (!"SUCCESS".equals(responseMap.get("result_code"))) {
            throw new BusinessException(scene + "业务失败："
                    + responseMap.get("err_code") + "，" + responseMap.get("err_code_des"));
        }
    }
}
