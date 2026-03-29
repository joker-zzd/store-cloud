package com.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.auth.dto.UserAuthInfo;
import com.store.common.resultvo.ResultVO;
import com.store.domain.SysUser;
import com.store.domain.dto.UpdatePasswordDTO;

public interface SysUserService extends IService<SysUser> {
    UserAuthInfo getAuthInfoByUsername(String username);

    ResultVO<Void> updatePassword(UpdatePasswordDTO updatePasswordDTO);
}