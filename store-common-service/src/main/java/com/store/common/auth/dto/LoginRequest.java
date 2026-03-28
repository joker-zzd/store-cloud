package com.store.common.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
