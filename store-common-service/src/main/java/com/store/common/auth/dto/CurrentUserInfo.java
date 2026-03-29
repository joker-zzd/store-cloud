package com.store.common.auth.dto;

import java.io.Serializable;
import java.util.List;

public record CurrentUserInfo(
        Long userId,
        String username,
        String nickname,
        List<String> roles
) implements Serializable {
}
