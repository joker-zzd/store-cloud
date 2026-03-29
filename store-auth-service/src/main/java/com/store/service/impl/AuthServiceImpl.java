package com.store.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.common.auth.AuthConstants;
import com.store.common.auth.JwtTokenUtils;
import com.store.common.auth.JwtUserClaims;
import com.store.common.auth.dto.AuthTokenResponse;
import com.store.common.auth.dto.LoginRequest;
import com.store.common.auth.dto.LogoutRequest;
import com.store.common.auth.dto.RefreshTokenRequest;
import com.store.common.auth.dto.RefreshTokenSession;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.client.UserServiceClient;
import com.store.config.AuthJwtProperties;
import com.store.service.AuthService;
import com.store.service.PasswordService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private static final int USER_STATUS_ENABLED = 1;

    private final UserServiceClient userServiceClient;
    private final PasswordService passwordService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final AuthJwtProperties authJwtProperties;

    public AuthServiceImpl(UserServiceClient userServiceClient,
                           PasswordService passwordService,
                           StringRedisTemplate stringRedisTemplate,
                           ObjectMapper objectMapper,
                           AuthJwtProperties authJwtProperties) {
        this.userServiceClient = userServiceClient;
        this.passwordService = passwordService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.authJwtProperties = authJwtProperties;
    }

    @Override
    public AuthTokenResponse login(LoginRequest request) {
        String username = requireText(request == null ? null : request.username(), "username");
        String password = requireText(request == null ? null : request.password(), "password");

        UserAuthInfo userAuthInfo = userServiceClient.getAuthInfoByUsername(username);
        if (userAuthInfo == null || !passwordService.matches(password, userAuthInfo.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        ensureUserEnabled(userAuthInfo);
        return issueTokenPair(userAuthInfo);
    }

    @Override
    public AuthTokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = requireText(request == null ? null : request.refreshToken(), "refreshToken");
        JwtUserClaims refreshClaims = parseRefreshToken(refreshToken);
        RefreshTokenSession session = loadRefreshTokenSession(refreshToken);
        if (session == null || !Objects.equals(session.userId(), refreshClaims.userId())
                || !Objects.equals(session.tokenId(), refreshClaims.tokenId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid or expired");
        }

        UserAuthInfo latestUserInfo = userServiceClient.getAuthInfoByUsername(session.username());
        if (latestUserInfo == null) {
            revokeRefreshToken(refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        ensureUserEnabled(latestUserInfo);

        revokeRefreshToken(refreshToken);
        return issueTokenPair(latestUserInfo);
    }

    @Override
    public void logout(LogoutRequest request) {
        String refreshToken = requireText(request == null ? null : request.refreshToken(), "refreshToken");
        revokeRefreshToken(refreshToken);
    }

    @Override
    public void invalidateUserSessions(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        }

        String userRefreshTokensKey = AuthConstants.userRefreshTokensKey(userId);
        Set<String> refreshTokens = stringRedisTemplate.opsForSet().members(userRefreshTokensKey);
        if (!CollectionUtils.isEmpty(refreshTokens)) {
            List<String> refreshTokenKeys = refreshTokens.stream()
                    .filter(StringUtils::hasText)
                    .map(AuthConstants::refreshTokenKey)
                    .toList();
            if (!refreshTokenKeys.isEmpty()) {
                stringRedisTemplate.delete(refreshTokenKeys);
            }
        }
        stringRedisTemplate.delete(userRefreshTokensKey);
    }

    private AuthTokenResponse issueTokenPair(UserAuthInfo userAuthInfo) {
        List<String> roles = userAuthInfo.roles() == null ? List.of() : userAuthInfo.roles();
        String accessTokenId = UUID.randomUUID().toString();
        String refreshTokenId = UUID.randomUUID().toString();

        String accessToken = JwtTokenUtils.createToken(
                authJwtProperties.getSecret(),
                authJwtProperties.getAccessTtl(),
                new JwtUserClaims(
                        userAuthInfo.userId(),
                        userAuthInfo.username(),
                        userAuthInfo.nickname(),
                        roles,
                        accessTokenId,
                        AuthConstants.ACCESS_TOKEN
                )
        );

        String refreshToken = JwtTokenUtils.createToken(
                authJwtProperties.getSecret(),
                authJwtProperties.getRefreshTtl(),
                new JwtUserClaims(
                        userAuthInfo.userId(),
                        userAuthInfo.username(),
                        userAuthInfo.nickname(),
                        roles,
                        refreshTokenId,
                        AuthConstants.REFRESH_TOKEN
                )
        );

        saveRefreshTokenSession(refreshToken, new RefreshTokenSession(
                userAuthInfo.userId(),
                userAuthInfo.username(),
                userAuthInfo.nickname(),
                roles,
                refreshTokenId
        ));

        return new AuthTokenResponse(
                "Bearer",
                accessToken,
                authJwtProperties.getAccessTtl().getSeconds(),
                refreshToken,
                authJwtProperties.getRefreshTtl().getSeconds(),
                userAuthInfo.userId(),
                userAuthInfo.username(),
                userAuthInfo.nickname(),
                roles
        );
    }

    private void saveRefreshTokenSession(String refreshToken, RefreshTokenSession session) {
        try {
            stringRedisTemplate.opsForValue().set(
                    AuthConstants.refreshTokenKey(refreshToken),
                    objectMapper.writeValueAsString(session),
                    authJwtProperties.getRefreshTtl()
            );
            if (session.userId() != null) {
                String userRefreshTokensKey = AuthConstants.userRefreshTokensKey(session.userId());
                stringRedisTemplate.opsForSet().add(userRefreshTokensKey, refreshToken);
                stringRedisTemplate.expire(userRefreshTokensKey, authJwtProperties.getRefreshTtl());
            }
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to persist refresh token session", exception);
        }
    }

    private RefreshTokenSession loadRefreshTokenSession(String refreshToken) {
        String json = stringRedisTemplate.opsForValue().get(AuthConstants.refreshTokenKey(refreshToken));
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, RefreshTokenSession.class);
        } catch (JsonProcessingException exception) {
            stringRedisTemplate.delete(AuthConstants.refreshTokenKey(refreshToken));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse refresh token session", exception);
        }
    }

    private void revokeRefreshToken(String refreshToken) {
        RefreshTokenSession session = loadRefreshTokenSessionSafely(refreshToken);
        stringRedisTemplate.delete(AuthConstants.refreshTokenKey(refreshToken));
        if (session == null || session.userId() == null) {
            return;
        }
        String userRefreshTokensKey = AuthConstants.userRefreshTokensKey(session.userId());
        stringRedisTemplate.opsForSet().remove(userRefreshTokensKey, refreshToken);
        Long remaining = stringRedisTemplate.opsForSet().size(userRefreshTokensKey);
        if (remaining != null && remaining <= 0) {
            stringRedisTemplate.delete(userRefreshTokensKey);
        }
    }

    private RefreshTokenSession loadRefreshTokenSessionSafely(String refreshToken) {
        String json = stringRedisTemplate.opsForValue().get(AuthConstants.refreshTokenKey(refreshToken));
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, RefreshTokenSession.class);
        } catch (JsonProcessingException exception) {
            stringRedisTemplate.delete(AuthConstants.refreshTokenKey(refreshToken));
            return null;
        }
    }

    private JwtUserClaims parseRefreshToken(String refreshToken) {
        try {
            return JwtTokenUtils.parseToken(refreshToken, authJwtProperties.getSecret(), AuthConstants.REFRESH_TOKEN);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid or expired", exception);
        }
    }

    private void ensureUserEnabled(UserAuthInfo userAuthInfo) {
        if (!Objects.equals(USER_STATUS_ENABLED, userAuthInfo.status())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
    }

    private String requireText(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must not be blank");
        }
        return value.trim();
    }
}