package com.sxpcwlkj.common.exception;

import com.sxpcwlkj.common.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 自定义异常
 * @author xijue
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MmsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 细节问题
     */
    private String detailMessage;

    public MmsException() {
    }

    public MmsException(String message) {
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public MmsException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public MmsException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = msg;
    }

}
