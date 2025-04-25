package com.sxpcwlkj.common.enums;

import com.sxpcwlkj.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 加密类型
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Getter
@AllArgsConstructor
public enum SafetyTypeEnum {

    /**
     * 对称加密
     */
    AES("AES"),
    /**
     * 非对称加密
     */
    RSA("RSA");

    private final String type;

    public static SafetyTypeEnum find(String databaseProductName) {
        if (StringUtil.isBlank(databaseProductName)) {
            return null;
        }
        for (SafetyTypeEnum type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
