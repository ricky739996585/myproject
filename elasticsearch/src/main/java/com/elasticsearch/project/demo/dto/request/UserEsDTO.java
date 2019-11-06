package com.elasticsearch.project.demo.dto.request;

import lombok.Data;

/**
 * @Author: ricky
 * @Date: 2019/7/1 14:56
 */
@Data
public class UserEsDTO {
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
}
