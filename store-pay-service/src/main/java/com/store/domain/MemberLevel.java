package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户等级表（充值月份+价格+赠积分+积分有效期规则）
 * @TableName member_level
 */
@TableName(value ="member_level")
@Data
public class MemberLevel {
    /**
     * 等级ID（自增主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 苹果内购商品ID
     */
    private String appleProductId;

    /**
     * 等级名称（如：体验价，VIP）
     */
    private String levelName;

    /**
     * 等级名称英文
     */
    private String levelNameEn;

    /**
     * 等级类型(1:VIP体验，2:VIP，3:SVIP，4:企业版)
     */
    private Integer levelType;

    /**
     * 对应充值套餐月份数（0表示无套餐绑定）
     */
    private Integer rechargeMonths;

    /**
     * 充值套餐原价（元）
     */
    private BigDecimal originalPrice;

    /**
     * 充值套餐现价（元）
     */
    private BigDecimal currentPrice;

    /**
     * 充值该套餐赠送的积分数量
     */
    private Integer giveIntegral;

    /**
     * 邀请首次购买赠送积分
     */
    private Integer inviteGiveIntegral;

    /**
     * 赠送积分的有效期（天数，0表示永久有效）
     */
    private Integer integralValidityDays;

    /**
     * 状态：1=启用，0=禁用
     */
    private Integer status;

    /**
     * 等级图标URL（前端展示用）
     */
    private String iconUrl;

    /**
     * 等级展示顺序：数值越小越靠前
     */
    private Integer sort;

    /**
     * 等级描述
     */
    private String levelDesc;

    /**
     * 等级备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最大子账号数量
     */
    private Integer maxSubAccounts;

    /**
     * 首购分佣比例
     */
    private BigDecimal commissionRate;

    /**
     * 续费分佣比例
     */
    private BigDecimal renewCommissionRate;

    /**
     * 苹果首购分佣比例
     */
    private BigDecimal appleCommissionRate;

    /**
     * 苹果续费分佣比例
     */
    private BigDecimal appleRenewCommissionRate;

    /**
     * 微信自动续费模板ID
     */
    private String wechatPlanId;
}