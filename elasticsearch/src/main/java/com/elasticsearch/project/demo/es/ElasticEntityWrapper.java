package com.elasticsearch.project.demo.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: ELK 条件工具类
 * @Author: ricky
 * @Date: 2019/4/25 16:14
 */
@Slf4j
public class ElasticEntityWrapper<T> implements Serializable {


    protected BoolQueryBuilder boolQueryBuilder;

    private Boolean sort = Boolean.FALSE;

    protected SortBuilder sortBuilder;

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public SortBuilder getSortBuilder() {
        return sortBuilder;
    }


    public BoolQueryBuilder getBoolQueryBuilder() {
        return boolQueryBuilder;
    }


    public ElasticEntityWrapper() {
        init();
    }


    private void init(){
        // 初始化复杂查询构造器
        this.boolQueryBuilder = QueryBuilders.boolQuery();
    }

    public ElasticEntityWrapper<T> eq(String column, Object params) {
        return eq(true, column, params);
    }

    public ElasticEntityWrapper<T> eq(boolean condition, String column, Object params) {
        if (condition){
            TermQueryBuilder whereAndCql =  QueryBuilders.termQuery(column,params);
            this.boolQueryBuilder.must(whereAndCql);
        }
        return this;
    }

    /**
     *  前缀模糊搜索（例子：127.0.*）
     */
    public ElasticEntityWrapper<T> prefix(boolean condition, String column, String prefix) {
        if (condition){
            PrefixQueryBuilder whereAndCql = QueryBuilders.prefixQuery(column,prefix);
            this.boolQueryBuilder.must(whereAndCql);
        }
        return this;
    }

    /**
     *  通配符搜索（例子：*老师）
     */
    public ElasticEntityWrapper<T> wilcard(boolean condition, String column, String prefix) {
        if (condition){
            WildcardQueryBuilder whereAndCql = QueryBuilders.wildcardQuery(column,prefix);
            this.boolQueryBuilder.must(whereAndCql);
        }
        return this;
    }

    /**
     * <p>
     * 添加OR条件
     * </p>
     */
    public ElasticEntityWrapper<T> or(String column, Object params) {
        TermQueryBuilder whereOrCql =  QueryBuilders.termQuery(column,params);
        this.boolQueryBuilder.should(whereOrCql);
        return this;
    }

    /**
     * <p>
     * 使用OR换行，并添加一个带()的新的条件
     * </p>
     * <p>
     * eg: ew.where("name='zhangsan'").and("id=11").orNew("statu=1"); 输出： WHERE
     * (name='zhangsan' AND id=11) OR (statu=1)
     * </p>
     *
     */
/*    public EntityWrapper<T> orNew(String column, Object params) {
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder whereOrCql =  QueryBuilders.termQuery(column,params);
        orBoolQueryBuilder.must(whereOrCql);
        this.boolQueryBuilder.should(orBoolQueryBuilder);
        return this;
    }*/


    /**
     * 等同于SQL的column>params"表达式
     * @param column    属性名
     * @param params    参数值
     */
    public ElasticEntityWrapper<T> gt(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.gt(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * 等同于SQL的column>=params"表达式
     * @param column    属性名
     * @param params    参数值
     */
    public ElasticEntityWrapper<T> ge(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.gte(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * 等同于SQL的column<params"表达式
     * @param column    属性名
     * @param params    参数值
     */
    public ElasticEntityWrapper<T> lt(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.lt(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * 等同于SQL的column<=params"表达式
     * @param column    属性名
     * @param params    参数值
     */
    public ElasticEntityWrapper<T> le(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.lte(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }


    public ElasticEntityWrapper<T> like(String column, String value) {
        return this.like(true,column,value);
    }

    public ElasticEntityWrapper<T> like(boolean condition,String column, String value) {
        if (condition){
            // 根据 词 不分隔模糊匹配
            // MatchPhraseQueryBuilder whereLikeCql= QueryBuilders.matchPhraseQuery(column,value);  //只能精确
             MatchQueryBuilder whereLikeCql = QueryBuilders.matchQuery(column,value);  //只能精确
//            MatchPhrasePrefixQueryBuilder whereLikeCql = QueryBuilders.matchPhrasePrefixQuery(column,value);
//            MultiMatchQueryBuilder whereLikeCql = QueryBuilders.multiMatchQuery(column, value);
            this.boolQueryBuilder.must(whereLikeCql);
        }

        return this;
    }


    public ElasticEntityWrapper<T> in(String column, Collection<?> value) {
        TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery(column,value);
        this.boolQueryBuilder.must(termQueryBuilder);
        return this;
    }

    public ElasticEntityWrapper<T> ids(String... id){
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        idsQueryBuilder.addIds(id);
        this.boolQueryBuilder.must(idsQueryBuilder);
        return this;
    }

    public ElasticEntityWrapper<T> orderBy(String columns, Boolean isAsc) {
        this.sort = Boolean.TRUE;
        this.sortBuilder = SortBuilders.fieldSort(columns);
        this.sortBuilder.order(isAsc? SortOrder.ASC : SortOrder.DESC);
        return this;
    }

}
