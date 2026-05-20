package com.store.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WechatSubscriptionLevelVO {

    /**
     * 会员套餐ID
     */
    private Integer levelId;

    /**
     * 套餐名称
     */
    private String levelName;

    /**
     * 套餐类型：1体验VIP 2VIP 3SVIP 4企业版
     */
    private Integer levelType;

    /**
     * 购买月份数
     */
    private Integer rechargeMonths;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 当前销售价
     */
    private BigDecimal currentPrice;

    /**
     * 赠送积分
     */
    private Integer giveIntegral;

    /**
     * 套餐描述
     */
    private String levelDesc;

    /**
     * 是否已经配置微信自动续费模板。
     * 只有配置了 wechat_plan_id 的套餐，才允许展示为 Android 微信自动续费套餐。
     */
    private Boolean wechatPlanConfigured;
}
