package com.store.service;

import com.store.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.domain.dto.UpdatePasswordDTO;

/**
* @author 19256
* @description 针对表【sys_user(后台用户信息表)】的数据库操作Service
* @createDate 2026-03-28 14:52:47
*/
public interface SysUserService extends IService<SysUser> {
    UserAuthInfo getAuthInfoByUsername(String username);

    void updatePassword(UpdatePasswordDTO updatePasswordDTO);
}
