package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.UserMemberInfo;
import com.store.service.UserMemberInfoService;
import com.store.mapper.UserMemberInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【user_member_info(会员信息表)】的数据库操作Service实现
* @createDate 2026-05-17 17:43:38
*/
@Service
public class UserMemberInfoServiceImpl extends ServiceImpl<UserMemberInfoMapper, UserMemberInfo>
    implements UserMemberInfoService{

}




