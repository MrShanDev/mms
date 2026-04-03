package com.sxpcwlkj.system.entity.bo;

import lombok.Data;

/**
 * 插件市场：单条插件配置项（保存用）。
 */
@Data
public class PluginMarketSysConfigItemBo {

    private String keySuffix;
    private String configName;
    private String configValue;
}
