package com.store.wechat.dto;

import lombok.Data;

/**
 * 微信纯签约预签约响应封装。
 */
@Data
public class WechatPartnerPreEntrustResponse {

    /**
     * 微信预签约 ID。
     */
    private String preEntrustwebId;

    /**
     * 微信签约小程序 username。
     */
    private String miniProgramUsername;

    /**
     * 微信签约小程序 path。
     */
    private String miniProgramPath;

    /**
     * 微信原始 XML 响应。
     */
    private String rawXml;
}
