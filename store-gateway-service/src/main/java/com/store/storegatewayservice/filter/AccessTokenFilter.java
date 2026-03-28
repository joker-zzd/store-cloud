package com.store.storegatewayservice.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.common.auth.AuthConstants;
import com.store.common.auth.JwtTokenUtils;
import com.store.common.auth.JwtUserClaims;
import com.store.storegatewayservice.config.GatewayJwtProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class AccessTokenFilter implements GlobalFilter, Ordered {
    private static final List<String> ALLOWLIST_PREFIXES = List.of("/auth/", "/actuator/", "/error");

    private final GatewayJwtProperties gatewayJwtProperties;
    private final ObjectMapper objectMapper;

    public AccessTokenFilter(GatewayJwtProperties gatewayJwtProperties, ObjectMapper objectMapper) {
        this.gatewayJwtProperties = gatewayJwtProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (isAllowlisted(path)) {
            return chain.filter(exchange);
        }

        String token = JwtTokenUtils.resolveBearerToken(
                exchange.getRequest().getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER)
        );
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange, "Missing access token");
        }

        final JwtUserClaims claims;
        try {
            claims = JwtTokenUtils.parseToken(token, gatewayJwtProperties.getSecret(), AuthConstants.ACCESS_TOKEN);
        } catch (Exception exception) {
            return unauthorized(exchange, "Access token is invalid or expired");
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(AuthConstants.HEADER_USER_ID, String.valueOf(claims.userId()))
                .header(AuthConstants.HEADER_USERNAME, claims.username() == null ? "" : claims.username())
                .header(AuthConstants.HEADER_USER_ROLES, String.join(",", claims.roles() == null ? List.of() : claims.roles()))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private boolean isAllowlisted(String path) {
        return ALLOWLIST_PREFIXES.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = toJsonBytes(Map.of(
                "code", HttpStatus.UNAUTHORIZED.value(),
                "message", message
        ));
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private byte[] toJsonBytes(Map<String, Object> body) {
        try {
            return objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException exception) {
            return "{\"code\":401,\"message\":\"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);
        }
    }
}
