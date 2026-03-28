package com.store.common.auth;

import java.util.List;

public record JwtUserClaims(
        Long userId,
        String username,
        String nickname,
        List<String> roles,
        String tokenId,
        String tokenType
) {
}
