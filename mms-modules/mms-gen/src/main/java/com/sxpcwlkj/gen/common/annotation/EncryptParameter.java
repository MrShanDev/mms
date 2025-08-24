package com.sxpcwlkj.gen.common.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 参数加解密注解
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface EncryptParameter {
}
