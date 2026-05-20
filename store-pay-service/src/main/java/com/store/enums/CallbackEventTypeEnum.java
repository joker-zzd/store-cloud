package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallbackEventTypeEnum {
    WAIT(0, "待处理"),
    PROCESSING(1, "处理中"),
    SUCCESS(2, "处理成功"),
    FAILED(3, "处理失败"),
    DUPLICATE_IGNORE(4, "重复忽略");

    private final Integer code;
    private final String desc;
}
