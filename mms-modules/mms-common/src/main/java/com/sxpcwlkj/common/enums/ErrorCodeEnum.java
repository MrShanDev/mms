package com.sxpcwlkj.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 接口返回错误码枚举
 * @author shanpengnian
 */
public enum ErrorCodeEnum implements IEnum {

    /* 系统异常 */
    SYSTEM_RUNNING(100000, "系统运行异常"),
    SYSTEM_ERROR(101000, "系统未知异常"),
    PARAM_EXIST_EXCEPTION(101001, "参数异常"),

    /* token相关 */
    TOKEN_IS_EMPTY(102001, "该请求没有携带token！请先获取token"),
    TOKEN_IS_INVALID(102002, "token失效，请重新登录！"),
    TOKEN_IS_ERROR(102003, "非法token！请重新登录！"),
    /** 业务体 {@code code}，与 HTTP 401 语义一致，便于前后端统一 */
    USER_NOT_LOGIN(401, "请先登录"),

    /** 与 HTTP 403 一致：权限不足 / 禁止访问 */
    FORBIDDEN(403, "没有访问权限，请联系管理员授权"),

    /** 与 HTTP 500 一致：服务端未分类错误或通用失败 */
    INTERNAL_ERROR(500, "系统内部错误，请稍后重试"),


    ;



    @Getter
    private Integer key;
    private String value;

    ErrorCodeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

