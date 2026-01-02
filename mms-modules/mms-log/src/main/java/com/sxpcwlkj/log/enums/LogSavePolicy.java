package com.sxpcwlkj.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志保存策略枚举
 *
 * @author mmsAdmin
 */
@Getter
@AllArgsConstructor
public enum LogSavePolicy {

    /**
     * 保存到数据库
     */
    DATABASE("数据库"),

    /**
     * 保存到本地文件
     */
    FILE("本地文件"),

    /**
     * 数据库和文件都保存
     */
    BOTH("数据库和文件");

    private final String description;
}
