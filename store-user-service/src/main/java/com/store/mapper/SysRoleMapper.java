package com.store.mapper;

import com.store.domain.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 19256
 * @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
 * @createDate 2026-03-28 14:59:37
 * @Entity com.store.domain.SysRole
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);
}




