package com.sxpcwlkj.common.enums;

import lombok.Getter;

/**
 * Http状态返回枚举
 *
 * @author javadog
 **/
@Getter
public enum WxCodeStatusEnum {
    /**
     * 系统警告消息
     */
    SUCCEED(0,"扫码成功"),
    /**
     * 待扫码
     */
    WAITING(1, "等待扫码中"),
    /**
     * 已扫码
     */
    SCANNED(2, "已扫码"),
    /**
     * 扫码失败
     */
    FAILING(3, "扫码失败");


    private final Integer value;
    private final String message;

    WxCodeStatusEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }
}
