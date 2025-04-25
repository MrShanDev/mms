package com.sxpcwlkj.common.code.entity;

import lombok.Data;

/**
 * @author xijue
 */
@Data
public class ConfigEntity {
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置类型
     */
    private Integer configType;
    /**
     * 配置值
     */
    private String configValue;
}
