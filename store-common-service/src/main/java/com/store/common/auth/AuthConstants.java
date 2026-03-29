package com.store.common.auth;

public final class AuthConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "access";
    public static final String REFRESH_TOKEN = "refresh";

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_NICKNAME = "nickname";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";
    public static final String CLAIM_TOKEN_ID = "tokenId";

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_NICKNAME = "X-Nickname";
    public static final String HEADER_USER_ROLES = "X-User-Roles";

    private AuthConstants() {
    }

    public static String refreshTokenKey(String refreshToken) {
        return "auth:refresh:" + refreshToken;
    }

    public static String userRefreshTokensKey(Long userId) {
        return "auth:user:refresh:" + userId;
    }
}