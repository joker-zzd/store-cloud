package com.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.auth.SecurityUtils;
import com.store.common.exception.BusinessException;
import com.store.common.resultvo.ResultVO;
import com.store.domain.Coupon;
import com.store.domain.CouponReceiveRecord;
import com.store.domain.vo.CouponReceiveVO;
import com.store.mapper.CouponReceiveRecordMapper;
import com.store.redis.CouponRedisConstants;
import com.store.service.CouponService;
import com.store.mapper.CouponMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 19256
 * @description 针对表【coupon(优惠券表)】的数据库操作Service实现
 * @createDate 2026-04-12 00:50:12
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
        implements CouponService {

    private final CouponMapper couponMapper;
    private final CouponReceiveRecordMapper couponReceiveRecordMapper;
    private final StringRedisTemplate redisTemplate;

    public CouponServiceImpl(CouponMapper couponMapper,
                             CouponReceiveRecordMapper couponReceiveRecordMapper,
                             StringRedisTemplate redisTemplate) {
        this.couponMapper = couponMapper;
        this.couponReceiveRecordMapper = couponReceiveRecordMapper;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public CouponReceiveVO receiveCoupon(Long couponId) {
        Long memberId = SecurityUtils.getMemberId();
        if (couponId == null || couponId <= 0) {
            throw new BusinessException("优惠券ID不能为空");
        }
        if (memberId <= 0) {
            throw new BusinessException("会员ID不能为空");
        }
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        if (coupon.getStatus() == null || coupon.getStatus() != 3) {
            throw new BusinessException("当前优惠券不可领取");
        }
        if (coupon.getIssueBeginTime() != null && coupon.getIssueBeginTime().after(new Date())) {
            throw new BusinessException("优惠券尚未开始发放");
        }
        if (coupon.getIssueEndTime() != null && coupon.getIssueEndTime().before(new Date())) {
            throw new BusinessException("优惠券发放已结束");
        }
        preloadCouponStock(couponId);
        String stockKey = buildStockKey(couponId);
        String userKey = buildUserKey(couponId);

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(CouponRedisConstants.RECEIVE_COUPON_LUA);
        redisScript.setResultType(Long.class);

        long result = redisTemplate.execute(
                redisScript,
                List.of(stockKey, userKey),
                String.valueOf(memberId)
        );
        if (result == 1L) {
            throw new BusinessException("优惠券已抢完");
        }
        if (result == 2L) {
            throw new BusinessException("您已领取过该优惠券");
        }

        try {
            CouponReceiveRecord receiveRecord = new CouponReceiveRecord();
            receiveRecord.setCouponId(couponId);
            receiveRecord.setMemberId(memberId);
            receiveRecord.setReceiveTime(new Date());
            couponReceiveRecordMapper.insert(receiveRecord);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("您已领取过该优惠券", e);
        } catch (Exception e) {
            rollbackRedis(couponId, memberId);
            throw new BusinessException("保存优惠券领取记录失败", e);
        }
        int updated = couponMapper.update(
                null,
                new LambdaUpdateWrapper<Coupon>()
                        .eq(Coupon::getId, couponId)
                        .apply("issue_num < total_num")
                        .setSql("issue_num = issue_num + 1")
        );
        if (updated <= 0) {
            rollbackRedis(couponId, memberId);
            throw new BusinessException("更新优惠券发放数量失败");
        }
        return CouponReceiveVO.builder()
                .couponId(couponId)
                .memberId(memberId)
                .result("领取成功")
                .build();
    }

    private void rollbackRedis(Long couponId, Long memberId) {
        String stockKey = buildStockKey(couponId);
        String userKey = buildUserKey(couponId);

        redisTemplate.opsForValue().increment(stockKey);
        redisTemplate.opsForSet().remove(userKey, String.valueOf(memberId));
    }

    @Override
    public void preloadCouponStock(Long couponId) {
        String stockKey = buildStockKey(couponId);
        Boolean exists = redisTemplate.hasKey(stockKey);
        if (exists) {
            return;
        }
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        int remainStock = Math.max(
                0,
                (coupon.getTotalNum() == null ? 0 : coupon.getTotalNum()) -
                        (coupon.getIssueNum() == null ? 0 : coupon.getIssueNum())
        );
        redisTemplate.opsForValue().set(
                stockKey,
                String.valueOf(remainStock),
                CouponRedisConstants.COUPON_CACHE_TTL
        );
        redisTemplate.expire(stockKey, CouponRedisConstants.COUPON_CACHE_TTL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startScheduledCoupons() {
        Date now = new Date();
        List<Coupon> couponList = couponMapper.selectList(
                new LambdaQueryWrapper<Coupon>()
                        .in(Coupon::getStatus, 1, 2)
                        .le(Coupon::getIssueBeginTime, now)
                        .ge(Coupon::getIssueEndTime, now)
        );
        if (couponList.isEmpty()) {
            return;
        }
        for (Coupon coupon : couponList) {
            boolean updated = this.update(
                    Wrappers.<Coupon>lambdaUpdate()
                            .eq(Coupon::getId, coupon.getId())
                            .in(Coupon::getStatus, 1, 2)
                            .set(Coupon::getStatus, 3)
            );
            if (updated) {
                preloadCouponStock(coupon.getId());
            }
        }

    }

    private String buildStockKey(Long couponId) {
        return CouponRedisConstants.COUPON_STOCK_KEY_PREFIX + couponId;
    }

    private String buildUserKey(Long couponId) {
        return CouponRedisConstants.COUPON_USER_KEY_PREFIX + couponId;
    }
}




