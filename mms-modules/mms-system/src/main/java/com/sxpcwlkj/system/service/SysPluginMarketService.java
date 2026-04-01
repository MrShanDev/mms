package com.sxpcwlkj.system.service;

import com.sxpcwlkj.system.entity.vo.PluginMarketCardVo;

import java.io.IOException;
import java.util.List;

public interface SysPluginMarketService {

    List<PluginMarketCardVo> listMarketCards();

    /**
     * 删除 {@code sys_plugin_version} 中该插件的登记，并删除 {@code sys_plugins} 上架行（若存在），全量重载宿主。
     * 不删除磁盘目录；需要删文件时请使用宿主 {@code /system/pluginHost/uninstall}。
     */
    void removeCatalogEntry(String pluginId);

    /**
     * 删除该插件在插件根目录下的<strong>全部磁盘安装</strong>，并同步库表（版本行 + 市场行）；由调用方在事务提交后 {@code reload}。
     */
    void purgePluginDiskAndCatalog(String pluginId) throws IOException;
}
