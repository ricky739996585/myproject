package com.ricky.common.utils;


import java.util.UUID;

/**
 * @Description: UUID工具类
 * @Author: ricky
 * @Date: 2019/7/1 11:09
 */
public class UuidUtils {

    //生成统一的uuid
    public static String createUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
