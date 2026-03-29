package com.store.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.client.AuthServiceClient;
import com.store.common.auth.UserContext;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.common.resultvo.ResultVO;
import com.store.config.PasswordEncoderConfig;
import com.store.domain.SysUser;
import com.store.domain.dto.UpdatePasswordDTO;
import com.store.mapper.SysRoleMapper;
import com.store.mapper.SysUserMapper;
import com.store.service.SysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 19256
 * @description 针对表【sys_user(后台用户信息表)】的数据库操作Service实现
 * @createDate 2026-03-28 14:52:47
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    private final SysRoleMapper sysRoleMapper;
    private final UserContext userContext;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceClient authServiceClient;
    private final PasswordEncoderConfig passwordEncoderConfig;

    public SysUserServiceImpl(SysRoleMapper sysRoleMapper,
                              UserContext userContext,
                              PasswordEncoder passwordEncoder,
                              AuthServiceClient authServiceClient,
                              PasswordEncoderConfig passwordEncoderConfig) {
        this.sysRoleMapper = sysRoleMapper;
        this.userContext = userContext;
        this.passwordEncoder = passwordEncoder;
        this.authServiceClient = authServiceClient;
        this.passwordEncoderConfig = passwordEncoderConfig;
    }

    @Override
    public UserAuthInfo getAuthInfoByUsername(String username) {
        SysUser user = getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username)
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
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        if (updatePasswordDTO == null) {
            ResultVO.fail("请求参数不能为空");
            return;
        }
        String oldPassword = updatePasswordDTO.getOldPassword();
        String newPassword = updatePasswordDTO.getNewPassword();
        String confirmPassword = updatePasswordDTO.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            ResultVO.fail("新密码和确认密码不一致");
            return;
        }
        Long userId = userContext.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now();
        SysUser user = this.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getId, userId)
                .eq(SysUser::getStatus, 1)
                .last("limit 1"));
        if (user == null) {
            ResultVO.fail("用户不存在");
            return;
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            ResultVO.fail("原密码错误");
            return;
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            ResultVO.fail("新密码不能与原密码相同");
            return;
        }
        String encodeNewPassword = passwordEncoder.encode(newPassword);

        boolean update = update(Wrappers.<SysUser>lambdaUpdate()
                .eq(SysUser::getId, userId)
                .eq(SysUser::getStatus, 1)
                .set(SysUser::getPassword, encodeNewPassword)
                .set(SysUser::getUpdateTime, now));
        if (!update) {
            ResultVO.fail("更新密码失败");
            return;
        }
        // 修改密码后，失效该用户全部 refresh token，会话强制下线
        authServiceClient.invalidateUserSessions(userId);
        ResultVO.success();
    }

}




