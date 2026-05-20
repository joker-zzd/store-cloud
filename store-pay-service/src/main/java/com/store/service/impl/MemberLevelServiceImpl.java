package com.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.domain.MemberLevel;
import com.store.domain.vo.WechatSubscriptionLevelVO;
import com.store.service.MemberLevelService;
import com.store.mapper.MemberLevelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 19256
 * @description 针对表【member_level(用户等级表（充值月份+价格+赠积分+积分有效期规则）)】的数据库操作Service实现
 * @createDate 2026-05-17 17:43:14
 */
@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel>
        implements MemberLevelService {

    @Override
    public List<WechatSubscriptionLevelVO> listWechatSubscriptionLevels() {
        return this.lambdaQuery()
                .eq(MemberLevel::getStatus, 1)
                .isNotNull(MemberLevel::getWechatPlanId)
                .orderByAsc(MemberLevel::getSort)
                .list()
                .stream()
                //防止数据库里出现空字符串
                .filter(level -> StringUtils.hasText(level.getWechatPlanId()))
                .map(level -> {
                    WechatSubscriptionLevelVO vo = new WechatSubscriptionLevelVO();
                    vo.setLevelId(level.getId());
                    vo.setLevelName(level.getLevelName());
                    vo.setLevelType(level.getLevelType());
                    vo.setRechargeMonths(level.getRechargeMonths());
                    vo.setOriginalPrice(level.getOriginalPrice());
                    vo.setCurrentPrice(level.getCurrentPrice());
                    vo.setGiveIntegral(level.getGiveIntegral());
                    vo.setLevelDesc(level.getLevelDesc());
                    vo.setWechatPlanConfigured(true);
                    return vo;
                }).toList();
    }
}




