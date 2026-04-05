package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDependencyDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginVersionConstraint;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在将插件装入内存前，校验 {@link PluginDescriptor#getDependencies()} 中非 optional 依赖是否已在 {@link com.sxpcwlkj.plugin.host.PluginLifecycleManager} 中加载，
 * 且版本区间（若有）与已加载实例一致。失败时抛出 {@link PluginException}，文案面向运维阅读（含「需要先加载哪些插件」）。
 */
@Slf4j
public final class PluginLoadedPeerDependencyValidator {

    private PluginLoadedPeerDependencyValidator() {}

    /**
     * @param desc          即将加载的插件
     * @param loadedPlugins 宿主当前已加载 map，key 为 {@code pluginId@version}
     */
    public static void validateOrThrow(PluginDescriptor desc, Map<String, LoadedPluginInstance> loadedPlugins) {
        if (desc == null) {
            return;
        }
        if (desc.getDependencies() == null || desc.getDependencies().isEmpty()) {
            return;
        }
        String selfId = desc.getId() != null ? desc.getId().trim() : "";
        String selfVer = desc.getVersion() != null ? desc.getVersion().trim() : "";
        String selfKey = selfId + "@" + selfVer;
        List<String> missingLabels = new ArrayList<>();
        for (PluginDependencyDescriptor dep : desc.getDependencies()) {
            if (Boolean.TRUE.equals(dep.getOptional())) {
                continue;
            }
            if (dep.getId() == null || dep.getId().isBlank()) {
                continue;
            }
            String depId = dep.getId().trim();
            LoadedPluginInstance peer = findSingleLoaded(depId, loadedPlugins);
            if (peer == null) {
                String label = depId;
                if (dep.getVersionRange() != null && !dep.getVersionRange().isBlank()) {
                    label = depId + "（要求版本: " + dep.getVersionRange().trim() + "）";
                }
                missingLabels.add(label);
                continue;
            }
            String actualVer = peer.getDescriptor().getVersion();
            if (!PluginVersionConstraint.satisfiedBy(dep.getVersionRange(), actualVer)) {
                throw new PluginException(
                        "本插件「"
                                + selfKey
                                + "」需要插件「"
                                + depId
                                + "」的版本满足约束「"
                                + dep.getVersionRange()
                                + "」，当前已加载版本为「"
                                + actualVer
                                + "」。请先调整已加载版本或修改依赖声明。");
            }
        }
        if (!missingLabels.isEmpty()) {
            throw new PluginException(
                    "本插件「"
                            + selfKey
                            + "」需要先成功加载以下插件后才能加载（请在 plugin.json 的 dependencies 中声明依赖以保证顺序；optional=true 的依赖不参与校验）: "
                            + String.join("、", missingLabels));
        }
    }

    private static LoadedPluginInstance findSingleLoaded(
            String pluginId, Map<String, LoadedPluginInstance> loadedPlugins) {
        if (loadedPlugins == null || loadedPlugins.isEmpty()) {
            return null;
        }
        String prefix = pluginId + "@";
        LoadedPluginInstance found = null;
        for (Map.Entry<String, LoadedPluginInstance> e : loadedPlugins.entrySet()) {
            String k = e.getKey();
            if (k != null && k.regionMatches(true, 0, prefix, 0, prefix.length())) {
                if (found != null) {
                    log.warn(
                            "依赖校验：插件 id {} 对应多个已加载版本（{} 与 {}），校验时使用后者",
                            pluginId,
                            found.getDescriptor().getId() + "@" + found.getDescriptor().getVersion(),
                            e.getKey());
                }
                found = e.getValue();
            }
        }
        return found;
    }
}
