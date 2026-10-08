package com.sxpcwlkj.common.annotation;
import java.lang.annotation.*;
/** Operations affecting shared platform resources require the platform tenant administrator. */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlatformAdmin { }
