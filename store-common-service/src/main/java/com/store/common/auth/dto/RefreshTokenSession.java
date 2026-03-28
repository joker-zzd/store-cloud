package com.store.common.auth.dto;

import java.util.List;

public record RefreshTokenSession(
        Long userId,
        String username,
        String nickname,
        List<String> roles,
        String tokenId
) {
}
