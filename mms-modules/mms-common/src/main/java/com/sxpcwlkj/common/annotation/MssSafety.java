package com.sxpcwlkj.common.annotation;

import com.sxpcwlkj.common.enums.SafetyTypeEnum;

import java.lang.annotation.*;

/**
 * @author XIjue
 * MMS安全注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface MssSafety {

    /**
     * 数据重复提交验证，默认开启
     */
    boolean isRepetition() default true;

    /**
     * 接口请求参数是否需要解密
     */
    boolean decryptRequest() default false;

    /**
     * 接口响应参数是否需要加密
     */
    boolean encryptResponse() default false;

    /**
     * 加密类型
     */
    SafetyTypeEnum encryptType() default SafetyTypeEnum.AES;
}
