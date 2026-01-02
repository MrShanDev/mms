package com.sxpcwlkj.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author mmsAdmin
 */
@Getter
@AllArgsConstructor
public enum OperationType {

    /**
     * 其它
     */
    OTHER("其它", 0),

    /**
     * 新增
     */
    INSERT("新增", 1),

    /**
     * 修改
     */
    UPDATE("修改", 2),

    /**
     * 删除
     */
    DELETE("删除", 3),

    /**
     * 查询
     */
    SELECT("查询", 4),

    /**
     * 导出
     */
    EXPORT("导出", 5),

    /**
     * 导入
     */
    IMPORT("导入", 6),

    /**
     * 登录
     */
    LOGIN("登录", 7),

    /**
     * 退出
     */
    LOGOUT("退出", 8),

    /**
     * 授权
     */
    GRANT("授权", 9),

    /**
     * 清空
     */
    CLEAN("清空", 10);

    /**
     * 操作名称
     */
    private final String name;

    /**
     * 操作代码
     */
    private final Integer code;

    /**
     * 根据代码获取操作类型
     */
    public static OperationType getByCode(Integer code) {
        for (OperationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return OTHER;
    }
}
