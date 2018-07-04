package com.along101.microservice.starter.cat.annotation;

import java.lang.annotation.*;

/**
 * Created by zhangyicong on 2017/4/25.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CatTransaction {
    String type() default "";
    String name() default "";
    boolean parameters() default false;
    boolean returnValue() default false;
}
