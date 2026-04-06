package com.sxpcwlkj.system.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginSysConfigDef;
import com.sxpcwlkj.plugin.PluginSysConfigKeys;
import com.sxpcwlkj.plugin.PluginSysConfigOperations;
import com.sxpcwlkj.plugin.PluginSysConfigRow;
import com.sxpcwlkj.plugin.PluginSysConfigValueShapes;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigItemBo;
import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigSaveBo;
import com.sxpcwlkj.system.entity.vo.PluginMarketSysConfigVo;
import com.sxpcwlkj.system.service.PluginMarketSysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PluginMarketSysConfigServiceImpl implements PluginMarketSysConfigService {

    private static final ObjectMapper OM = new ObjectMapper();

    private final PluginSysConfigOperations pluginSysConfigOperations;
    private final PluginLifecycleManager pluginLifecycleManager;

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
        List<PluginSysConfigDef> schema = pluginLifecycleManager.resolveSysConfigSchema(pid);
        Set<String> existingKeys = pluginSysConfigOperations.listForPlugin(tid, pid).stream()
                .map(r -> r.keySuffix().trim())
                .collect(Collectors.toCollection(HashSet::new));
        Map<String, PluginSysConfigDef> schemaBySuffix = new HashMap<>();
        if (!schema.isEmpty()) {
            Set<String> allowed = new HashSet<>();
            for (PluginSysConfigDef def : schema) {
                if (def != null && def.getKeySuffix() != null && !def.getKeySuffix().isBlank()) {
                    String ks = def.getKeySuffix().trim();
                    allowed.add(ks);
                    schemaBySuffix.put(ks, def);
                }
            }
            for (PluginMarketSysConfigItemBo item : body.getItems()) {
                if (item == null || item.getKeySuffix() == null || item.getKeySuffix().isBlank()) {
                    continue;
                }
                String k = item.getKeySuffix().trim();
                if (!allowed.contains(k)) {
                    throw new IllegalArgumentException("配置键未在插件 plugin.json 的 sysConfig 中声明: " + k);
                }
            }
        } else {
            for (PluginMarketSysConfigItemBo item : body.getItems()) {
                if (item == null || item.getKeySuffix() == null || item.getKeySuffix().isBlank()) {
                    continue;
                }
                String k = item.getKeySuffix().trim();
                if (!existingKeys.contains(k)) {
                    throw new IllegalArgumentException(
                            "插件未在 plugin.json 声明 sysConfig 时，不可新增配置键（仅可更新已有项）: " + k);
                }
            }
        }
        for (PluginMarketSysConfigItemBo item : body.getItems()) {
            if (item == null || item.getKeySuffix() == null || item.getKeySuffix().isBlank()) {
                continue;
            }
            PluginSysConfigKeys.validateSuffix(item.getKeySuffix());
            String suf = item.getKeySuffix().trim();
            String name =
                    item.getConfigName() != null && !item.getConfigName().isBlank()
                            ? item.getConfigName().trim()
                            : suf;
            String val = item.getConfigValue() != null ? item.getConfigValue() : "";
            PluginSysConfigDef def = schemaBySuffix.isEmpty() ? null : schemaBySuffix.get(suf);
            validateStoredShape(def, suf, val);
            pluginSysConfigOperations.upsert(tid, pid, name, suf, val);
        }
    }

    private static void validateStoredShape(PluginSysConfigDef def, String keySuffix, String value) {
        if (def == null) {
            return;
        }
        String card = effectiveCardinality(def);
        if (value == null || value.isBlank()) {
            return;
        }
        if (PluginSysConfigValueShapes.SCALAR.equals(card)) {
            return;
        }
        try {
            JsonNode n = OM.readTree(value);
            if (PluginSysConfigValueShapes.LIST.equals(card) && !n.isArray()) {
                throw new IllegalArgumentException("配置项 " + keySuffix + " 须为 JSON 数组（见 plugin.json valueCardinality=list）");
            }
            if (PluginSysConfigValueShapes.OBJECT.equals(card) && !n.isObject()) {
                throw new IllegalArgumentException("配置项 " + keySuffix + " 须为 JSON 对象（见 plugin.json valueCardinality=object）");
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("配置项 " + keySuffix + " 须为合法 JSON: " + e.getMessage());
        }
    }

    private static String effectiveCardinality(PluginSysConfigDef def) {
        String vt = def.getValueType() == null ? "" : def.getValueType().trim().toLowerCase(Locale.ROOT);
        if ("multiselect".equals(vt)) {
            return PluginSysConfigValueShapes.LIST;
        }
        try {
            return PluginSysConfigValueShapes.normalizeDeclaredCardinality(def.getValueCardinality());
        } catch (PluginException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static String normalizeTenant(String tenantId) {
        return tenantId != null && !tenantId.isBlank() ? tenantId.trim() : "000000";
    }
}
