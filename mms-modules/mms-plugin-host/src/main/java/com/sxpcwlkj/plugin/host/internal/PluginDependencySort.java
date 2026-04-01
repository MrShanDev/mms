package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDependencyDescriptor;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.PluginVersionConstraint;
import com.sxpcwlkj.plugin.host.PluginVersionCoordinate;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 按 {@link PluginDescriptor#getDependencies()}（忽略 optional=true）拓扑排序加载顺序；
 * 必选依赖缺失或多版本歧义时抛 {@link PluginException}。
 */
@Slf4j
public final class PluginDependencySort {

    public record PluginLoadSlot(String folderPluginId, String folderVersion, Path versionDir) {}

    private PluginDependencySort() {
    }

    /**
     * 库表激活坐标：按依赖拓扑排序；磁盘缺目录的坐标在调用前应已过滤。
     */
    public static List<PluginVersionCoordinate> sortCoordinates(Path root, List<PluginVersionCoordinate> coords)
            throws Exception {
        if (coords == null || coords.isEmpty()) {
            return List.of();
        }
        LinkedHashMap<String, String> versionByPlugin = new LinkedHashMap<>();
        for (PluginVersionCoordinate c : coords) {
            if (c.pluginId() == null || c.version() == null) {
                continue;
            }
            String pid = c.pluginId().trim();
            String ver = c.version().trim();
            if (versionByPlugin.containsKey(pid)) {
                log.warn("插件 {} 在激活列表中出现多次，保留后者版本 {}", pid, ver);
            }
            versionByPlugin.put(pid, ver);
        }
        Map<String, PluginDescriptor> descByKey = new LinkedHashMap<>();
        Map<String, PluginVersionCoordinate> coordByKey = new LinkedHashMap<>();
        for (PluginVersionCoordinate c : coords) {
            if (c.pluginId() == null || c.version() == null) {
                continue;
            }
            Path verDir = root.resolve(PluginInstallationLayout.safeSegment(c.pluginId().trim()))
                    .resolve(PluginInstallationLayout.safeSegment(c.version().trim()));
            if (!Files.isDirectory(verDir)) {
                continue;
            }
            var probe = PluginDescriptorProbe.tryRead(verDir);
            if (probe.isEmpty()) {
                continue;
            }
            PluginDescriptor d = probe.get().descriptor();
            String key = d.getId() + "@" + d.getVersion();
            descByKey.putIfAbsent(key, d);
            coordByKey.putIfAbsent(key, c);
        }
        DepKeyResolver coordResolver = (depId) -> {
            String v = versionByPlugin.get(depId.trim());
            if (v == null) {
                throw new PluginException("缺少必选依赖插件的激活版本: " + depId);
            }
            return depId.trim() + "@" + v.trim();
        };
        validateVersionRanges(descByKey, coordResolver);
        List<String> orderedKeys = topologicalKeys(descByKey, coordResolver);
        List<PluginVersionCoordinate> out = new ArrayList<>();
        for (String k : orderedKeys) {
            PluginVersionCoordinate pv = coordByKey.get(k);
            if (pv != null) {
                out.add(pv);
            }
        }
        return out;
    }

    /**
     * 磁盘扫描批次：同一批内每个 pluginId 只能有一个版本，否则无法解析依赖。
     */
    public static List<PluginLoadSlot> sortDiskSlots(List<PluginLoadSlot> slots) throws Exception {
        if (slots == null || slots.isEmpty()) {
            return List.of();
        }
        Map<String, PluginDescriptor> descByKey = new LinkedHashMap<>();
        Map<String, PluginLoadSlot> slotByKey = new LinkedHashMap<>();
        for (PluginLoadSlot s : slots) {
            var probe = PluginDescriptorProbe.tryRead(s.versionDir());
            if (probe.isEmpty()) {
                continue;
            }
            PluginDescriptor d = probe.get().descriptor();
            String key = d.getId() + "@" + d.getVersion();
            descByKey.putIfAbsent(key, d);
            slotByKey.putIfAbsent(key, s);
        }
        Map<String, Long> versionCountByPlugin = descByKey.keySet().stream()
                .collect(Collectors.groupingBy(k -> k.substring(0, k.indexOf('@')), Collectors.counting()));
        for (Map.Entry<String, Long> e : versionCountByPlugin.entrySet()) {
            if (e.getValue() > 1) {
                throw new PluginException(
                        "同批次磁盘扫描中插件 " + e.getKey() + " 存在多个版本，无法解析 dependencies 顺序，请用库表激活单版本");
            }
        }
        Map<String, String> soleVersionById = new HashMap<>();
        for (String k : descByKey.keySet()) {
            int at = k.indexOf('@');
            soleVersionById.put(k.substring(0, at), k.substring(at + 1));
        }
        DepKeyResolver diskResolver = (depId) -> {
            String v = soleVersionById.get(depId.trim());
            if (v == null) {
                throw new PluginException("缺少必选依赖插件（同批未加载）: " + depId);
            }
            return depId.trim() + "@" + v;
        };
        validateVersionRanges(descByKey, diskResolver);
        List<String> orderedKeys = topologicalKeys(descByKey, diskResolver);
        List<PluginLoadSlot> out = new ArrayList<>();
        for (String k : orderedKeys) {
            PluginLoadSlot slot = slotByKey.get(k);
            if (slot != null) {
                out.add(slot);
            }
        }
        return out;
    }

    private static void validateVersionRanges(Map<String, PluginDescriptor> descByKey, DepKeyResolver r)
            throws PluginException {
        for (Map.Entry<String, PluginDescriptor> e : descByKey.entrySet()) {
            String key = e.getKey();
            PluginDescriptor d = e.getValue();
            if (d.getDependencies() == null) {
                continue;
            }
            for (PluginDependencyDescriptor dep : d.getDependencies()) {
                if (Boolean.TRUE.equals(dep.getOptional())) {
                    continue;
                }
                if (dep.getId() == null || dep.getId().isBlank()) {
                    continue;
                }
                String depKey = r.resolve(dep.getId());
                PluginDescriptor depDesc = descByKey.get(depKey);
                if (depDesc == null) {
                    continue;
                }
                if (!PluginVersionConstraint.satisfiedBy(dep.getVersionRange(), depDesc.getVersion())) {
                    throw new PluginException(
                            "插件 "
                                    + key
                                    + " 依赖 "
                                    + depKey
                                    + " 的版本约束「"
                                    + dep.getVersionRange()
                                    + "」与实际版本「"
                                    + depDesc.getVersion()
                                    + "」不匹配");
                }
            }
        }
    }

    private static List<String> topologicalKeys(
            Map<String, PluginDescriptor> descByKey, DepKeyResolver depKeyResolver) throws PluginException {

        Set<String> nodes = descByKey.keySet();
        Map<String, List<String>> depsOf = new HashMap<>(); // key -> dep keys
        Map<String, List<String>> reverse = new HashMap<>(); // dep -> dependents
        for (String key : nodes) {
            depsOf.put(key, new ArrayList<>());
        }
        for (Map.Entry<String, PluginDescriptor> e : descByKey.entrySet()) {
            String key = e.getKey();
            PluginDescriptor d = e.getValue();
            if (d.getDependencies() == null) {
                continue;
            }
            for (PluginDependencyDescriptor dep : d.getDependencies()) {
                if (Boolean.TRUE.equals(dep.getOptional())) {
                    continue;
                }
                if (dep.getId() == null || dep.getId().isBlank()) {
                    continue;
                }
                String depKey = depKeyResolver.resolve(dep.getId());
                if (!nodes.contains(depKey)) {
                    throw new PluginException("插件 " + key + " 依赖 " + depKey + "，但本次加载批次中未包含该插件");
                }
                depsOf.get(key).add(depKey);
                reverse.computeIfAbsent(depKey, k -> new ArrayList<>()).add(key);
            }
        }
        Map<String, Integer> indegree = new HashMap<>();
        for (String key : nodes) {
            indegree.put(key, depsOf.get(key).size());
        }
        Queue<String> q = new ArrayDeque<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) {
                q.add(e.getKey());
            }
        }
        List<String> out = new ArrayList<>();
        while (!q.isEmpty()) {
            String u = q.remove();
            out.add(u);
            for (String v : reverse.getOrDefault(u, List.of())) {
                int left = indegree.merge(v, -1, Integer::sum);
                if (left == 0) {
                    q.add(v);
                }
            }
        }
        if (out.size() != nodes.size()) {
            throw new PluginException("插件依赖存在环路，无法确定加载顺序");
        }
        return out;
    }

    @FunctionalInterface
    private interface DepKeyResolver {
        String resolve(String depPluginId) throws PluginException;
    }
}
