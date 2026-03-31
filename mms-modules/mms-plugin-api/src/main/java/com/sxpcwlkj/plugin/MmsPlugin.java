package com.sxpcwlkj.plugin;

/**
 * 插件 SPI 入口。实现类应在 JAR 中注册
 * {@link PluginConstants#SPI_SERVICES_RESOURCE}，并由宿主通过 {@link java.util.ServiceLoader} 解析。
 */
public interface MmsPlugin {

    /**
     * 插件安装并就绪时调用；与 {@link PluginDescriptor#getEntryClass()} 对应同一实现类时由宿主触发。
     */
    default void onLoad(PluginRuntimeContext context) throws Exception {
    }

    /**
     * 插件卸载或进程退出前调用（热卸载能力取决于宿主实现）。
     */
    default void onUnload() throws Exception {
    }
}
