package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 优惠券表
 * @TableName coupon
 */
@TableName(value ="coupon")
@Data
public class Coupon {
    /**
     * 优惠券ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型:1=普通券
     */
    private Integer type;

    /**
     * 折扣类型:1=满减,2=每满减,3=折扣,4=无门槛
     */
    private Integer discountType;

    /**
     * 限定范围:0=不限定,1=限定
     */
    private Integer specificScope;

    /**
     * 折扣值
     */
    private Integer discountValue;

    /**
     * 使用门槛
     */
    private Integer thresholdAmount;

    /**
     * 最大优惠金额
     */
    private Integer maxDiscountAmount;

    /**
     * 获取方式:1=手动,2=兑换码
     */
    private Integer obtainWay;

    /**
     * 发放开始时间
     */
    private Date issueBeginTime;

    /**
     * 发放结束时间
     */
    private Date issueEndTime;

    /**
     * 有效天数
     */
    private Integer termDays;

    /**
     * 有效期开始
     */
    private Date termBeginTime;

    /**
     * 有效期结束
     */
    private Date termEndTime;

    /**
     * 状态:1=待发放,2=未开始,3=进行中,4=已结束,5=暂停
     */
    private Integer status;

    /**
     * 总量
     */
    private Integer totalNum;

    /**
     * 已发放数量
     */
    private Integer issueNum;

    /**
     * 已使用数量
     */
    private Integer usedNum;

    /**
     * 每人限领
     */
    private Integer userLimit;

    /**
     * 扩展参数
     */
    private Object extParam;

    /**
     * 创建人ID
     */
    private Long creater;

    /**
     * 更新人ID
     */
    private Long updater;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}