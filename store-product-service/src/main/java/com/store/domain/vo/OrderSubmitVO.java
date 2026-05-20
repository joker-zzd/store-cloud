package com.store.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提交订单返回结果
 */
@Data
public class OrderSubmitVO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal promotionAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付方式：0=未选择，1=支付宝，2=微信
     */
    private Integer payType;

    /**
     * 订单状态：0=待付款
     */
    private Integer status;
}
