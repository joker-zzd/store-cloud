package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝"),
    APPLE(3, "苹果");
    private final Integer code;
    private final String desc;
}
