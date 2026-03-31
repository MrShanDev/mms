package com.sxpcwlkj.plugin.host;

import com.sxpcwlkj.plugin.PluginFrontendHint;
import com.sxpcwlkj.plugin.PluginKind;

/**
 * 已加载插件的 manifest 视图（供前端与联邦路由对齐 {@code plugin.json} 元数据）。
 */
public record PluginManifestView(
        String id,
        String version,
        String name,
        String description,
        PluginKind kind,
        PluginFrontendHint frontend
) {
}
