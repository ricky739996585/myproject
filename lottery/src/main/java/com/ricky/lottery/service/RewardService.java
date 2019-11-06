package com.ricky.lottery.service;

import com.baomidou.mybatisplus.service.IService;
import com.ricky.common.utils.PageUtils;
import com.ricky.common.utils.R;
import com.ricky.lottery.entity.RewardEntity;

import java.math.BigDecimal;

/**
 * 奖品详情表
 */
public interface RewardService extends IService<RewardEntity> {

    PageUtils queryPage(Integer index, String name, String icon, BigDecimal probability, Integer bigPrice, Integer page, Integer limit);

    R getRewardList();

    R lottery();

}

