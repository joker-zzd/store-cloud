package com.store.constans;

public interface SimplePayConstants {
    // 自动续费状态：关闭
    Integer AUTO_RENEW_CLOSE = 0;

    // 自动续费状态：开启
    Integer AUTO_RENEW_OPEN = 1;

    // 客户端平台：Android
    Integer CLIENT_ANDROID = 1;

    // 扣款类型：首期签约支付
    Integer DEDUCT_TYPE_FIRST = 1;

    // 扣款类型：自动续费扣款
    Integer DEDUCT_TYPE_RENEW = 2;

    // 支付状态：待支付
    String PAY_STATUS_PENDING = "pending";

    // 支付状态：支付成功
    String PAY_STATUS_SUCCESS = "success";

    // 支付状态：支付失败
    String PAY_STATUS_FAILED = "failed";

    // 会员状态：未生效
    String MEMBER_STATUS_UNUSED = "unused";

    // 会员状态：生效中
    String MEMBER_STATUS_EFFECTIVE = "effective";

    // 会员状态：已过期
    String MEMBER_STATUS_EXPIRED = "expired";

    // 会员状态：已取消
    String MEMBER_STATUS_CANCELED = "canceled";
}
