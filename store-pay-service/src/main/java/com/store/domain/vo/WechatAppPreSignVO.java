package com.store.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * Android APP 纯签约预签约响应。
 */
@Data
public class WechatAppPreSignVO {
    /** 本地签约协议号，对应微信 contract_code */
    private String contractNo;

    /** 微信预签约ID */
    private String preEntrustwebId;

    /** 跳转签约小程序 username，Android WXLaunchMiniProgram 使用 */
    private String miniProgramUsername;

    /** 跳转签约小程序 path，Android WXLaunchMiniProgram 使用 */
    private String miniProgramPath;

    /**
     * 小程序版本。
     *
     * <p>Android WXLaunchMiniProgram 正式版传 0，体验版/开发版后续按微信 OpenSDK 约定调整。
     */
    private Integer miniProgramType;

    /** 套餐ID */
    private Integer levelId;

    /** 套餐名称 */
    private String levelName;

    /** 协议约定金额，后续扣款时使用 */
    private BigDecimal amount;

    /** 提示信息 */
    private String message;
}
