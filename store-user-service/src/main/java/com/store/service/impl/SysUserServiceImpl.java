package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.SysUser;
import com.store.service.SysUserService;
import com.store.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【sys_user(后台用户信息表)】的数据库操作Service实现
* @createDate 2026-03-28 14:52:47
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




