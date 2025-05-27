package com.sxpcwlkj.doc.enums;

/**
 * 支付类型
 *
 * @author xijue
 * @Doc mmsadmin.cn
 */
public enum PayType {
    WX_JSAPI("wx_jsapi"),
    WX_NATIVE("wx_native");

    private final String value;

    PayType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
