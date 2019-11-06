package com.ricky.lottery.controller;

import com.ricky.common.utils.PageUtils;
import com.ricky.common.utils.R;
import com.ricky.lottery.entity.RewardEntity;
import com.ricky.lottery.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;


/**
 * 奖品详情表
 */
@RestController
@RequestMapping("sys/reward")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(Integer index, String name, String icon, BigDecimal probability, Integer bigPrice, @RequestParam(name = "page", defaultValue = "10") Integer page,
                  @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        PageUtils pageResult = rewardService.queryPage(index, name, icon, probability, bigPrice, page, limit);
        return R.ok().put("page", pageResult);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RewardEntity reward = rewardService.selectById(id);

        return R.ok().put("reward", reward);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody RewardEntity reward) {
        rewardService.insert(reward);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody RewardEntity reward) {
        rewardService.updateAllColumnById(reward);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        rewardService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

}
