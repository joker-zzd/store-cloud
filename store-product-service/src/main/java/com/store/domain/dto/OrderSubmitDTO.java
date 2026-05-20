package com.store.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 提交订单请求参数
 */
@Data
public class OrderSubmitDTO {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 需要结算的购物车项ID列表
     */
    private List<Long> cartItemIds;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 优惠券ID，不使用优惠券时为空
     */
    private Long couponId;

    /**
     * 订单备注
     */
    private String note;
}
