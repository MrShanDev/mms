package com.sxpcwlkj.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限组
 *
 * @name: DataPermissionGroup
 * @author: 西决
 * @date: 2022/12/01
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermissionGroup {

    DataColumn[] value();

}
