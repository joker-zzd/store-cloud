package com.store.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 微信支付中签约响应。
 *
 * <p>前端 APP 主要使用 appId、partnerId、prepayId、packageValue、nonceStr、
 * timeStamp、sign 拉起微信支付。</p>
 */
@Data
public class WechatContractOrderVO {

    /**
     * 本地会员购买订单号，也是微信 out_trade_no。
     */
    private String purchaseNo;

    /**
     * 本地签约协议号，也是微信 contract_code。
     */
    private String contractNo;

    /**
     * 微信预支付交易会话 ID。
     */
    private String prepayId;

    /**
     * 微信签约协议号；部分场景可能需要等签约回调才最终确认。
     */
    private String contractId;

    /**
     * 微信开放平台 APPID。
     */
    private String appId;

    /**
     * 微信支付商户号。
     */
    private String partnerId;

    /**
     * APP 支付固定值：Sign=WXPay。
     */
    private String packageValue;

    /**
     * APP 支付二次签名随机串。
     */
    private String nonceStr;

    /**
     * APP 支付二次签名时间戳。
     */
    private String timeStamp;

    /**
     * APP 支付二次签名。
     */
    private String sign;

    /**
     * 签名类型，默认 HMAC-SHA256。
     */
    private String signType;

    /**
     * 会员套餐 ID。
     */
    private Integer levelId;

    /**
     * 会员套餐名称。
     */
    private String levelName;

    /**
     * 首期支付金额，单位：元。
     */
    private BigDecimal amount;

    /**
     * 业务提示。
     */
    private String message;
}
