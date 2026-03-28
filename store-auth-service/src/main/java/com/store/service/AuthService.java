package com.store.service;

import com.store.common.auth.dto.AuthTokenResponse;
import com.store.common.auth.dto.LoginRequest;
import com.store.common.auth.dto.LogoutRequest;
import com.store.common.auth.dto.RefreshTokenRequest;

public interface AuthService {
    AuthTokenResponse login(LoginRequest request);

    AuthTokenResponse refresh(RefreshTokenRequest request);

    void logout(LogoutRequest request);
}
