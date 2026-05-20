package com.store.redis;

import java.time.Duration;

public class CouponRedisConstants {
    /**
     * 优惠券库存缓存key前缀
     */
    public static final String COUPON_STOCK_KEY_PREFIX = "coupon:stock:";

    /**
     * 优惠券用户领取缓存key前缀
     */
    public static final String COUPON_USER_KEY_PREFIX = "coupon:user:";

    /**
     * 优惠券缓存有效期
     */
    public static final Duration COUPON_CACHE_TTL = Duration.ofDays(7);

    /**
     * 优惠券领取Lua脚本
     */
    public static final String RECEIVE_COUPON_LUA = """
            local stockKey = KEYS[1]
            local userKey = KEYS[2]
            local memberId = ARGV[1]
            
            if redis.call('SISMEMBER', userKey, memberId) == 1 then
                return 2
            end
            
            local stock = tonumber(redis.call('GET', stockKey))
            if stock == nil or stock <= 0 then
                return 1
            end
            
            redis.call('DECR', stockKey)
            redis.call('SADD', userKey, memberId)
            return 0
            """;
}
