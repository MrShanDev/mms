package com.sxpcwlkj.common.annotation;


import java.lang.annotation.*;
/**
 * 数据权限组 属性成员
 *
 * @name: DataPermissionGroup
 * @author: mmsAdmin
 * @date: 2022/12/01
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataColumn {
    /**
     * 占位符关键字 部门名称
     */
    String[] key() default "deptName";

    /**
     * 占位符替换值 部门ID
     */
    String[] value() default "dept_id";
}
