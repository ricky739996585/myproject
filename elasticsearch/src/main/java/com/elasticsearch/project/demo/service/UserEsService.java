package com.elasticsearch.project.demo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.elasticsearch.project.demo.dto.request.UserEsDTO;
import com.elasticsearch.project.demo.es.ElasticEntityWrapper;
import com.ricky.common.utils.PageUtils;

import java.util.Map;

/**
 * @Author: ricky
 * @Date: 2019/7/1 14:36
 */
public interface UserEsService {

    Boolean insert(UserEsDTO userEsDTO);

    Boolean update(UserEsDTO userEsDTO);

    Boolean updateFieldById(String id, Map<String,Object> map);

    Boolean delete(String id);

    PageUtils query(String username,String phone,Integer currPage,Integer pageSize);
}
