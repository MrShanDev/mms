package com.sxpcwlkj.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginMenuBootstrapDef;
import com.sxpcwlkj.plugin.PluginMenuItemDef;
import com.sxpcwlkj.system.entity.SysDict;
import com.sxpcwlkj.system.entity.SysDictData;
import com.sxpcwlkj.system.entity.SysFunction;
import com.sxpcwlkj.system.entity.SysTenant;
import com.sxpcwlkj.system.mapper.PluginOwnedMenuTenantFreeMapper;
import com.sxpcwlkj.system.mapper.SysDictDataMapper;
import com.sxpcwlkj.system.mapper.SysDictMapper;
import com.sxpcwlkj.system.mapper.SysFunctionMapper;
import com.sxpcwlkj.system.mapper.SysRoleFunctionMapper;
import com.sxpcwlkj.system.mapper.SysTenantMapper;
import com.sxpcwlkj.system.service.PluginOwnedMenuBootstrapService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 与 {@link SysPluginVersionServiceImpl#seedPluginSysConfigKeysIfAbsent} 相同租户集合，保证安装体验一致。
 */
@Service
@RequiredArgsConstructor
public class PluginOwnedMenuBootstrapServiceImpl implements PluginOwnedMenuBootstrapService {

    private static final Logger log = LoggerFactory.getLogger(PluginOwnedMenuBootstrapServiceImpl.class);

    private final PluginOwnedMenuTenantFreeMapper pluginOwnedMenuTenantFreeMapper;
    private final SysFunctionMapper sysFunctionMapper;
    private final SysRoleFunctionMapper sysRoleFunctionMapper;
    private final SysTenantMapper sysTenantMapper;
    private final SysDictMapper sysDictMapper;
    private final SysDictDataMapper sysDictDataMapper;

    static String remarkFor(String pluginId) {
        return "plugin:" + pluginId.trim();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOnInstall(PluginDescriptor descriptor) {
        if (descriptor == null || descriptor.getId() == null || descriptor.getId().isBlank()) {
            return;
        }
        PluginMenuBootstrapDef boot = descriptor.getMenuBootstrap();
        if (boot == null || boot.getItems() == null || boot.getItems().isEmpty()) {
            return;
        }
        String pid = descriptor.getId().trim();
        String remark = remarkFor(pid);
        Set<String> tenantIds = collectTenantIds();
        Date now = new Date();
        for (String tid : tenantIds) {
            for (PluginMenuItemDef item : boot.getItems()) {
                if (item == null || item.getId() == null || item.getId().isBlank()) {
                    continue;
                }
                if (item.getParentId() == null || item.getParentId().isBlank()) {
                    log.warn("plugin menuBootstrap 跳过缺少 parentId 的项 pluginId={} id={}", pid, item.getId());
                    continue;
                }
                int menuType = item.getType() != null ? item.getType() : 1;
                if (menuType == 1 && (item.getPath() == null || item.getPath().isBlank())) {
                    log.warn(
                            "plugin menuBootstrap 跳过 type=1 但缺少 path 的项（管理端动态路由无法注册）pluginId={} id={}",
                            pid,
                            item.getId());
                    continue;
                }
                upsertOneMenu(tid, remark, item, now);
            }
        }
    }

    private void upsertOneMenu(String tenantId, String remark, PluginMenuItemDef item, Date now) {
        String id = item.getId().trim();
        SysFunction existing = pluginOwnedMenuTenantFreeMapper.selectOneIdTenant(id, tenantId);
        if (existing != null) {
            String er = existing.getRemark();
            if (er != null && er.startsWith("plugin:") && !remark.equals(er)) {
                log.warn(
                        "sys_function id={} 已存在且归属其它插件 remark={}，跳过写入（当前插件 remark={}）",
                        id,
                        er,
                        remark);
                return;
            }
            if (er == null || er.isBlank()) {
                log.warn(
                        "sys_function id={} 已存在且无插件归属备注，跳过覆盖（避免误改手工菜单）",
                        id);
                return;
            }
        }
        SysFunction row = toEntity(tenantId, remark, item, now, existing);
        if (existing == null) {
            pluginOwnedMenuTenantFreeMapper.insertMenu(row);
        } else {
            pluginOwnedMenuTenantFreeMapper.updateMenu(row);
        }
    }

    /**
     * 与 {@link com.sxpcwlkj.system.service.impl.SysUserServiceImpl} 一致：{@code meta.isHide = (visible == 1)}。
     * 目录菜单（type=1）默认展示；未声明 {@code visible} 或误写 {@code 1}（易与「启用」混淆）时统一为 {@code -1}。
     */
    private static int resolveVisibleForBootstrap(PluginMenuItemDef item) {
        int type = item.getType() != null ? item.getType() : 1;
        Integer raw = item.getVisible();
        if (raw == null) {
            return -1;
        }
        if (type == 1 && raw == 1) {
            return -1;
        }
        return raw;
    }

    private static SysFunction toEntity(
            String tenantId, String remark, PluginMenuItemDef item, Date now, SysFunction existing) {
        SysFunction f = new SysFunction();
        f.setId(item.getId().trim());
        f.setTenantId(tenantId);
        f.setRemark(remark);
        f.setParentId(parseParentId(item.getParentId()));
        f.setPath(nullToEmpty(item.getPath()));
        f.setName(item.getName());
        f.setComponent(nullToEmpty(item.getComponent()));
        f.setComponentName(item.getComponentName());
        f.setLanguageCode(item.getLanguageCode() != null ? item.getLanguageCode() : item.getName());
        f.setPermission(item.getPermission());
        f.setType(item.getType() != null ? item.getType() : 1);
        f.setSort(item.getSort() != null ? item.getSort() : 0);
        f.setIcon(item.getIcon());
        f.setStatus(item.getStatus() != null ? item.getStatus() : 1);
        f.setVisible(resolveVisibleForBootstrap(item));
        f.setIsIframe(item.getIsIframe() != null ? item.getIsIframe() : -1);
        f.setIsOpenLink(item.getIsOpenLink() != null ? item.getIsOpenLink() : -1);
        f.setIsLink(item.getIsLink() != null ? item.getIsLink() : "");
        f.setKeepAlive(item.getKeepAlive() != null ? item.getKeepAlive() : -1);
        f.setAlwaysShow(item.getAlwaysShow() != null ? item.getAlwaysShow() : -1);
        f.setIsFast(item.getIsFast() != null ? item.getIsFast() : 0);
        f.setUpdatedTime(now);
        f.setUpdatedBy(1L);
        if (existing == null) {
            f.setCreatedTime(now);
            f.setCreatedBy(1L);
            f.setRevision(1L);
        } else {
            f.setCreatedTime(existing.getCreatedTime());
            f.setCreatedBy(existing.getCreatedBy());
            f.setRevision(existing.getRevision());
        }
        return f;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static Long parseParentId(String raw) {
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Set<String> collectTenantIds() {
        Set<String> tenantIds = new LinkedHashSet<>();
        tenantIds.add("000000");
        List<SysTenant> tenants = sysTenantMapper.selectList(new LambdaQueryWrapper<>());
        if (tenants != null) {
            for (SysTenant st : tenants) {
                if (st != null && st.getTenantId() != null && !st.getTenantId().isBlank()) {
                    tenantIds.add(st.getTenantId().trim());
                }
            }
        }
        return tenantIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAllForPlugin(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return;
        }
        String remark = remarkFor(pluginId.trim());
        for (String tid : collectTenantIds()) {
            List<String> ids = sysFunctionMapper.selectIdsByRemarkAndTenant(remark, tid);
            if (ids == null || ids.isEmpty()) {
                continue;
            }
            for (String functionId : ids) {
                sysRoleFunctionMapper.deleteByFunctionIdAndTenant(functionId, tid);
            }
            sysFunctionMapper.deleteByRemarkAndTenant(remark, tid);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSysFunctionRowsByIds(List<String> sysFunctionIds) {
        if (sysFunctionIds == null || sysFunctionIds.isEmpty()) {
            return;
        }
        for (String fid : sysFunctionIds) {
            if (fid == null || fid.isBlank()) {
                continue;
            }
            String id = fid.trim();
            List<String> tids = sysFunctionMapper.selectDistinctTenantIdsByFunctionId(id);
            if (tids == null || tids.isEmpty()) {
                continue;
            }
            for (String tid : tids) {
                if (tid == null || tid.isBlank()) {
                    continue;
                }
                sysRoleFunctionMapper.deleteByFunctionIdAndTenant(id, tid.trim());
                sysFunctionMapper.deleteByIdAndTenant(id, tid.trim());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSysDictDataRowsByIds(List<String> dictDataIds) {
        if (dictDataIds == null || dictDataIds.isEmpty()) {
            return;
        }
        for (String raw : dictDataIds) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            String id = raw.trim();
            sysDictDataMapper.delete(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getId, id));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSysDictRowsByIds(List<String> dictIds) {
        if (dictIds == null || dictIds.isEmpty()) {
            return;
        }
        for (String raw : dictIds) {
            if (raw == null || raw.isBlank()) {
                continue;
            }
            String id = raw.trim();
            sysDictMapper.delete(new LambdaQueryWrapper<SysDict>().eq(SysDict::getId, id));
        }
    }
}
