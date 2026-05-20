package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractStatusEnum {
    WAIT_SIGN(0, "待签约"),
    SIGNED(1, "已签约"),
    SIGN_FAILED(2, "签约失败"),
    UNSIGNED(3, "已解约"),
    PAUSED(4, "暂停");
    private final Integer code;
    private final String desc;
}