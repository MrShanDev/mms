package com.sxpcwlkj.common.exception;

import java.io.Serial;
/**
 * @author xijue
 */
public class TenantException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public TenantException(Throwable e) {
        super(e.getMessage(), e);
    }

    public TenantException(String message) {
        super(message);
    }

    public TenantException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
