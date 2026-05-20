package com.store.service;

import com.store.domain.MemberLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.vo.WechatSubscriptionLevelVO;

import java.util.List;

/**
 * @author 19256
 * @description 针对表【member_level(用户等级表（充值月份+价格+赠积分+积分有效期规则）)】的数据库操作Service
 * @createDate 2026-05-17 17:43:14
 */
public interface MemberLevelService extends IService<MemberLevel> {

    /**
     * 查询 微信自动续费套餐。
     */
    List<WechatSubscriptionLevelVO> listWechatSubscriptionLevels();
}
