package com.sxpcwlkj.system.service.impl;

import com.sxpcwlkj.plugin.PluginSysConfigKeys;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.PluginSysConfigRow;
import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigItemBo;
import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigSaveBo;
import com.sxpcwlkj.system.entity.vo.PluginMarketSysConfigVo;
import com.sxpcwlkj.system.service.PluginMarketSysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PluginMarketSysConfigServiceImpl implements PluginMarketSysConfigService {

    private final PluginSysConfigOperations pluginSysConfigOperations;

    @Override
    public List<PluginMarketSysConfigVo> listForPlugin(String pluginId, String tenantId) {
        PluginSysConfigKeys.validatePluginId(pluginId);
        String tid = normalizeTenant(tenantId);
        List<PluginSysConfigRow> rows = pluginSysConfigOperations.listForPlugin(tid, pluginId.trim());
        List<PluginMarketSysConfigVo> out = new ArrayList<>(rows.size());
        for (PluginSysConfigRow r : rows) {
            PluginMarketSysConfigVo vo = new PluginMarketSysConfigVo();
            vo.setFullConfigKey(r.fullConfigKey());
            vo.setKeySuffix(r.keySuffix());
            vo.setConfigName(r.configName());
            vo.setConfigValue(r.configValue());
            out.add(vo);
        }
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveForPlugin(PluginMarketSysConfigSaveBo body, String tenantId) {
        if (body == null || body.getPluginId() == null || body.getPluginId().isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        String pid = body.getPluginId().trim();
        PluginSysConfigKeys.validatePluginId(pid);
        String tid = normalizeTenant(tenantId);
        if (body.getItems() == null) {
            return;
        }
        for (PluginMarketSysConfigItemBo item : body.getItems()) {
            if (item == null || item.getKeySuffix() == null || item.getKeySuffix().isBlank()) {
                continue;
            }
            PluginSysConfigKeys.validateSuffix(item.getKeySuffix());
            String full = PluginSysConfigKeys.fullKey(pid, item.getKeySuffix().trim());
            String name =
                    item.getConfigName() != null && !item.getConfigName().isBlank()
                            ? item.getConfigName().trim()
                            : item.getKeySuffix().trim();
            String val = item.getConfigValue() != null ? item.getConfigValue() : "";
            pluginSysConfigOperations.upsert(tid, name, full, val);
        }
    }

    private static String normalizeTenant(String tenantId) {
        return tenantId != null && !tenantId.isBlank() ? tenantId.trim() : "000000";
    }
}
