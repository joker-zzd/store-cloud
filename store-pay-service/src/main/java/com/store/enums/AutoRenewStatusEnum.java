package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AutoRenewStatusEnum {
    OPEN(1, "开启"),
    CLOSE(0, "关闭");
    private final Integer code;
    private final String desc;
}
