package com.sxpcwlkj.common.exception;

import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import lombok.Setter;

import java.io.Serial;

/**
 * 用户登陆信息过期异常
 * @author mmsAdmin
 */
@Setter
public class TokenExpireException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    public TokenExpireException() {
    }

    public TokenExpireException(String msg) {
        this.message = msg;
    }

    public TokenExpireException(ErrorCodeEnum e) {
        this.message = e.getValue();
    }

    @Override
    public String getMessage() {
        return message;
    }

}

