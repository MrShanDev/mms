package com.sxpcwlkj.common.annotation;

import java.lang.annotation.*;

/**
 * 登陆验证自定义注解
 *
 * @name: AuthLoginAnnotation
 * @author: 西决
 * @date: 2022/11/30
 **/
@Documented //文档生成时，该注解将被包含在javadoc中，可去掉
@Target(ElementType.METHOD)//目标是方法
@Retention(RetentionPolicy.RUNTIME) //注解会在class中存在，运行时可通过反射获取
@Inherited
public @interface AuthLoginAnnotation {
    /**
     * 检查是否已登录（注解的参数）
     *
     * @return true-检查；默认true 进行登录验证
     */
    boolean login() default true;

    /**
     * 验证对象
     * com.sxpcwlkj.common.enumeration.AnnotationEnum
     * @return
     */
    String object() default "admin";

    /**
     * 是否验证当前对象 访问权限
     * 默认 true  进行验证
     *
     * @return
     */
    boolean authority() default true;

    /**
     * 权限标识
     * 默认 null  拦截所有
     *
     * @return
     */
    String authorityCode() default "";

    /**
     * app是否验证key
     * 默认不验证
     *
     * @return
     */
    boolean key() default false;
}
