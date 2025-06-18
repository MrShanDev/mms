package com.sxpcwlkj.common.exception;

import java.io.Serial;

/**
 * @author xijue
 */
public class LoginException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public LoginException(String message) {
        super(message);
    }

}
