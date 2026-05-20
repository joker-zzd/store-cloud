package com.store.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "优惠券抢领结果")
public class CouponReceiveVO {

    @Schema(description = "优惠券ID")
    private Long couponId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "领取结果")
    private String result;
}