package com.store.controlller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.vo.CouponReceiveVO;
import com.store.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@Tag(name = "优惠卷管理", description = "优惠券相关接口")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/receive/{couponId}")
    @Operation(summary = "抢领优惠券")
    public ResultVO<CouponReceiveVO> receiveCoupon(@PathVariable Long couponId) {
        return ResultVO.success(couponService.receiveCoupon(couponId));
    }

    @PostMapping("/preload/{couponId}")
    @Operation(summary = "预热优惠券库存到Redis")
    public ResultVO<Void> preloadCouponStock(@PathVariable Long couponId) {
        couponService.preloadCouponStock(couponId);
        return ResultVO.success();
    }

    @PostMapping("/schedule/start")
    @Operation(summary = "手动触发优惠券自动发放任务")
    public ResultVO<Void> startScheduledCoupons() {
        couponService.startScheduledCoupons();
        return ResultVO.success();
    }
}
