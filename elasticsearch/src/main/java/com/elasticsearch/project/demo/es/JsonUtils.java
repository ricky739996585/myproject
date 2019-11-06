package com.elasticsearch.project.demo.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class JsonUtils {

    public static <T> String toJsonStr(T t){
        return JSON.toJSONString(t, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty);
    }
}
