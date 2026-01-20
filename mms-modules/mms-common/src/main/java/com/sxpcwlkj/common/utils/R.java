package com.sxpcwlkj.common.utils;

import com.sxpcwlkj.common.enums.HttpStatusEnum;
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
public class R<T> {

    /**
     * 状态码
     */

    private Integer code;

    /**
     * 状态信息
     */
    private Boolean status=false;

    /**
     * 返回信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
    /**
     * 是否加密
     */
    private Boolean isSecurity=false;

    /**
     * 判断是否成功
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    private static <T> R<T> response(Integer code, Boolean status, String message, T data) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(code);
        ajaxResult.setStatus(code==200);
        ajaxResult.setMsg(message);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    private static <T> R<T> response(Integer code, Boolean status, String message) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(code);
        ajaxResult.setStatus(code==200);
        ajaxResult.setMsg(message);
        return ajaxResult;
    }

    /**
     * 全参数方法
     * @param aBoolean 状态
     * @param data 数据
     * @return 返回结果
     * @param <T> 泛型
     */
    protected static <T> R<T> response(Boolean aBoolean, T data) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(aBoolean ? HttpStatusEnum.SUCCESS.getCode() : HttpStatusEnum.ERROR.getCode());
        ajaxResult.setStatus(aBoolean);
        ajaxResult.setMsg(aBoolean ? HttpStatusEnum.SUCCESS.getMessage() : HttpStatusEnum.ERROR.getMessage());
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success() {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }


    /**
     * 成功返回（枚举参数）
     *
     * @param httpResponseEnum 枚举参数
     * @param <T>              泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), true, httpResponseEnum.getMessage());
    }

    /**
     * 成功返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(Integer code, String message) {
        return response(code, true, message);
    }

    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(String message, T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(Integer code, String message, T data) {
        return response(code, true, message, data);
    }



    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> success(String message) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, null);
    }

    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail() {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     *
     * @param httpResponseEnum 枚举
     * @param <T>              泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), false, httpResponseEnum.getMessage());
    }

    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(Integer code, String message) {
        return response(code, false, message);
    }

    /**
     * 失败返回（返回信息+数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(String message, T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, data);
    }

    /**
     * 失败返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(Integer code, String message, T data) {
        return response(code, false, message, data);
    }

    /**
     * 失败返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), data);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link R <T>}
     */
    public static <T> R<T> fail(String message) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, null);
    }

    /**
     * 自定义返回（状态码+返回信息+数据）
     *
     * @param b       成功或者失败
     * @param message 返回信息
     * @param <T>     泛型
     * @return 返回结果
     */
    public static <T> R<T> ok(Boolean b, String message) {
        return response(b ? HttpStatusEnum.SUCCESS.getCode() : HttpStatusEnum.ERROR.getCode(), b, message);
    }

    /**
     * 自定义返回（状态码+返回信息+数据）
     *
     * @param b       成功或者失败
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return 返回结果
     */
    public static <T> R<T> ok(Boolean b, String message, T data) {
        return response(b ? HttpStatusEnum.SUCCESS.getCode() : HttpStatusEnum.ERROR.getCode(), b, message, data);
    }

    public static <T> R<T> security(Integer code, String message, T data) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(code);
        ajaxResult.setStatus(code==200);
        ajaxResult.setMsg(message);
        ajaxResult.setData(data);
        ajaxResult.setIsSecurity(true);
        return ajaxResult;
    }

    public static <T> R<T> okFail(String message) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(200);
        ajaxResult.setStatus(false);
        ajaxResult.setMsg(message);
        ajaxResult.setIsSecurity(false);
        return ajaxResult;
    }
    public static <T> R<T> okFail(String message, T data) {
        R<T> ajaxResult = new R<>();
        ajaxResult.setCode(200);
        ajaxResult.setStatus(true);
        ajaxResult.setMsg(message);
        ajaxResult.setData(data);
        ajaxResult.setIsSecurity(false);
        return ajaxResult;
    }

}

