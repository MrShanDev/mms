package com.sxpcwlkj.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * DemoModeException
 * @Author 西决
 */
@Getter
public class DemoModeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    // 添加错误代码字段
    private final int errorCode;

    public DemoModeException(String message) {
        this(403100, message);
    }

    public DemoModeException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
