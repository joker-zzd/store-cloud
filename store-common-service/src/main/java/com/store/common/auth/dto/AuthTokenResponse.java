package com.store.common.auth.dto;

import java.util.List;

public record AuthTokenResponse(
        String tokenType,
        String accessToken,
        long accessTokenExpiresInSeconds,
        String refreshToken,
        long refreshTokenExpiresInSeconds,
        Long userId,
        String username,
        String nickname,
        List<String> roles
) {
}
