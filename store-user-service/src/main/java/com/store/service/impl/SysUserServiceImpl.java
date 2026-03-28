package com.store.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.domain.SysUser;
import com.store.mapper.SysRoleMapper;
import com.store.mapper.SysUserMapper;
import com.store.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 19256
* @description 针对表【sys_user(后台用户信息表)】的数据库操作Service实现
* @createDate 2026-03-28 14:52:47
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    private final SysRoleMapper sysRoleMapper;

    public SysUserServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
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

}




