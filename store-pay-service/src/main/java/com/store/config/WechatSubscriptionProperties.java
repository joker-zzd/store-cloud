package com.store.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信委托代扣配置。
 *
 * <p>这里全部使用占位符，正式环境建议放到 Nacos 或其他配置中心。</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.subscription")
public class WechatSubscriptionProperties {

    /**
     * 微信商户号 mch_id。
     */
    private String mchId = "XXX";

    /**
     * 微信开放平台移动应用 appid。
     */
    private String appId = "XXX";

    /**
     * 子商户号 sub_mch_id。
     *
     * <p>当前支付中签约接口按普通商户参数组包，保留该字段给后续服务商模式使用。</p>
     */
    private String subMchId = "XXX";

    /**
     * 子商户应用 ID sub_appid，保留给服务商模式使用。
     */
    private String subAppId = "XXX";

    /**
     * 微信支付 APIv3 密钥，后续处理 V3 回调时使用。
     */
    private String apiV3Key = "XXX";

    /**
     * 微信支付 APIv2 密钥，V2 XML 请求签名和响应验签使用。
     */
    private String apiV2Key = "XXX";

    /**
     * 商户证书序列号，后续需要证书的接口再使用。
     */
    private String mchSerialNo = "XXX";

    /**
     * 商户私钥路径，后续 V3 或证书类接口再使用。
     */
    private String privateKeyPath = "XXX";

    /**
     * 微信 V2 签名方式，官方支持 MD5 和 HMAC-SHA256，生产建议使用 HMAC-SHA256。
     */
    private String signType = "HMAC-SHA256";

    /**
     * 微信 V2 支付中签约接口地址。
     */
    private String contractOrderUrl = "https://api.mch.weixin.qq.com/pay/contractorder";

    /**
     * 微信 V2 APP 纯签约预签约接口地址，保留已有纯签约链路。
     */
    private String partnerPreEntrustWebUrl = "https://api.mch.weixin.qq.com/papay/partner/preentrustweb";

    /**
     * 支付成功回调地址，对应微信 notify_url。
     */
    private String payNotifyUrl = "https://你的域名/api/pay/wechat/pay/notify";

    /**
     * 签约状态回调地址，对应微信 contract_notify_url。
     */
    private String contractNotifyUrl = "https://你的域名/api/pay/wechat/contract/notify";

    /**
     * APP 支付交易类型。
     */
    private String tradeType = "APP";
}
