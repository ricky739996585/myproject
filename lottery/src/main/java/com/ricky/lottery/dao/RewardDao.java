package com.ricky.lottery.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ricky.lottery.entity.RewardEntity;
import org.springframework.stereotype.Component;

/**
 * 奖品详情表
 */
@Component
public interface RewardDao extends BaseMapper<RewardEntity> {

}
