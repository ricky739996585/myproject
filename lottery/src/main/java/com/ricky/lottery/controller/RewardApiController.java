package com.ricky.lottery.controller;

import com.ricky.common.utils.R;
import com.ricky.lottery.entity.RewardEntity;
import com.ricky.lottery.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ricky
 * @Date: 2019/7/29 11:58
 */
@RestController
@RequestMapping("reward/api")
public class RewardApiController {
    @Autowired
    private RewardService rewardService;

    /**
     * 获取礼物列表
     */
    @GetMapping("/getRewardList")
    public R getRewardList() {
        return rewardService.getRewardList();
    }

    /**
     * 获取礼物列表
     */
    @GetMapping("/lottery")
    public R lottery() {
        return rewardService.lottery();
    }

}
