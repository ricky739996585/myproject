package com.elasticsearch.project.demo.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.elasticsearch.project.demo.dto.request.UserEsDTO;
import com.elasticsearch.project.demo.es.ElasticBaseRepository;
import com.elasticsearch.project.demo.es.ElasticEntityWrapper;
import com.elasticsearch.project.demo.pojo.User;
import com.elasticsearch.project.demo.service.UserEsService;
import com.ricky.common.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author: ricky
 * @Date: 2019/7/1 15:04
 */
@Service
public class UserEsServiceImpl extends ElasticBaseRepository<User> implements UserEsService {
    @Override
    public Boolean insert(UserEsDTO userEsDTO) {
        User user = new User();
        BeanUtils.copyProperties(userEsDTO,user);
        return this.insert(userEsDTO.getId().toString(),user);
    }

    @Override
    public Boolean update(UserEsDTO userEsDTO) {
        User user = new User();
        BeanUtils.copyProperties(userEsDTO,user);
        return this.updateById(userEsDTO.getId().toString(),user);
    }

    @Override
    public Boolean delete(String id) {
        return this.deleteById(id);
    }

    @Override
    public PageUtils query(String username, String phone,Integer currPage,Integer pageSize) {
        ElasticEntityWrapper<User> wrapper = new ElasticEntityWrapper<>();
        wrapper.eq(!StringUtils.isEmpty(username),"username",username);
        wrapper.eq(!StringUtils.isEmpty(phone),"phone",phone);
        return this.selectPage(wrapper,new Page<>(currPage,pageSize));
    }
}
