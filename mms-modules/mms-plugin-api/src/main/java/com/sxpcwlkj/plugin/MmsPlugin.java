package com.sxpcwlkj.plugin;

/**
 * 插件 SPI 入口。实现类应在 JAR 中注册
 * {@link PluginConstants#SPI_SERVICES_RESOURCE}，并由宿主通过 {@link java.util.ServiceLoader} 解析。
 * <p>{@link PluginRuntimeMode#INDEPENDENT_PROCESS} 且宿主开启子进程时：子进程跑独立 {@code mainClass}；
 * 宿主 JVM 仍会加载本 SPI（供 {@link PluginRuntimeContext#tryInvokePeerPlugin}、健康接口及生命周期管理），
 * 请勿在 {@code onLoad} 中假设已与子进程网络互通。</p>
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
