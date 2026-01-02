package com.sxpcwlkj.log.annotation;

import com.sxpcwlkj.log.enums.LogSavePolicy;
import com.sxpcwlkj.log.enums.OperationType;

import java.lang.annotation.*;

/**
 * MMS 操作日志注解
 *
 * @author mmsAdmin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MmsLog {

    /**
     * 模块名称 (留空则自动识别)
     */
    String module() default "";

    /**
     * 操作类型 (默认 AUTO 自动识别)
     */
    OperationType operType() default OperationType.OTHER;

    /**
     * 操作描述 (留空则自动识别)
     */
    String description() default "";

    /**
     * 是否保存请求参数
     */
    boolean saveRequestData() default true;

    /**
     * 是否保存响应数据
     */
    boolean saveResponseData() default false;

    /**
     * 日志保存策略(默认保存到数据库)
     */
    LogSavePolicy savePolicy() default LogSavePolicy.DATABASE;

    /**
     * 是否记录操作前的数据(用于对比修改前后)
     */
    boolean saveBeforeData() default false;

    /**
     * 是否自动识别 (module、operType、description 都为空时自动为 true)
     */
    boolean auto() default true;

    /**
     * 排除指定的请求参数(敏感字段,如密码)
     */
    String[] excludeParams() default {"password", "oldPassword", "newPassword", "confirmPassword"};
}