package com.ricky.lottery.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 奖品详情表
 */
@Data
@TableName("reward")
public class RewardInfoDTO {

    /**
     * 奖品名称
     */
    private String name;
    /**
     * 图标URL
     */
    private String icon;

}
