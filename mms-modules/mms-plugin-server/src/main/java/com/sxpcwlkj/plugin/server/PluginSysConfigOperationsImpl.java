package com.sxpcwlkj.plugin.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.plugin.PluginSysConfigKeys;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.PluginSysConfigRow;
import com.sxpcwlkj.system.entity.SysConfig;
import com.sxpcwlkj.system.entity.SysPlugin;
import com.sxpcwlkj.system.entity.SysPluginVersion;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.mapper.SysConfigMapper;
import com.sxpcwlkj.system.mapper.SysPluginMapper;
import com.sxpcwlkj.system.mapper.SysPluginVersionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 插件专属 {@code sys_config} 读写（键 {@link PluginSysConfigKeys#fullKey(String, String)}）。
 */
@Component
@RequiredArgsConstructor
public class PluginSysConfigOperationsImpl implements PluginSysConfigOperations {

    private static final int MAX_VALUE_LEN = 255;

    private final SysConfigMapper baseMapper;
    private final SysPluginMapper sysPluginMapper;
    private final SysPluginVersionMapper sysPluginVersionMapper;

    @Override
    public Optional<String> get(String tenantId, String pluginId, String keySuffix) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        PluginSysConfigKeys.validateSuffix(keySuffix);
        String full = PluginSysConfigKeys.fullKey(pluginId, keySuffix);
        String tid = normalizeTenant(tenantId);
        SysConfigVo vo =
                baseMapper.selectVoOne(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getConfigKey, full)
                                .eq(SysConfig::getTenantId, tid)
                                .last("LIMIT 1"));
        if (vo == null || vo.getConfigValue() == null) {
            return Optional.empty();
        }
        return Optional.of(vo.getConfigValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upsert(String tenantId, String pluginId, String configName, String keySuffix, String value) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        PluginSysConfigKeys.validateSuffix(keySuffix);
        if (value != null && value.length() > MAX_VALUE_LEN) {
            throw new IllegalArgumentException("config_value 长度不能超过 " + MAX_VALUE_LEN + "（sys_config 字段限制）");
        }
        String full = PluginSysConfigKeys.fullKey(pluginId, keySuffix);
        String tid = normalizeTenant(tenantId);
        SysConfig existing =
                baseMapper.selectOne(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getConfigKey, full)
                                .eq(SysConfig::getTenantId, tid)
                                .last("LIMIT 1"));
        if (existing != null) {
            existing.setConfigName(configName);
            existing.setConfigValue(value == null ? "" : value);
            baseMapper.updateById(existing);
            return;
        }
        SysConfig row = new SysConfig();
        row.setId(String.valueOf(IdWorker.getId()));
        row.setTenantId(tid);
        row.setConfigKey(full);
        row.setConfigName(configName);
        row.setConfigValue(value == null ? "" : value);
        row.setConfigType(2);
        row.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        row.setSort(0);
        baseMapper.insert(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIfAbsent(
            String tenantId, String pluginId, String configName, String keySuffix, String defaultValue) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        PluginSysConfigKeys.validateSuffix(keySuffix);
        String v = defaultValue == null ? "" : defaultValue;
        if (v.length() > MAX_VALUE_LEN) {
            throw new IllegalArgumentException("config_value 长度不能超过 " + MAX_VALUE_LEN + "（sys_config 字段限制）");
        }
        String full = PluginSysConfigKeys.fullKey(pluginId, keySuffix);
        String tid = normalizeTenant(tenantId);
        SysConfig existing =
                baseMapper.selectOne(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getConfigKey, full)
                                .eq(SysConfig::getTenantId, tid)
                                .last("LIMIT 1"));
        if (existing != null) {
            return;
        }
        SysConfig row = new SysConfig();
        row.setId(String.valueOf(IdWorker.getId()));
        row.setTenantId(tid);
        row.setConfigKey(full);
        row.setConfigName(configName);
        row.setConfigValue(v);
        row.setConfigType(2);
        row.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        row.setSort(0);
        baseMapper.insert(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllKeysForPluginAllTenants(String pluginId) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        String pid = pluginId.trim();
        String prefix = PluginSysConfigKeys.keyPrefix(pid);
        Set<String> registry = new HashSet<>(loadRegisteredPluginIds());
        registry.add(pid);
        List<SysConfig> rows =
                baseMapper.selectList(
                        new LambdaQueryWrapper<SysConfig>().likeRight(SysConfig::getConfigKey, prefix));
        for (SysConfig row : rows) {
            if (row.getConfigKey() == null) {
                continue;
            }
            Optional<String> owner = PluginSysConfigKeys.resolveOwningPluginId(row.getConfigKey(), registry);
            if (owner.isPresent() && owner.get().equals(pid)) {
                baseMapper.deleteById(row.getId());
            }
        }
    }

    @Override
    public List<PluginSysConfigRow> listForPlugin(String tenantId, String pluginId) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        String tid = normalizeTenant(tenantId);
        String p = pluginId.trim();
        Set<String> registry = new HashSet<>(loadRegisteredPluginIds());
        registry.add(p);
        String prefix = PluginSysConfigKeys.keyPrefix(p);
        List<SysConfigVo> vos =
                baseMapper.selectVoList(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getTenantId, tid)
                                .likeRight(SysConfig::getConfigKey, prefix)
                                .orderByAsc(SysConfig::getConfigKey));
        List<PluginSysConfigRow> out = new ArrayList<>();
        for (SysConfigVo vo : vos) {
            String full = vo.getConfigKey();
            if (full == null) {
                continue;
            }
            Optional<String> owner = PluginSysConfigKeys.resolveOwningPluginId(full, registry);
            if (owner.isEmpty() || !owner.get().equals(p)) {
                continue;
            }
            String suffix = PluginSysConfigKeys.extractSuffix(p, full);
            out.add(
                    new PluginSysConfigRow(
                            full,
                            suffix,
                            vo.getConfigName() != null ? vo.getConfigName() : suffix,
                            vo.getConfigValue() != null ? vo.getConfigValue() : ""));
        }
        return out;
    }

    private Set<String> loadRegisteredPluginIds() {
        Set<String> s = new HashSet<>();
        addPluginIdsFromObjs(
                sysPluginMapper.selectObjs(
                        new LambdaQueryWrapper<SysPlugin>().select(SysPlugin::getPluginId)),
                s);
        addPluginIdsFromObjs(
                sysPluginVersionMapper.selectObjs(
                        new LambdaQueryWrapper<SysPluginVersion>().select(SysPluginVersion::getPluginId)),
                s);
        return s;
    }

    private static void addPluginIdsFromObjs(List<Object> objs, Set<String> into) {
        if (objs == null) {
            return;
        }
        for (Object o : objs) {
            if (o == null) {
                continue;
            }
            String t = o.toString().trim();
            if (!t.isEmpty()) {
                into.add(t);
            }
        }
    }

    private static String normalizeTenant(String tenantId) {
        return tenantId != null && !tenantId.isBlank() ? tenantId.trim() : "000000";
    }
}
