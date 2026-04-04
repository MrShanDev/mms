package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sxpcwlkj.plugin.host.DiskPluginSlot;
import com.sxpcwlkj.plugin.host.PluginHealthRow;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.PluginRuntimeMode;
import com.sxpcwlkj.plugin.host.PluginJarLocationStatus;
import com.sxpcwlkj.plugin.host.PluginLifecycleEvent;
import com.sxpcwlkj.plugin.host.PluginLifecycleEventType;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.plugin.host.PluginSubprocessSnapshot;
import com.sxpcwlkj.plugin.host.PluginManifestView;
import com.sxpcwlkj.system.entity.SysPlugin;
import com.sxpcwlkj.system.entity.SysPluginVersion;
import com.sxpcwlkj.system.entity.vo.PluginMarketCardVo;
import com.sxpcwlkj.system.mapper.SysPluginMapper;
import com.sxpcwlkj.plugin.host.PluginHostDbBridge;
import com.sxpcwlkj.system.service.SysPluginMarketService;
import com.sxpcwlkj.system.service.SysPluginVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sxpcwlkj.system.service.impl.PluginHostDbBridgeImpl.PLUGIN_REGISTRY_TENANT;

@Service
@RequiredArgsConstructor
public class SysPluginMarketServiceImpl implements SysPluginMarketService {

    private final SysPluginMapper sysPluginMapper;
    private final SysPluginVersionService sysPluginVersionService;
    private final PluginLifecycleManager pluginLifecycleManager;
    private final PluginHostProperties pluginHostProperties;
    private final PluginHostDbBridge pluginHostDbBridge;

    @Override
    public List<PluginMarketCardVo> listMarketCards() {
        String tenantId = PLUGIN_REGISTRY_TENANT;
        List<SysPlugin> catalog = sysPluginMapper.selectList(Wrappers.<SysPlugin>lambdaQuery()
                .eq(SysPlugin::getTenantId, PLUGIN_REGISTRY_TENANT)
                .eq(SysPlugin::getStatus, 1)
                .orderByAsc(SysPlugin::getSort));

        List<DiskPluginSlot> allDiskSlots = pluginLifecycleManager.listDiskSlots();
        Map<String, List<DiskPluginSlot>> diskByPluginAllSlots = allDiskSlots.stream()
                .collect(Collectors.groupingBy(DiskPluginSlot::pluginId, LinkedHashMap::new, Collectors.toList()));

        Map<String, PluginManifestView> manifestByPlugin = new LinkedHashMap<>();
        for (PluginManifestView v : pluginLifecycleManager.listManifests()) {
            manifestByPlugin.putIfAbsent(v.id(), v);
        }

        List<PluginHealthRow> healthRows = pluginLifecycleManager.collectHealth();
        Map<String, List<PluginHealthRow>> healthByPluginId = healthRows.stream()
                .collect(Collectors.groupingBy(PluginHealthRow::pluginId, LinkedHashMap::new, Collectors.toList()));

        boolean hostEnabled = pluginHostProperties.isEnabled();
        String rootHint = pluginHostProperties.getRootDir();
        if (rootHint == null || rootHint.isBlank()) {
            rootHint = "(默认 user.dir/mms-plugins)";
        }

        Map<String, SysPlugin> catalogByPid = new LinkedHashMap<>();
        for (SysPlugin row : catalog) {
            if (row.getPluginId() != null && !row.getPluginId().isBlank()) {
                catalogByPid.putIfAbsent(row.getPluginId().trim(), row);
            }
        }

        Set<String> orderedIds = new LinkedHashSet<>();
        orderedIds.addAll(catalogByPid.keySet());
        List<String> rest = new ArrayList<>(diskByPluginAllSlots.keySet());
        Collections.sort(rest, String.CASE_INSENSITIVE_ORDER);
        orderedIds.addAll(rest);
        orderedIds.addAll(manifestByPlugin.keySet());

        boolean rootReady = pluginLifecycleManager.isPluginsRootDirectory();
        List<PluginMarketCardVo> out = new ArrayList<>();
        for (String pluginId : orderedIds) {
            SysPlugin row = catalogByPid.get(pluginId);
            List<SysPluginVersion> verRows = sysPluginVersionService.listVersionsForPlugin(pluginId, tenantId);
            String activeVer = verRows.stream()
                    .filter(v -> v.getIsActive() != null && v.getIsActive() == 1)
                    .map(SysPluginVersion::getVersion)
                    .findFirst()
                    .orElse(null);
            List<String> recorded = verRows.stream()
                    .map(SysPluginVersion::getVersion)
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .toList();
            List<DiskPluginSlot> slotsForPlugin = diskByPluginAllSlots.getOrDefault(pluginId, List.of());
            PluginMarketCardVo card = buildCard(pluginId, row, slotsForPlugin,
                    manifestByPlugin.get(pluginId),
                    healthByPluginId.getOrDefault(pluginId, List.of()),
                    hostEnabled, rootHint, activeVer, recorded);
            PluginJarLocationStatus diskWarn = resolveDiskLayoutWarning(rootReady, pluginId, activeVer, slotsForPlugin);
            card.setDiskLayoutWarning(diskWarn == PluginJarLocationStatus.OK ? null : diskWarn.name());
            card.setSubprocessLaunchEnabled(pluginHostProperties.isSubprocessLaunchEnabled());
            mergeSubprocessRuntime(card, manifestByPlugin.get(pluginId), hostEnabled);
            out.add(card);
        }
        return out;
    }

