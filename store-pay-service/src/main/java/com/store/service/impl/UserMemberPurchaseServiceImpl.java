package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.UserMemberPurchase;
import com.store.service.UserMemberPurchaseService;
import com.store.mapper.UserMemberPurchaseMapper;
import org.springframework.stereotype.Service;

/**
* @author 19256
* @description 针对表【idnice_user_member_purchase(会员购买明细表（关联会员等级表，记录会员购买行为）)】的数据库操作Service实现
* @createDate 2026-05-19 21:52:37
*/
@Service
public class UserMemberPurchaseServiceImpl extends ServiceImpl<UserMemberPurchaseMapper, UserMemberPurchase>
    implements UserMemberPurchaseService{

}




