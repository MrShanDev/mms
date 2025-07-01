package com.sxpcwlkj.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解
 * @author mmsAdmin
 *
 */

//注解作用在方法上
@Target(ElementType.METHOD)
// 运行时生效
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    // 每秒允许的请求数
    double permitsPerSecond();
}