    private PluginJarLocationStatus resolveDiskLayoutWarning(
            boolean rootReady,
            String pluginId,
            String catalogActiveVersion,
            List<DiskPluginSlot> diskForPlugin) {
        if (!rootReady) {
            return PluginJarLocationStatus.ROOT_NOT_DIRECTORY;
        }
        if (catalogActiveVersion != null && !catalogActiveVersion.isBlank()) {
            PluginJarLocationStatus st = pluginLifecycleManager.probeVersionLayout(pluginId, catalogActiveVersion);
            if (st != PluginJarLocationStatus.OK) {
                return st;
            }
        }
        if (diskForPlugin != null && !diskForPlugin.isEmpty()) {
            boolean anyJar = diskForPlugin.stream().anyMatch(DiskPluginSlot::libHasJars);
            if (!anyJar) {
                return PluginJarLocationStatus.JAR_NOT_FOUND;
            }
        }
        return PluginJarLocationStatus.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCatalogEntry(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        String tid = PLUGIN_REGISTRY_TENANT;
        String pid = pluginId.trim();
        sysPluginVersionService.afterUninstallFromDisk(pid, null, tid);
        sysPluginMapper.delete(Wrappers.<SysPlugin>lambdaQuery()
                .eq(SysPlugin::getPluginId, pid)
                .eq(SysPlugin::getTenantId, tid));
        // 全量重载由 Controller 在事务提交后触发，避免读到未提交的库表
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void purgePluginDiskAndCatalog(String pluginId) throws IOException {
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        String pid = pluginId.trim();
        // 先卸载内存中的 ClassLoader / 子进程 / 路由，再删磁盘，避免运行中删除 JAR 失败（尤其 Windows）
        pluginLifecycleManager.unloadAllVersionsOfPlugin(pid);
        pluginLifecycleManager.uninstallFromDisk(pid, null, false);
        pluginHostDbBridge.onUninstallDiskFinished(pid, null);
        removeCatalogEntry(pid);
        pluginLifecycleManager.publishPluginLifecycleEvent(
                PluginLifecycleEvent.of(
                        PluginLifecycleEventType.PLUGIN_PURGED,
                        pid,
                        null,
                        "市场「删除」：已卸载内存、删除磁盘安装目录并移除库表登记",
                        null));
    }

    private static PluginMarketCardVo buildCard(
            String pluginId,
            SysPlugin catalog,
            List<DiskPluginSlot> diskForPlugin,
            PluginManifestView manifest,
            List<PluginHealthRow> healthForPlugin,
            boolean hostEnabled,
            String rootHint,
            String catalogActiveVersion,
            List<String> recordedVersions) {

        PluginMarketCardVo vo = new PluginMarketCardVo();
        vo.setPluginId(pluginId);
        vo.setCatalogId(catalog != null ? catalog.getId() : null);
        vo.setHostEnabled(hostEnabled);
        vo.setRootDirHint(rootHint);

        if (catalog != null) {
            vo.setName(catalog.getName());
            vo.setIconUrl(catalog.getIconUrl());
            vo.setDescription(catalog.getDescription());
        }
        if (vo.getName() == null || vo.getName().isBlank()) {
            vo.setName(manifest != null && manifest.name() != null && !manifest.name().isBlank()
                    ? manifest.name() : pluginId);
        }
        if (vo.getDescription() == null || vo.getDescription().isBlank()) {
            if (manifest != null && manifest.description() != null && !manifest.description().isBlank()) {
                vo.setDescription(manifest.description());
            } else if (!diskForPlugin.isEmpty()) {
                vo.setDescription("插件已通过 JAR 安装在本地，尚未在市场中配置介绍。可在表 sys_plugins 中补充名称、封面与说明。");
            } else {
                vo.setDescription("尚未安装。可上传官方提供的插件包，或联系管理员上架。");
            }
        }

        List<String> diskVers = diskForPlugin.stream()
                .map(DiskPluginSlot::version)
                .sorted(Comparator.reverseOrder())
                .distinct()
                .toList();
        vo.setDiskVersionsLine(diskVers.isEmpty() ? "—" : String.join("、", diskVers));

        boolean onDisk = !diskForPlugin.isEmpty();
        boolean loaded = hostEnabled && manifest != null;
        String state;
        if (loaded) {
            state = "LOADED";
        } else if (onDisk) {
            state = "ON_DISK";
        } else {
            state = "NOT_INSTALLED";
        }
        vo.setRuntimeState(state);
        vo.setCatalogActiveVersion(catalogActiveVersion);
        vo.setRecordedVersions(recordedVersions == null ? List.of() : recordedVersions);

        if (manifest != null && loaded) {
            vo.setDisplayVersion(manifest.version());
        } else if (catalogActiveVersion != null && !catalogActiveVersion.isBlank()) {
            vo.setDisplayVersion(catalogActiveVersion);
        } else if (!diskVers.isEmpty()) {
            vo.setDisplayVersion(diskVers.get(0));
        } else {
            vo.setDisplayVersion("—");
        }

        vo.setManifest(loaded ? manifest : null);
        vo.setHealthState(resolveHealthState(loaded, healthForPlugin));
        vo.setHealthBody(pickHealthBody(healthForPlugin));
        return vo;
    }

    private static String resolveHealthState(boolean loaded, List<PluginHealthRow> rows) {
        if (!loaded) {
            return "NONE";
        }
        if (rows == null || rows.isEmpty()) {
            return "NONE";
        }
        boolean error = rows.stream().anyMatch(r -> "ERROR".equals(r.state()));
        if (error) {
            return "ABNORMAL";
        }
        boolean ok = rows.stream().anyMatch(r -> "OK".equals(r.state()));
        if (ok) {
            return "NORMAL";
        }
        boolean noSpi = rows.stream().anyMatch(r -> "NO_HEALTH_SPI".equals(r.state()));
        if (noSpi) {
            return "NO_SPI";
        }
        return "UNKNOWN";
    }

    private static String pickHealthBody(List<PluginHealthRow> rows) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        for (PluginHealthRow r : rows) {
            if ("OK".equals(r.state()) && r.body() != null && !r.body().isBlank()) {
                return r.body();
            }
        }
        for (PluginHealthRow r : rows) {
            if ("ERROR".equals(r.state()) && r.body() != null && !r.body().isBlank()) {
                return r.body();
            }
        }
        return null;
    }

    private void mergeSubprocessRuntime(PluginMarketCardVo card, PluginManifestView manifest, boolean hostEnabled) {
        if (!hostEnabled
                || !Boolean.TRUE.equals(card.getSubprocessLaunchEnabled())
                || manifest == null
                || manifest.runtimeMode() != PluginRuntimeMode.INDEPENDENT_PROCESS) {
            return;
        }
        PluginSubprocessSnapshot snap = pluginLifecycleManager.subprocessSnapshotFor(manifest.id(), manifest.version());
        if (snap == null) {
            return;
        }
        card.setSubprocessPort(snap.effectivePort() > 0 ? snap.effectivePort() : null);
        card.setSubprocessHostLeasedPort(snap.hostLeasedPort() > 0 ? snap.hostLeasedPort() : null);
        card.setSubprocessTcpPortAppearsBound(snap.tcpPortAppearsBound());
        card.setSubprocessAlive(snap.alive());
        card.setSubprocessPid(snap.pid());
        card.setSubprocessLastError(snap.lastError());
    }
}
