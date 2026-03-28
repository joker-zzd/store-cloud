package com.store.client;

import com.store.common.auth.dto.UserAuthInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/internal/auth-info")
    UserAuthInfo getAuthInfoByUsername(@RequestParam("username") String username);
}
