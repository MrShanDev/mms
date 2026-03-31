package com.sxpcwlkj.system.entity.bo;

import lombok.Data;

/**
 * 插件市场：按 pluginId 移除库表登记（不删磁盘）。
 */
@Data
public class PluginMarketRemoveBo {

    private String pluginId;
}
