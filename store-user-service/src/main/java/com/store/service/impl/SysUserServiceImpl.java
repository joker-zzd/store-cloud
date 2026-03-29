package com.store.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.client.AuthServiceClient;
import com.store.common.auth.UserContext;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.common.resultvo.ResultVO;
import com.store.domain.User;
import com.store.domain.dto.UpdatePasswordDTO;
import com.store.mapper.SysRoleMapper;
import com.store.mapper.SysUserMapper;
import com.store.service.SysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User>
        implements SysUserService {

    private final SysRoleMapper sysRoleMapper;
    private final UserContext userContext;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceClient authServiceClient;

    public SysUserServiceImpl(SysRoleMapper sysRoleMapper,
                              UserContext userContext,
                              PasswordEncoder passwordEncoder,
                              AuthServiceClient authServiceClient) {
        this.sysRoleMapper = sysRoleMapper;
        this.userContext = userContext;
        this.passwordEncoder = passwordEncoder;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public UserAuthInfo getAuthInfoByUsername(String username) {
        User user = getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username)
                .last("limit 1"));
        if (user == null) {
            return null;
        }
        List<String> roles = sysRoleMapper.selectRoleKeysByUserId(user.getId());
        return new UserAuthInfo(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getStatus(),
                roles == null ? List.of() : roles
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        if (updatePasswordDTO == null) {
            return ResultVO.fail("请求参数不能为空");
        }

        String oldPassword = trim(updatePasswordDTO.getOldPassword());
        String newPassword = trim(updatePasswordDTO.getNewPassword());
        String confirmPassword = trim(updatePasswordDTO.getConfirmPassword());
        if (!newPassword.equals(confirmPassword)) {
            return ResultVO.fail("新密码和确认密码不一致");
        }

        Long userId = userContext.getCurrentUserId();
        Date now = new Date();
        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, userId)
                .eq(User::getStatus, 1)
                .last("limit 1"));
        if (user == null) {
            return ResultVO.fail("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResultVO.fail("原密码错误");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResultVO.fail("新密码不能与原密码相同");
        }

        String encodeNewPassword = passwordEncoder.encode(newPassword);
        boolean update = this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getId, userId)
                .eq(User::getStatus, 1)
                .eq(User::getPassword, user.getPassword())
                .set(User::getPassword, encodeNewPassword)
                .set(User::getUpdateTime, now));
        if (!update) {
            return ResultVO.fail("更新密码失败");
        }

        authServiceClient.invalidateUserSessions(userId);
        return ResultVO.success();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}