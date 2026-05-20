package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeductStatusEnum {
    WAIT(0, "待发起"),
    PROCESSING(1, "扣款中"),
    SUCCESS(2, "扣款成功"),
    FAILED(3, "扣款失败"),
    CLOSED(4, "已关闭"),
    CANCELED(5, "已取消");
    private final Integer code;
    private final String desc;
}
