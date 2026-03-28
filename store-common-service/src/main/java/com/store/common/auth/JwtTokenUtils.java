package com.store.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public final class JwtTokenUtils {
    private JwtTokenUtils() {
    }

    public static String createToken(String secret, Duration ttl, JwtUserClaims claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(claims.username())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .claim(AuthConstants.CLAIM_USER_ID, claims.userId())
                .claim(AuthConstants.CLAIM_USERNAME, claims.username())
                .claim(AuthConstants.CLAIM_NICKNAME, claims.nickname())
                .claim(AuthConstants.CLAIM_ROLES, claims.roles())
                .claim(AuthConstants.CLAIM_TOKEN_ID, claims.tokenId())
                .claim(AuthConstants.CLAIM_TOKEN_TYPE, claims.tokenType())
                .signWith(signingKey(secret))
                .compact();
    }

    public static JwtUserClaims parseToken(String token, String secret, String expectedTokenType) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String tokenType = claims.get(AuthConstants.CLAIM_TOKEN_TYPE, String.class);
        if (!Objects.equals(expectedTokenType, tokenType)) {
            throw new IllegalArgumentException("Unexpected token type");
        }

        return new JwtUserClaims(
                getLongClaim(claims, AuthConstants.CLAIM_USER_ID),
                claims.get(AuthConstants.CLAIM_USERNAME, String.class),
                claims.get(AuthConstants.CLAIM_NICKNAME, String.class),
                getRoles(claims),
                claims.get(AuthConstants.CLAIM_TOKEN_ID, String.class),
                tokenType
        );
    }

    public static String resolveBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(AuthConstants.BEARER_PREFIX.length()).trim();
    }

    private static SecretKey signingKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static Long getLongClaim(Claims claims, String claimName) {
        Number number = claims.get(claimName, Number.class);
        return number == null ? null : number.longValue();
    }

    private static List<String> getRoles(Claims claims) {
        Object roles = claims.get(AuthConstants.CLAIM_ROLES);
        if (!(roles instanceof List<?> roleList)) {
            return Collections.emptyList();
        }
        return roleList.stream().map(String::valueOf).toList();
    }
}
