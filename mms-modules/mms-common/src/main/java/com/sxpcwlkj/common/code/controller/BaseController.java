package com.sxpcwlkj.common.code.controller;


import com.sxpcwlkj.common.utils.R;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName BaseController
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2022/12/4 0:59
 */
public class BaseController extends R<Object> {

    protected final static String DATE_FORMATE = "yyyy-MM-dd";

    // 下面是判断null的操作

    public boolean isEmpty(String str) {
        return (null == str) || (str.trim().length() <= 0);
    }

    public boolean isEmpty(Character cha) {
        return (null == cha) || cha.equals(' ');
    }

    public boolean isEmpty(Object obj) {
        return (null == obj);
    }

    public boolean isEmpty(Object[] objs) {
        return (null == objs) || (objs.length <= 0);
    }

    public boolean isEmpty(Collection<?> obj) {
        return (null == obj) || obj.isEmpty();
    }

    public boolean isEmpty(Set<?> set) {
        return (null == set) || set.isEmpty();
    }

    public boolean isEmpty(Serializable obj) {
        return null == obj;
    }

    public boolean isEmpty(Map<?, ?> map) {
        return (null == map) || map.isEmpty();
    }

    /**
     *
     * 获得map
     * @return
     */
    public Map<String,Object> getMap(){
        return new HashMap<String,Object>();
    }


}
