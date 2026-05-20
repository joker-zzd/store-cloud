package com.store.service;

import com.store.domain.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.vo.CouponReceiveVO;

/**
 * @author 19256
 * @description 针对表【coupon(优惠券表)】的数据库操作Service
 * @createDate 2026-04-12 00:50:12
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 领取优惠券
     */
    CouponReceiveVO receiveCoupon(Long couponId);

    /**
     * 预加载优惠券库存
     */
    void preloadCouponStock(Long couponId);

    /**
     * 启动定时任务
     */
    void startScheduledCoupons();

}
