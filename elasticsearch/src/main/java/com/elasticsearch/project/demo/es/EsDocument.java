package com.elasticsearch.project.demo.es;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsDocument {

    String index() default "";

    String type() default "";
}
