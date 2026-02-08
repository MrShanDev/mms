package com.sxpcwlkj.common.code.controller;


import com.sxpcwlkj.common.utils.EmptyUtil;
import com.sxpcwlkj.common.utils.R;
import jakarta.servlet.http.HttpServletRequest;

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

    protected static final String DATE_FORMAT = "yyyy-MM-dd";
    @Deprecated
    protected static final String DATE_FORMATE = DATE_FORMAT;

    // 下面是判断null的操作

    public boolean isEmpty(String str) {
        return EmptyUtil.isEmpty(str);
    }

    public boolean isEmpty(Character cha) {
        return EmptyUtil.isEmpty(cha);
    }

    public boolean isEmpty(Object obj) {
        return EmptyUtil.isEmpty(obj);
    }

    public boolean isEmpty(Object[] objs) {
        return EmptyUtil.isEmpty(objs);
    }

    public boolean isEmpty(Collection<?> obj) {
        return EmptyUtil.isEmpty(obj);
    }

    public boolean isEmpty(Set<?> set) {
        return EmptyUtil.isEmpty(set);
    }

    public boolean isEmpty(Serializable obj) {
        return EmptyUtil.isEmpty(obj);
    }

    public boolean isEmpty(Map<?, ?> map) {
        return EmptyUtil.isEmpty(map);
    }

    /**
     *
     * 获得map
     * @return
     */
    public Map<String,Object> getMap(){
        return new HashMap<>();
    }


    public Map<String,String> getParameters(HttpServletRequest request){
        Map<String, String> params = new java.util.HashMap<>();
        // 获取请求参数
        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.put(paramName, paramValue);
        }
        return params;
    }

    public Map<String,String> getHeaders(HttpServletRequest request){
        Map<String, String> params = new java.util.HashMap<>();
        // 获取请求参数
        java.util.Enumeration<String> paramNames = request.getHeaderNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getHeader(paramName);
            params.put(paramName, paramValue);
        }
        return params;
    }

}
