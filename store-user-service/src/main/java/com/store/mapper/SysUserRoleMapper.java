package com.store.mapper;

import com.store.domain.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 19256
 * @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Mapper
 * @createDate 2026-03-28 14:59:46
 * @Entity com.store.domain.SysUserRole
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<UserRole> {

}




