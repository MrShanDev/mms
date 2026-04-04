package com.sxpcwlkj.plugin.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.plugin.PluginSysConfigKeys;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.PluginSysConfigRow;
import com.sxpcwlkj.system.entity.SysConfig;
import com.sxpcwlkj.system.entity.vo.SysConfigVo;
import com.sxpcwlkj.system.mapper.SysConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 插件专属 {@code sys_config} 读写（键前缀 {@link PluginSysConfigKeys#PREFIX}）。
 */
@Component
@RequiredArgsConstructor
public class PluginSysConfigOperationsImpl implements PluginSysConfigOperations {

    private static final int MAX_VALUE_LEN = 255;

    private final SysConfigMapper baseMapper;

    @Override
    public Optional<String> get(String tenantId, String fullConfigKey) {
        String tid = normalizeTenant(tenantId);
        SysConfigVo vo =
                baseMapper.selectVoOne(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getConfigKey, fullConfigKey)
                                .eq(SysConfig::getTenantId, tid)
                                .last("LIMIT 1"));
        if (vo == null || vo.getConfigValue() == null) {
            return Optional.empty();
        }
        return Optional.of(vo.getConfigValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upsert(String tenantId, String configName, String fullConfigKey, String value) {
        if (value != null && value.length() > MAX_VALUE_LEN) {
            throw new IllegalArgumentException("config_value 长度不能超过 " + MAX_VALUE_LEN + "（sys_config 字段限制）");
        }
        String tid = normalizeTenant(tenantId);
        SysConfig existing =
                baseMapper.selectOne(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getConfigKey, fullConfigKey)
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
        row.setConfigKey(fullConfigKey);
        row.setConfigName(configName);
        row.setConfigValue(value == null ? "" : value);
        row.setConfigType(2);
        row.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
        row.setSort(0);
        baseMapper.insert(row);
    }

    @Override
    public List<PluginSysConfigRow> listForPlugin(String tenantId, String pluginId) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        String tid = normalizeTenant(tenantId);
        String prefix = PluginSysConfigKeys.keyPrefix(pluginId);
        List<SysConfigVo> vos =
                baseMapper.selectVoList(
                        new LambdaQueryWrapper<SysConfig>()
                                .eq(SysConfig::getTenantId, tid)
                                .likeRight(SysConfig::getConfigKey, prefix)
                                .orderByAsc(SysConfig::getConfigKey));
        List<PluginSysConfigRow> out = new ArrayList<>(vos.size());
        for (SysConfigVo vo : vos) {
            String full = vo.getConfigKey();
            String suffix = PluginSysConfigKeys.extractSuffix(pluginId, full);
            out.add(
                    new PluginSysConfigRow(
                            full,
                            suffix,
                            vo.getConfigName() != null ? vo.getConfigName() : suffix,
                            vo.getConfigValue() != null ? vo.getConfigValue() : ""));
        }
        return out;
    }

    private static String normalizeTenant(String tenantId) {
        return tenantId != null && !tenantId.isBlank() ? tenantId.trim() : "000000";
    }
}
