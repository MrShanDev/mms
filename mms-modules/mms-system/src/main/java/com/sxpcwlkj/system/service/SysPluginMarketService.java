package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.vo.PluginMarketCardVo;

import java.util.List;

public interface SysPluginMarketService {

    List<PluginMarketCardVo> listMarketCards();

    /**
     * 删除 {@code sys_plugin_version} 中该插件的登记，并删除 {@code sys_plugins} 上架行（若存在），全量重载宿主。
     * 不删除磁盘目录；需要删文件时请使用宿主 {@code /system/pluginHost/uninstall}。
     */
    void removeCatalogEntry(String pluginId);
}
