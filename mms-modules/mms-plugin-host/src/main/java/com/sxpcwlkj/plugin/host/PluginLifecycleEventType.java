package com.sxpcwlkj.plugin.host;

/**
 * 插件宿主生命周期事件（用于通知：企业微信等）。
 */
public enum PluginLifecycleEventType {
    /** 插件 SPI 加载并完成路由注册等收尾 */
    PLUGIN_LOADED("插件加载成功"),
    /** 校验、onLoad、HOST_MVC 注册等任一步失败 */
    PLUGIN_LOAD_FAILED("插件加载失败"),
    /** 已从内存卸载（含替换版本时卸载旧版） */
    PLUGIN_UNLOADED("插件已卸载"),
    /** 卸载钩子或关闭 ClassLoader 异常 */
    PLUGIN_UNLOAD_FAILED("插件卸载失败"),
    /** 激活版本重载时目标插件加载失败 */
    PLUGIN_RELOAD_TARGET_FAILED("插件激活重载失败"),
    /** 仅从磁盘删除安装目录（PluginHost uninstall） */
    PLUGIN_DISK_UNINSTALLED("插件磁盘目录已删除"),
    /** 市场「删除」：卸载内存后删盘并清库表 */
    PLUGIN_PURGED("插件已删除（磁盘+库表）");

    private final String title;

    PluginLifecycleEventType(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
