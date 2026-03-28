package com.store.common.auth.dto;

import java.util.List;

public record UserAuthInfo(
        Long userId,
        String username,
        String nickname,
        String password,
        Integer status,
        List<String> roles
) {
}
