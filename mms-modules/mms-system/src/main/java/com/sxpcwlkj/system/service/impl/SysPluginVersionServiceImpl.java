package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginInstallationLayout;
import com.sxpcwlkj.plugin.PluginSysConfigDef;
import com.sxpcwlkj.plugin.PluginSysConfigKeys;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.host.PluginHostProperties;
import com.sxpcwlkj.plugin.host.PluginVersionCoordinate;
import com.sxpcwlkj.system.entity.SysPlugin;
import com.sxpcwlkj.system.entity.SysPluginVersion;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.mapper.SysPluginMapper;
import com.sxpcwlkj.system.mapper.SysPluginVersionMapper;
import com.sxpcwlkj.system.mapper.SysTenantMapper;
import com.sxpcwlkj.system.service.PluginOwnedMenuBootstrapService;
import com.sxpcwlkj.system.service.SysPluginVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SysPluginVersionServiceImpl implements SysPluginVersionService {

    private final SysPluginVersionMapper sysPluginVersionMapper;
    private final SysPluginMapper sysPluginMapper;
    private final SysTenantMapper sysTenantMapper;
    private final PluginHostProperties pluginHostProperties;
    private final ObjectProvider<PluginSysConfigOperations> pluginSysConfigOperations;
    private final ObjectProvider<PluginOwnedMenuBootstrapService> pluginOwnedMenuBootstrapService;

    @Override
    public List<PluginVersionCoordinate> listActiveCoordinatesForTenant(String tenantId) {
        String tid = normalizeTenant(tenantId);
        List<SysPluginVersion> rows = sysPluginVersionMapper.selectList(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getTenantId, tid)
                        .eq(SysPluginVersion::getIsActive, 1));
        if (rows.isEmpty()) {
            return List.of();
        }
        return rows.stream()
                .map(r -> new PluginVersionCoordinate(r.getPluginId(), r.getVersion()))
                .toList();
    }

    @Override
    public Set<String> pluginIdsManagedInTenant(String tenantId) {
        String tid = normalizeTenant(tenantId);
        List<SysPluginVersion> rows = sysPluginVersionMapper.selectList(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getTenantId, tid)
                        .select(SysPluginVersion::getPluginId));
        if (rows.isEmpty()) {
            return Set.of();
        }
        return rows.stream().map(SysPluginVersion::getPluginId).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordInstallSuccess(PluginDescriptor descriptor, String tenantId) {
        String tid = normalizeTenant(tenantId);
        String pid = descriptor.getId();
        String ver = descriptor.getVersion();
        deactivateAllForPlugin(pid, tid);
        SysPluginVersion existing = sysPluginVersionMapper.selectOne(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pid)
                        .eq(SysPluginVersion::getVersion, ver)
                        .eq(SysPluginVersion::getTenantId, tid)
                        .last("LIMIT 1"));
        if (existing == null) {
            SysPluginVersion row = new SysPluginVersion();
            row.setId(String.valueOf(IdWorker.getId()));
            row.setPluginId(pid);
            row.setVersion(ver);
            row.setIsActive(1);
            row.setTenantId(tid);
            sysPluginVersionMapper.insert(row);
        } else {
            existing.setIsActive(1);
            sysPluginVersionMapper.updateById(existing);
        }
        upsertMarketCatalog(descriptor, tid);
        seedPluginSysConfigKeysIfAbsent(descriptor, tid);
        pluginOwnedMenuBootstrapService.ifAvailable(s -> s.syncOnInstall(descriptor));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterUninstallFromDisk(String pluginId, String versionOrNullMeansAll, String tenantId) {
        String tid = normalizeTenant(tenantId);
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        if (versionOrNullMeansAll == null || versionOrNullMeansAll.isBlank()) {
            sysPluginVersionMapper.delete(
                    new LambdaQueryWrapper<SysPluginVersion>()
                            .eq(SysPluginVersion::getPluginId, pluginId)
                            .eq(SysPluginVersion::getTenantId, tid));
            return;
        }
        sysPluginVersionMapper.delete(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getVersion, versionOrNullMeansAll)
                        .eq(SysPluginVersion::getTenantId, tid));
        ensureOneActiveIfVersionsRemain(pluginId, tid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateVersionOnDisk(String pluginId, String version, String tenantId) {
        if (pluginId == null || pluginId.isBlank() || version == null || version.isBlank()) {
            throw new IllegalArgumentException("pluginId 与 version 不能为空");
        }
        assertVersionDirHasJar(pluginId, version);
        String tid = normalizeTenant(tenantId);
        deactivateAllForPlugin(pluginId, tid);
        SysPluginVersion existing = sysPluginVersionMapper.selectOne(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getVersion, version)
                        .eq(SysPluginVersion::getTenantId, tid)
                        .last("LIMIT 1"));
        if (existing == null) {
            SysPluginVersion row = new SysPluginVersion();
            row.setId(String.valueOf(IdWorker.getId()));
            row.setPluginId(pluginId);
            row.setVersion(version);
            row.setIsActive(1);
            row.setTenantId(tid);
            sysPluginVersionMapper.insert(row);
        } else {
            existing.setIsActive(1);
            sysPluginVersionMapper.updateById(existing);
        }
    }

    @Override
    public List<SysPluginVersion> listVersionsForPlugin(String pluginId, String tenantId) {
        String ptid = normalizeTenant(tenantId);
        return sysPluginVersionMapper.selectList(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getTenantId, ptid)
                        .orderByDesc(SysPluginVersion::getCreatedTime));
    }

    @Override
    public boolean hasVersionRowsForPlugin(String pluginId, String tenantId) {
        if (pluginId == null || pluginId.isBlank()) {
            return false;
        }
        String tid = normalizeTenant(tenantId);
        Long c = sysPluginVersionMapper.selectCount(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId.trim())
                        .eq(SysPluginVersion::getTenantId, tid));
        return c != null && c > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deactivateAllVersionsForPlugin(String pluginId, String tenantId) {
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        deactivateAllForPlugin(pluginId.trim(), normalizeTenant(tenantId));
    }

    private void deactivateAllForPlugin(String pluginId, String tenantId) {
        sysPluginVersionMapper.update(
                null,
                new LambdaUpdateWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getTenantId, tenantId)
                        .set(SysPluginVersion::getIsActive, 0));
    }

    private void ensureOneActiveIfVersionsRemain(String pluginId, String tenantId) {
        Long c = sysPluginVersionMapper.selectCount(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getTenantId, tenantId));
        if (c == null || c == 0) {
            return;
        }
        Long active = sysPluginVersionMapper.selectCount(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getTenantId, tenantId)
                        .eq(SysPluginVersion::getIsActive, 1));
        if (active != null && active > 0) {
            return;
        }
        SysPluginVersion latest = sysPluginVersionMapper.selectOne(
                new LambdaQueryWrapper<SysPluginVersion>()
                        .eq(SysPluginVersion::getPluginId, pluginId)
                        .eq(SysPluginVersion::getTenantId, tenantId)
                        .orderByDesc(SysPluginVersion::getCreatedTime)
                        .last("LIMIT 1"));
        if (latest != null) {
            latest.setIsActive(1);
            sysPluginVersionMapper.updateById(latest);
        }
    }

    /**
     * 安装成功时：对 {@code plugin.json} 中 {@code sysConfig} 声明的每项，在<strong>插件登记租户</strong>及
     * {@code sys_tenant} 中<strong>每一租户</strong>下，若 {@code sys_config} 尚无对应 {@code config_key} 则插入
     *（名称与默认值来自描述符）；已存在则跳过，不覆盖运维已改过的值。
     * <p>卸载时清理插件专属配置由宿主 {@link com.sxpcwlkj.plugin.host.PluginHostDbBridge} 编排，不在此直接删库。</p>
     */
    private void seedPluginSysConfigKeysIfAbsent(PluginDescriptor descriptor, String tenantId) {
        pluginSysConfigOperations.ifAvailable(ops -> {
            List<PluginSysConfigDef> defs = descriptor.getSysConfig();
            if (defs == null || defs.isEmpty()) {
                return;
            }
            String pid = descriptor.getId();
            Set<String> tenantIds = new LinkedHashSet<>();
            tenantIds.add(normalizeTenant(tenantId));
            List<SysTenant> tenants = sysTenantMapper.selectList(new LambdaQueryWrapper<>());
            if (tenants != null) {
                for (SysTenant st : tenants) {
                    if (st != null && st.getTenantId() != null && !st.getTenantId().isBlank()) {
                        tenantIds.add(st.getTenantId().trim());
                    }
                }
            }
            for (String tid : tenantIds) {
                for (PluginSysConfigDef def : defs) {
                    if (def == null || def.getKeySuffix() == null || def.getKeySuffix().isBlank()) {
                        continue;
                    }
                    try {
                        PluginSysConfigKeys.validateSuffix(def.getKeySuffix());
                    } catch (PluginException ex) {
                        throw new IllegalStateException(
                                "插件 " + pid + " 的 sysConfig 项非法: " + ex.getMessage(), ex);
                    }
                    String suffix = def.getKeySuffix().trim();
                    String name =
                            def.getConfigName() != null && !def.getConfigName().isBlank()
                                    ? def.getConfigName().trim()
                                    : suffix;
                    String val = def.getDefaultValue() != null ? def.getDefaultValue() : "";
                    ops.insertIfAbsent(tid, pid, name, suffix, val);
                }
            }
        });
    }

    private void upsertMarketCatalog(PluginDescriptor d, String tenantId) {
        SysPlugin existing = sysPluginMapper.selectOne(
                new LambdaQueryWrapper<SysPlugin>()
                        .eq(SysPlugin::getPluginId, d.getId())
                        .eq(SysPlugin::getTenantId, tenantId)
                        .last("LIMIT 1"));
        if (existing == null) {
            SysPlugin p = new SysPlugin();
            p.setId(String.valueOf(IdWorker.getId()));
            p.setPluginId(d.getId());
            p.setName(d.getName());
            p.setDescription(d.getDescription());
            p.setIconUrl("");
            p.setTenantId(tenantId);
            p.setStatus(1);
            p.setSort(100);
            sysPluginMapper.insert(p);
        } else {
            boolean need = (existing.getName() == null || existing.getName().isBlank())
                    || (existing.getDescription() == null || existing.getDescription().isBlank());
            if (need) {
                if (existing.getName() == null || existing.getName().isBlank()) {
                    existing.setName(d.getName());
                }
                if (existing.getDescription() == null || existing.getDescription().isBlank()) {
                    existing.setDescription(d.getDescription());
                }
                sysPluginMapper.updateById(existing);
            }
        }
    }

    private void assertVersionDirHasJar(String pluginId, String version) {
        Path root = resolveRoot();
        Path verDir = PluginInstallationLayout.pluginRoot(root, pluginId, version);
        Path lib = verDir.resolve("lib");
        if (!Files.isDirectory(lib)) {
            throw new IllegalStateException("磁盘上不存在该版本的 lib 目录: " + pluginId + "@" + version);
        }
        try (Stream<Path> s = Files.list(lib)) {
            boolean any = s.anyMatch(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar"));
            if (!any) {
                throw new IllegalStateException("该版本 lib 下无 JAR: " + pluginId + "@" + version);
            }
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                throw (IllegalStateException) e;
            }
            throw new IllegalStateException("无法校验插件目录: " + e.getMessage());
        }
    }

    private Path resolveRoot() {
        if (pluginHostProperties.getRootDir() != null && !pluginHostProperties.getRootDir().isBlank()) {
            return Path.of(pluginHostProperties.getRootDir().trim()).toAbsolutePath().normalize();
        }
        return Path.of(System.getProperty("user.dir", "."), "mms-plugins").toAbsolutePath().normalize();
    }

    private static String normalizeTenant(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return "000000";
        }
        return tenantId.trim();
    }
}
