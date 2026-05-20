package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.Role;
import com.store.service.RoleService;
import com.store.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2026-04-12 17:19:46
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




