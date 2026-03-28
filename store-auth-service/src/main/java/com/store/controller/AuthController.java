package com.store.controller;

import com.store.common.auth.dto.AuthTokenResponse;
import com.store.common.auth.dto.LoginRequest;
import com.store.common.auth.dto.LogoutRequest;
import com.store.common.auth.dto.RefreshTokenRequest;
import com.store.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "认证相关接口")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码进行 Authenticate，成功后返回 access token 和 refresh token。")
    public AuthTokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新 token", description = "使用 refresh token 轮换并签发新的一组 access token 与 refresh token。")
    public AuthTokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户退出登录", description = "使当前提交的 refresh token 失效，并结束当前登录会话。")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}