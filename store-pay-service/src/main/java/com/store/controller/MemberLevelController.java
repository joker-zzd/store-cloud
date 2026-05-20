package com.store.controller;

import com.store.common.resultvo.ResultVO;
import com.store.domain.vo.WechatSubscriptionLevelVO;
import com.store.service.MemberLevelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员套餐相关接口。
 */
@RestController
@RequestMapping("/api/member/level")
public class MemberLevelController {
    private final MemberLevelService memberLevelService;

    public MemberLevelController(MemberLevelService memberLevelService) {
        this.memberLevelService = memberLevelService;
    }

    @GetMapping("/wechat/subscription/list")
    public ResultVO<List<WechatSubscriptionLevelVO>> listWechatSubscriptionLevels() {
        return ResultVO.success(memberLevelService.listWechatSubscriptionLevels());
    }
}
