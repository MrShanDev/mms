package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件上架安装时自动写入 {@code sys_function} 的声明（与 {@code sysConfig} 种子数据同属安装编排）。
 * <p>各行 {@link PluginMenuItemDef#getId()} 须在库内唯一；卸载插件（且版本记录已清空）时由宿主按 {@code remark = "plugin:" + pluginId} 删除。</p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginMenuBootstrapDef {

    private List<PluginMenuItemDef> items = new ArrayList<>();
}
