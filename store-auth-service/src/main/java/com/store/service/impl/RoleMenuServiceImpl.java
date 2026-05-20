package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.RoleMenu;
import com.store.service.RoleMenuService;
import com.store.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2026-04-12 17:19:40
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

}




