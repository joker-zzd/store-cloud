package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表
 */
@TableName("oms_order")
@Data
public class Order {

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 促销优惠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 支付方式：0=未选择，1=支付宝，2=微信
     */
    private Integer payType;

    /**
     * 订单状态：0=待付款
     */
    private Integer status;

    /**
     * 订单类型：0=正常订单，1=秒杀订单
     */
    private Integer orderType;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 收货人姓名快照
     */
    private String receiverName;

    /**
     * 收货人电话快照
     */
    private String receiverPhone;

    /**
     * 收货详细地址快照
     */
    private String receiverDetailAddress;

    /**
     * 订单备注
     */
    private String note;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
