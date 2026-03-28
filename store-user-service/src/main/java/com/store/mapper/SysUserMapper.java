package com.store.mapper;

import com.store.domain.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 19256
 * @description 针对表【sys_user(后台用户信息表)】的数据库操作Mapper
 * @createDate 2026-03-28 14:52:47
 * @Entity com.store.domain.SysUser
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}




