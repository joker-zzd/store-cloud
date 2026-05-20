package com.store.config.task;

import com.store.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CouponScheduleTask {
    private final CouponService couponService;

    public CouponScheduleTask(CouponService couponService) {
        this.couponService = couponService;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void startCoupons() {
        log.info("开始扫描待发放优惠券");
        couponService.startScheduledCoupons();
    }
}
