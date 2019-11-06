package com.ricky.lottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.*;

/**
 * 奖品详情表
 */
@Data
@TableName("reward")
public class RewardEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 奖品ID
     */
    @TableId
    private Long id;
    /**
     * 奖品序号（从0开始）
     */
    private Integer orderNo;
    /**
     * 奖品名称
     */
    private String name;
    /**
     * 图标URL
     */
    private String icon;
    /**
     * 奖品数量
     */
    private Integer amount;
    /**
     * 命中率
     */
    private BigDecimal probability;
    /**
     * 是否大奖（0：不是，1：是）
     */
    private Integer bigPrice;
    /**
     * 状态（0：正常，-1：禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
