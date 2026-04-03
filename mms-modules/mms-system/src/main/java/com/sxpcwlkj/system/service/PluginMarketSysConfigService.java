package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigSaveBo;
import com.sxpcwlkj.system.entity.vo.PluginMarketSysConfigVo;

import java.util.List;

/**
 * 插件市场：插件专属 sys_config（键前缀 mms.plugin.{pluginId}.）。
 */
public interface PluginMarketSysConfigService {

    List<PluginMarketSysConfigVo> listForPlugin(String pluginId, String tenantId);

    void saveForPlugin(PluginMarketSysConfigSaveBo body, String tenantId);
}
