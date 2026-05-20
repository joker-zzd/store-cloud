package com.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientPlatformEnum {
    ANDROID(1, "Android");

    private final Integer code;
    private final String desc;
}