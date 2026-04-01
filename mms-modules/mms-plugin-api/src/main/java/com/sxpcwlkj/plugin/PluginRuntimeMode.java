package com.sxpcwlkj.plugin;

/**
 * 插件运行模式（{@code plugin.json} 字段 {@code runtimeMode}），与 v2 宿主需求一致。
 * <ul>
 *   <li>{@link #SPI_ONLY} — 仅 SPI，不向宿主 8080 注册路由（默认，兼容未声明字段的旧插件）。</li>
 *   <li>{@link #HOST_MVC} — 依赖宿主能力，可向主端口注册路由（P1）。</li>
 *   <li>{@link #INDEPENDENT_PROCESS} — 独立进程对外端口（P2 PortManager；P0 仅校验元数据并同 JVM 完成 SPI 加载，见宿主说明）。</li>
 * </ul>
 */
public enum PluginRuntimeMode {

    SPI_ONLY,
    HOST_MVC,
    INDEPENDENT_PROCESS
}
