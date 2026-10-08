package com.sxpcwlkj.common.annotation;
import java.lang.annotation.*;

/** Opt-in row permissions for a Mapper/table, including SELECT/UPDATE/DELETE. */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    String table();
    String userColumn() default "created_by";
    String deptColumn() default "";
}
