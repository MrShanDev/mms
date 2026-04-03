package com.sxpcwlkj.system.entity.vo;

import lombok.Data;

/**
 * 插件市场：单条配置展示。
 */
@Data
public class PluginMarketSysConfigVo {

    private String fullConfigKey;
    private String keySuffix;
    private String configName;
    private String configValue;
}
