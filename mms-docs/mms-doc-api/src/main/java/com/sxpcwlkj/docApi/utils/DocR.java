package com.sxpcwlkj.docApi.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 返回结果集
 *
 * @author javadog
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocR<T> {

    /**
     * 状态码
     */

    private String errno;


    /**
     * 返回信息
     */

    private String errmsg;

    /**
     * 数据
     */

    private T body;
    /**
     * 时间
     */
    private String host_time;

    /**
     * 全参数方法
     * @param errno 状态
     * @param body 数据
     * @return 返回结果
     * @param <T> 泛型
     */
    public static <T> DocR<T> response(String errno, String errmsg, T body) {
        DocR<T> ajaxResult = new DocR<>();
        ajaxResult.setErrno(errno);
        ajaxResult.setErrmsg(errmsg);
        ajaxResult.setBody(body);
        ajaxResult.setHost_time(System.currentTimeMillis()+"");
        return ajaxResult;
    }

    /**
     * 成功
     * @param body 数据
     * @return 返回结果
     * @param <T> 泛型
     */
    public static <T> DocR<T> success(String errmsg, T body) {
        DocR<T> ajaxResult = new DocR<>();
        ajaxResult.setErrno("00000");
        ajaxResult.setErrmsg(errmsg);
        ajaxResult.setBody(body);
        ajaxResult.setHost_time(System.currentTimeMillis()+"");
        return ajaxResult;
    }

    /**
     * 成功 + OK
     * @param body 数据
     * @return 返回结果
     * @param <T> 泛型
     */
    public static <T> DocR<T> ok( T body) {
        DocR<T> ajaxResult = new DocR<>();
        ajaxResult.setErrno("00000");
        ajaxResult.setErrmsg("OK");
        ajaxResult.setBody(body);
        ajaxResult.setHost_time(System.currentTimeMillis()+"");
        return ajaxResult;
    }

    public static <T> DocR<T> error(String errno, String errmsg) {
        DocR<T> ajaxResult = new DocR<>();
        ajaxResult.setErrno(errno);
        ajaxResult.setErrmsg(errmsg);
        ajaxResult.setHost_time(System.currentTimeMillis()+"");
        return ajaxResult;
    }




}


