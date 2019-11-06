package com.elasticsearch.project.demo.es;


import com.baomidou.mybatisplus.plugins.Page;
import com.ricky.common.utils.PageUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;
import java.util.Map;

/**
 * @Author: ricky
 * @Date: 2019/4/25 16:31
 */
public interface BaseRepository<T> {

    Boolean insert(String id, T t);

    /**
     * 如果查不到 ,返回 null
     * @param id id
     * @return T 对象
     */
    T selectById(String id);

    /**
     * 根据ID列表查询，不受长度限制
     * @param ids ID列表
     * @return T对象列表
     */
    List<T> listByIds(String... ids);

    /**
     * 查询一条数据
     * @param wrapper ElasticEntityWrapper 对象
     * @return T对象
     */
    T selectOne(ElasticEntityWrapper<T> wrapper);

    /**
     *  警告：此方法目前返回至多 10000 条数据
     * @param wrapper ElasticEntityWrapper 对象
     * @return T对象列表
     */
    List<T> selectList(ElasticEntityWrapper<T> wrapper);

    /**
     * 分页查询
     * @param wrapper ElasticEntityWrapper对象
     * @param page Page对象
     * @return PageUtils对象
     */
    PageUtils selectPage(ElasticEntityWrapper<T> wrapper, Page<T> page);

    /**
     *  警告：此方法目前返回至多 10000 条数据
     * @param boolQueryBuilder BoolQueryBuilder对象
     * @return T对象列表
     */
    List<T> selectList(BoolQueryBuilder boolQueryBuilder);


    /**
     * 删
     * @param id ID
     */
    Boolean deleteById(String id);

    /**
     * 改
     * 警告：改方法采用全量替换的方式，不像 MySQL 可以部分字段更新
     * @param id ID
     * @param data 实体全部数据
     */
    Boolean updateById(String id, T data);

    /**
     * 修改部分字段
     * @param id ID
     * @param map 实体全部数据
     */
    Boolean updateFieldById(String id, Map<String, Object> map);
}
