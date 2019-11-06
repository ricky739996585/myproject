package com.ricky.lottery.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ricky.common.utils.PageUtils;
import com.ricky.common.utils.R;
import com.ricky.lottery.dao.RewardDao;
import com.ricky.lottery.dto.RewardInfoDTO;
import com.ricky.lottery.entity.RewardEntity;
import com.ricky.lottery.service.RewardService;
import com.ricky.lottery.utils.AliasMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("rewardService")
public class RewardServiceImpl extends ServiceImpl<RewardDao, RewardEntity> implements RewardService {

    @Override
    public PageUtils queryPage(Integer index, String name, String icon, BigDecimal probability, Integer bigPrice, Integer page, Integer limit) {
        Page<RewardEntity> pageResult = this.selectPage(
                new Page<>(page, limit),
                new EntityWrapper<RewardEntity>()
                        .eq(null != index, "index", index)
                        .eq(StringUtils.isNotBlank(name), "name", name)
                        .eq(StringUtils.isNotBlank(icon), "icon", icon)
                        .eq(null != probability, "probability", probability)
                        .eq(null != bigPrice, "big_price", bigPrice)
        );
        return new PageUtils(pageResult);
    }

    @Override
    public R getRewardList() {
        List<RewardEntity> list = this.selectList(
                new EntityWrapper<RewardEntity>()
                        .eq("status", 0).orderBy("order_no"));
        List<RewardInfoDTO> dtoList = new ArrayList<>(list.size());
        list.forEach(c ->{
            RewardInfoDTO dto = new RewardInfoDTO();
            BeanUtils.copyProperties(c,dto);
            dtoList.add(dto);
        });

        return R.ok(dtoList);
    }

    @Override
    public R lottery() {
        List<RewardEntity> list = this.selectList(
                new EntityWrapper<RewardEntity>()
                        .eq("status", 0).orderBy("order_no"));
        List<Double> prob = new ArrayList<>(list.size());
        list.forEach(c ->{
            prob.add(c.getProbability().doubleValue());
        });
        AliasMethod aliasMethod = new AliasMethod(prob);
        boolean result = true;
        RewardEntity rewardEntity = null;
        while(result){
            //结果下标
            int next = aliasMethod.next();
            //抽奖结果奖品
            rewardEntity = list.get(next);

            //是否是大奖，如果不是大奖，则结束循环
            if(rewardEntity.getBigPrice() == 0)
                result = false;

        }
        //TODO 结算礼品，记录数据


        return R.ok(rewardEntity.getName());
    }

    public static void main(String[] args) {

        String join = Joiner.on("|").skipNulls().join(Lists.newArrayList("name ", " dddd", "sx e x"));
        System.out.println(join);
        String str = "name | dddd|sx e x";
        List<String> list = Splitter.on("|").splitToList(str);
        Map<String, String> map = Maps.newHashMap();
        map.put("age","18");
        map.put("name","ricky");
        map.put("sex","man");
        String join1 = Joiner.on(",").withKeyValueSeparator(":").join(map);
        System.out.println(join1);

    }

}
