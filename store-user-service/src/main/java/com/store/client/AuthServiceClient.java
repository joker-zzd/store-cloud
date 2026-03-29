package com.store.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @PostMapping("/auth/internal/sessions/invalidate")
    void invalidateUserSessions(@RequestParam("userId") Long userId);
}