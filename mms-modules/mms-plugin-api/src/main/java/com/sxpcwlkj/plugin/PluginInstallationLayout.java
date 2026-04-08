package com.sxpcwlkj.plugin;

import java.nio.file.Path;

/**
 * 约定插件在磁盘上的隔离目录布局（宿主在安装时应遵循，便于备份与卸载）。
 * <p>
 * 默认结构：{@code <pluginsRoot>/<safeId>/<safeVersion>/{lib,data,tmp}/}，
 * 其中 safe 为将 {@code /} 等替换为 {@code _} 后的片段。
 */
public final class PluginInstallationLayout {

    private PluginInstallationLayout() {
    }

    public static Path pluginRoot(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginsRoot.resolve(safeSegment(pluginId)).resolve(safeSegment(pluginVersion));
    }

    public static Path libDirectory(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginRoot(pluginsRoot, pluginId, pluginVersion).resolve(PluginConstants.SUBDIR_LIB);
    }

    public static Path dataDirectory(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginRoot(pluginsRoot, pluginId, pluginVersion).resolve(PluginConstants.SUBDIR_DATA);
    }

    public static Path temporaryDirectory(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginRoot(pluginsRoot, pluginId, pluginVersion).resolve(PluginConstants.SUBDIR_TMP);
    }

    /**
     * 联邦前端静态文件根目录（{@code remoteEntry.js} 与 chunk 等）。
     */
    public static Path webDirectory(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginRoot(pluginsRoot, pluginId, pluginVersion).resolve(PluginConstants.SUBDIR_WEB);
    }

    public static Path descriptorCopy(Path pluginsRoot, String pluginId, String pluginVersion) {
        return pluginRoot(pluginsRoot, pluginId, pluginVersion).resolve(PluginConstants.DESCRIPTOR_FILE_NAME);
    }

    public static String safeSegment(String raw) {
        if (raw == null || raw.isBlank()) {
            return "_";
        }
        return raw.trim().replace('/', '_').replace('\\', '_').replace(':', '_');
    }
}
