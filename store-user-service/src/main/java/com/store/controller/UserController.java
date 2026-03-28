package com.store.controller;

import com.store.common.auth.dto.UserAuthInfo;
import com.store.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/internal")
public class UserController {
    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/auth-info")
    public UserAuthInfo getAuthInfoByUsername(@RequestParam("username") String username) {
        return sysUserService.getAuthInfoByUsername(username);
    }
}
