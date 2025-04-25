package com.sxpcwlkj.common.annotation;


import com.sxpcwlkj.common.enums.PrintTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限组 属性成员
 *
 * @name: DataPermissionGroup
 * @author: 西决
 * @date: 2022/12/01
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintColumn {
    /**
     * 名称
     */
    String title() default "";
    /**
     * 类型
     */
    PrintTypeEnum type() default PrintTypeEnum.TEXT;
    /**
     * 表头宽度
     */
    String width() default "100";

    /**
     * 表头高度
     */
    String height() default "100";
    /**
     * 对齐方式
     */
    String align() default "center";
    /**
     * 是否显示
     */
    boolean show() default true;


}
