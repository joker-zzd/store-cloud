package com.store.common.auth.dto;

public record LogoutRequest(
        String refreshToken
) {
}
