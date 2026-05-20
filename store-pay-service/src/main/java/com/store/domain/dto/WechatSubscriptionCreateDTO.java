package com.store.domain.dto;

import lombok.Data;

@Data
public class WechatSubscriptionCreateDTO {

    /**
     * 用户选择的会员套餐ID
     */
    private Integer levelId;

    /**
     * 微信 openid。
     * 如果后续项目登录体系能直接拿到 openid，则可以不让 APP 传。
     * 当前阶段先保留，方便完整跑通微信签约链路。
     */
    private String openid;

    /**
     * 备注，可选
     */
    private String remark;
}
