package com.store.controller;

import com.store.common.auth.dto.UserAuthInfo;
import com.store.common.resultvo.ResultVO;
import com.store.domain.dto.UpdatePasswordDTO;
import com.store.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/internal")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {
    private final SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/auth-info")
    @Operation(summary = "根据用户名查询用户认证信息")
    public UserAuthInfo getAuthInfoByUsername(@RequestParam("username") String username) {
        return sysUserService.getAuthInfoByUsername(username);
    }

    @PostMapping("/update/password")
    @Operation(summary = "修改密码")
    public ResultVO<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        sysUserService.updatePassword(updatePasswordDTO);
        return ResultVO.success();
    }
}