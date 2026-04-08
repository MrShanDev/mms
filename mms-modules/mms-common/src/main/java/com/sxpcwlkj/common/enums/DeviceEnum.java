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
     * 移动端（默认与 mms-admin 同进程；历史独立进程曾为 mms-open-api.jar）
     */
    MOBILE("MOBILE"),
    /**
     * PC 端：mms-unix 等场景下的 {@code store_member} 会员（与文档站 {@link #DOC} 区分）
     */
    PC("PC"),
    /**
     * MMS-DOC 文档站付费/扫码用户（Redis {@code doc:member:}）；登录 id 与 {@code store_member.id} 对齐，会话体为 Map/VO 非 ORM 实体
     */
    DOC("DOC");

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
