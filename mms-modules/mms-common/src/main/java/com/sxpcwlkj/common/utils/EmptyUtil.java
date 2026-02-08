package com.sxpcwlkj.common.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 空值判断工具类，统一空判断逻辑。
 */
public final class EmptyUtil {

    private EmptyUtil() {
    }

    public static boolean isEmpty(String str) {
        return (str == null) || (str.trim().length() <= 0);
    }

    public static boolean isEmpty(Character cha) {
        return (cha == null) || cha.equals(' ');
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(Object[] objs) {
        return (objs == null) || (objs.length <= 0);
    }

    public static boolean isEmpty(Collection<?> obj) {
        return (obj == null) || obj.isEmpty();
    }

    public static boolean isEmpty(Set<?> set) {
        return (set == null) || set.isEmpty();
    }

    public static boolean isEmpty(Serializable obj) {
        return obj == null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }
}
