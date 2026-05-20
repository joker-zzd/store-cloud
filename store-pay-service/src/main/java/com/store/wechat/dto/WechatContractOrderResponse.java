package com.store.wechat.dto;

import lombok.Data;

/**
 * 微信支付中签约响应封装。
 */
@Data
public class WechatContractOrderResponse {

    /**
     * 微信返回的 appid。
     */
    private String appId;

    /**
     * 微信返回的 mch_id。
     */
    private String mchId;

    /**
     * 微信返回的随机串。
     */
    private String nonceStr;

    /**
     * 交易类型。
     */
    private String tradeType;

    /**
     * 微信预支付交易会话 ID。
     */
    private String prepayId;

    /**
     * 微信签约协议号。
     */
    private String contractId;

    /**
     * 微信原始 XML 响应，用于排查和回调对账。
     */
    private String rawXml;
}
