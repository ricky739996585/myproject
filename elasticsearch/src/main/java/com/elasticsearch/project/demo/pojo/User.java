package com.elasticsearch.project.demo.pojo;

import com.elasticsearch.project.demo.es.EsDocument;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ricky
 * @Date: 2019/4/25 17:09
 */
@EsDocument(index = "project_user",type = "user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 创建时间
     */
    private Long createTime;


}
