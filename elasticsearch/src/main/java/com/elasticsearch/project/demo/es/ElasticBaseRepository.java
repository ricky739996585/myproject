package com.elasticsearch.project.demo.es;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.ricky.common.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: ELK 实体公用方法
 * @Author: ricky
 * @Date: 2019/4/25 16:15
 */
@Slf4j
public class ElasticBaseRepository<T> implements BaseRepository<T>{

    @Autowired
    private RestHighLevelClient rhlClient;

    /**
     * 在不分页的情况下，Elasticsearch 只能查 10000 条数据
     */
    protected static final Integer ELK_MAX_SIZE  = 10000;


    @Override
    public Boolean insert(String id,T t) {
        IndexRequest indexRequest = new IndexRequest(this.getIndex(),this.getType());
        indexRequest.id(id);
        indexRequest.source(JsonUtils.toJsonStr(t),XContentType.JSON);
        //设置超时：等待主分片变得可用的时间
        indexRequest.timeout(TimeValue.timeValueSeconds(1));//TimeValue方式
        IndexResponse indexResponse = null;
        try {
            indexResponse = rhlClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("插入数据失败！");
        }
        return RestStatus.CREATED.getStatus() == indexResponse.status().getStatus();
    }

    @Override
    public T selectById(String id){
        GetResponse response = null;
        try {
            response = rhlClient.get(new GetRequest(this.getIndex(), this.getType(), id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据失败！");
        }
        return JSON.parseObject(response.getSourceAsString(),this.getEntityClass());
    }

    @Override
    public List<T> listByIds(String... ids){
        checkSize(ids.length);
        // 默认是分页返回，设置这个参数返回全部匹配值
        ElasticEntityWrapper<T> wrapper = new ElasticEntityWrapper<>();
        wrapper.ids(ids);
        SearchRequest searchRequest = getSearchRequest(wrapper.getBoolQueryBuilder(),0, ids.length);
        SearchResponse searchResponse = null;
        try {
            searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据失败！");
        }
        log.info("条件："+wrapper.getBoolQueryBuilder());
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
    }


    @Override
    public T selectOne(ElasticEntityWrapper<T> wrapper) {
        return getObject(this.selectList(wrapper));
    }

    @Override
    public List<T> selectList(ElasticEntityWrapper<T> wrapper){
        SearchRequest searchRequest = getSearchRequest(wrapper.getBoolQueryBuilder(),0,ELK_MAX_SIZE,wrapper.sortBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据失败！");
        }
        log.info("条件："+wrapper.getBoolQueryBuilder());
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
    }

    @Override
    public PageUtils selectPage(ElasticEntityWrapper<T> wrapper, Page<T> page){
        checkSize(page.getSize());
        int from = (page.getCurrent() - 1) * page.getSize();
        int size = page.getSize();
        SearchRequest searchRequest = getSearchRequest(wrapper.getBoolQueryBuilder(),from,size,wrapper.sortBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据失败！");
        }
        log.info("条件："+searchRequest);
        log.info("响应："+searchResponse);
        return this.dataPage(searchResponse,page);
    }

    @Override
    public List<T> selectList(BoolQueryBuilder boolQueryBuilder){
        SearchRequest searchRequest = getSearchRequest(boolQueryBuilder,0,ELK_MAX_SIZE);
        SearchResponse searchResponse = null;
        try {
            searchResponse = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询数据失败！");
        }
        log.info("条件："+boolQueryBuilder);
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
    }

    protected PageUtils dataPage(SearchResponse searchResponse, Page<T> page){
        List<T> data = this.data(searchResponse);
        long totalCount = searchResponse.getHits().getTotalHits();
        return new PageUtils(data,(int)totalCount,page.getSize(),page.getCurrent());
    }



    //============================================ 删 ================================================

    @Override
     public Boolean deleteById(String id){
         if (StringUtils.isEmpty(id)){
             throw new IllegalArgumentException("Id 不能为空");
         }
         String index = this.getIndex();
         String type = this.getType();
         log.info("delete Elasticsearch data . index >>> [{}],type >>> [{}],id >>> [{}]",index,type,id);
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        deleteRequest.timeout(TimeValue.timeValueSeconds(2));
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = rhlClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("删除数据失败！");
        }
        return RestStatus.OK.getStatus() == deleteResponse.status().getStatus();
     }

    //============================================ 改 ================================================

    @Override
    public Boolean updateById(String id,T data){
        String index = this.getIndex();
        String type = this.getType();
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(id)){
            return false;
        }
        UpdateRequest updateRequest = new UpdateRequest(index, type, id);
        //设置修改内容
        updateRequest.doc(JsonUtils.toJsonStr(data),XContentType.JSON);
        //设置超时：等待主分片变得可用的时间
        updateRequest.timeout(TimeValue.timeValueSeconds(1));
        log.info("update Elasticsearch data . index >>> [{}],type >>> [{}],id >>> [{}],data >>>[{}]",index,type,id,data.toString());
        UpdateResponse updateResponse = null;
        try {
            updateResponse = rhlClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新数据失败！");
        }
        return RestStatus.OK.getStatus() == updateResponse.status().getStatus();
    }

    @Override
    public Boolean updateFieldById(String id, Map<String, Object> map) {
        String index = this.getIndex();
        String type = this.getType();
        if (StringUtils.isEmpty(map) || StringUtils.isEmpty(id)){
            return false;
        }
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                .doc(map);
        //设置超时：等待主分片变得可用的时间
        updateRequest.timeout(TimeValue.timeValueSeconds(1));
        UpdateResponse updateResponse = null;
        try {
            updateResponse = rhlClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新部分数据失败！");
        }
        return RestStatus.OK.getStatus() == updateResponse.status().getStatus();
    }

    // 获取 T 的类型
    protected Class<T> getEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String getIndex(){
        return this.getEsDocument().index();
    }


    protected String getType(){
        return this.getEsDocument().type();
    }

    private EsDocument getEsDocument(){
        EsDocument esDocument =  getEntityClass().getAnnotation(EsDocument.class);
        if (esDocument == null){
            throw new IllegalArgumentException("没有配置索引");
        }
        return esDocument;
    }

    private void checkSize(Integer size){
        if (size > ELK_MAX_SIZE){
            throw new IllegalArgumentException("长度过长");
        }
    }


    private List<T> data(SearchResponse searchResponse){
        List<T> data = new ArrayList<>();
        for (SearchHit searchHit:searchResponse.getHits().getHits()) {
            T t = JSON.parseObject(searchHit.getSourceAsString(),getEntityClass());
            data.add(t);
        }
        return data;
    }


    private static <E> E getObject(List<E> list) {
        if (!CollectionUtils.isEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                log.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        }
        return null;
    }

    protected SearchRequest getSearchRequest(SearchSourceBuilder searchSourceBuilder){
        SearchRequest searchRequest = new SearchRequest(this.getIndex());
        searchRequest.types(this.getType());
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    protected SearchRequest getSearchRequest(BoolQueryBuilder boolQueryBuilder,int from,int size){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(boolQueryBuilder);
        return this.getSearchRequest(searchSourceBuilder);
    }

    protected SearchRequest getSearchRequest(BoolQueryBuilder boolQueryBuilder,int from, int size, SortBuilder sortBuilder){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        if(null != sortBuilder){
            searchSourceBuilder.sort(sortBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        return this.getSearchRequest(searchSourceBuilder);
    }




}
