package com.store.redis;

import java.time.Duration;

public class DetailRedisConstants {
    /**
     * 商品详情缓存key前缀
     */
    public static final String PRODUCT_DETAIL_KEY_PREFIX="product:detail:";

    /**
     * 空值
     */
    public static final String NULL_VALUE="null";

    /**
     * 商品详情缓存有效期
     */
    public static final Duration PRODUCT_DETAIL_TTL =Duration.ofMinutes(30);

    /**
     * 空值缓存有效期
     */
    public static final Duration PRODUCT_NULL_TTL =Duration.ofMinutes(30);
}
