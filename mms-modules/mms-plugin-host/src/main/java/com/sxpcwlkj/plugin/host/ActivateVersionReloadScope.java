package com.sxpcwlkj.plugin.host;

/**
 * {@link PluginHostController#activateVersion} 后如何重载插件。
 */
public enum ActivateVersionReloadScope {

    /** 全量卸载再加载（拓扑序，推荐默认）。 */
    FULL,

    /**
     * 仅卸载并重载传入的 pluginId+version（更快；多插件依赖场景请谨慎或仍用 FULL）。
     */
    SINGLE_TARGET
}
