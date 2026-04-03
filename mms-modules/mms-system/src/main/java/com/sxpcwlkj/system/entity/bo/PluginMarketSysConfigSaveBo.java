package com.sxpcwlkj.system.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * 插件市场：批量保存插件 sys_config。
 */
@Data
public class PluginMarketSysConfigSaveBo {

    private String pluginId;
    private List<PluginMarketSysConfigItemBo> items;
}
