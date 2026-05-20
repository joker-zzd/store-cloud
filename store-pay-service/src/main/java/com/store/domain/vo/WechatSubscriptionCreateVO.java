package com.store.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建 Android 微信订阅订单响应。
 */
@Data
public class WechatSubscriptionCreateVO {

    /**
     * 会员购买单号
     */
    private String purchaseNo;

    /**
     * 本地签约协议号，对应微信 contract_code
     */
    private String contractNo;

    /**
     * 微信预签约ID
     */
    private String preContractId;

    /**
     * 使用 WXLaunchMiniProgram 拉起的小程序 username
     */
    private String miniProgramUsername;

    /**
     * 使用 WXLaunchMiniProgram 拉起小程序的 path
     */
    private String miniProgramPath;

    /**
     * 首期支付金额
     */
    private BigDecimal amount;

    /**
     * 套餐名称
     */
    private String levelName;

    /**
     * 提示信息
     */
    private String message;
}
