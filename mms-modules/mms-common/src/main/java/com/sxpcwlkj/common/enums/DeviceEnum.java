package com.sxpcwlkj.common.enums;

import com.sxpcwlkj.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 设备类型
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeviceEnum {

    /**
     * 后端
     */
    ADMIN("ADMIN"),
    /**
     * 移动端
     */
    MOBILE("MOBILE"),
    /**
     * PC端
     */
    PC("PC");

    private final String type;

    public static DeviceEnum find(String databaseProductName) {
        if (StringUtil.isBlank(databaseProductName)) {
            return null;
        }
        for (DeviceEnum type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
