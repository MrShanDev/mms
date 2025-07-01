package com.sxpcwlkj.common.exception;

import com.sxpcwlkj.common.enums.ErrorCodeEnum;
import lombok.Setter;

import java.io.Serial;

/**
 * 参数异常
 * @author mmsAdmin
 */
@Setter
public class ParamsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    public ParamsException() {
    }

    public ParamsException(String msg) {
        this.message = msg;
    }

    public ParamsException(ErrorCodeEnum e) {
        this.message = e.getValue();
    }

    @Override
    public String getMessage() {
        return message;
    }

}

