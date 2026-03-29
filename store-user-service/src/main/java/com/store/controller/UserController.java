package com.store.controller;

import com.store.common.auth.dto.UserAuthInfo;
import com.store.domain.dto.UpdatePasswordDTO;
import com.store.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/internal")
@Tag(name="用户管理器",description = "用户相关接口")
public class UserController {
    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/auth-info")
    @Operation(summary = "根据用户名查询用户信息")
    public UserAuthInfo getAuthInfoByUsername(@RequestParam("username") String username) {
        return sysUserService.getAuthInfoByUsername(username);
    }

    @PostMapping("/update/password")
    @Operation(summary = "修改密码")
    public void updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        sysUserService.updatePassword(updatePasswordDTO);
    }
}
