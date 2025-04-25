package com.sxpcwlkj.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sxpcwlkj.common.captcha.SensitivitySerializer;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义数据脱敏注解
 * @author 10419
 */
// 作用在字段上
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitivitySerializer.class) // 该注解使用序列化的方式
public @interface SensitivityEncrypt {

    /**
     * 脱敏数据类型（必须指定类型）
     */
    SensitivityTypeEnum type();

    /**
     * 前面有多少不需要脱敏的长度
     */
    int prefixNoMaskLen() default 1;

    /**
     * 后面有多少不需要脱敏的长度
     */
    int suffixNoMaskLen() default 1;

    /**
     * 用什么符号进行打码
     */
    String symbol() default "*";
}
