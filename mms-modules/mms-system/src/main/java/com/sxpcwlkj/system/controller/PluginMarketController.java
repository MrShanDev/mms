package com.sxpcwlkj.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.system.entity.bo.PluginMarketRemoveBo;
import com.sxpcwlkj.system.entity.bo.PluginMarketSysConfigSaveBo;
import com.sxpcwlkj.system.entity.vo.PluginMarketCardVo;
import com.sxpcwlkj.system.entity.vo.PluginMarketSysConfigVo;
import com.sxpcwlkj.system.service.PluginMarketSysConfigService;
import com.sxpcwlkj.system.service.SysPluginMarketService;
import com.sxpcwlkj.system.service.SysPluginVersionService;
import com.sxpcwlkj.system.service.impl.PluginHostDbBridgeImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 插件市场：合并 sys_plugins 元数据与宿主运行时状态（超级管理员与管理员）。
 */
@Tag(name = "系统管理模块-插件市场", description = "插件市场卡片数据")
@RequestMapping("system/pluginMarket")
@RestController
@RequiredArgsConstructor
public class PluginMarketController {

    private final SysPluginMarketService sysPluginMarketService;
    private final SysPluginVersionService sysPluginVersionService;
    private final PluginMarketSysConfigService pluginMarketSysConfigService;
    private final PluginLifecycleManager pluginLifecycleManager;

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @GetMapping("/cards")
    public R<List<PluginMarketCardVo>> cards() {
        return R.success(sysPluginMarketService.listMarketCards());
    }

    /**
     * 移除库表市场登记与版本记录并重载；不删除磁盘（与 {@link #purge} 区分）。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/removeCatalog")
    public R<Void> removeCatalog(@RequestBody PluginMarketRemoveBo body) {
        if (body == null || body.getPluginId() == null || body.getPluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        try {
            sysPluginMarketService.removeCatalogEntry(body.getPluginId());
            pluginLifecycleManager.reload();
            return R.success();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    /**
     * 停用：将该插件所有版本的「激活」标记清零并重载；不删磁盘、不删市场登记，便于稍后重新激活或升级。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/deactivate")
    public R<Void> pluginDeactivate(@RequestBody PluginMarketRemoveBo body) {
        if (body == null || body.getPluginId() == null || body.getPluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        try {
            sysPluginVersionService.deactivateAllVersionsForPlugin(
                    body.getPluginId(), PluginHostDbBridgeImpl.PLUGIN_REGISTRY_TENANT);
            pluginLifecycleManager.reload();
            return R.success();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    /**
     * 删除：清空该插件磁盘安装目录与库表（版本 + 市场行）后重载。
     */
    /**
     * 插件专属 sys_config（键前缀 {@code mms.plugin.{pluginId}.}），按当前登录租户读取。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @GetMapping("/pluginSysConfig")
    public R<java.util.List<PluginMarketSysConfigVo>> pluginSysConfig(@RequestParam String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        String tenant = LoginObject.getLoginTenant();
        if (tenant == null || tenant.isBlank()) {
            tenant = PluginHostDbBridgeImpl.PLUGIN_REGISTRY_TENANT;
        }
        return R.success(pluginMarketSysConfigService.listForPlugin(pluginId.trim(), tenant));
    }

    /**
     * 批量保存插件配置项（upsert）；不删除未出现在列表中的已有键，需另行清理接口时再加。
     */
    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/pluginSysConfig")
    public R<Void> savePluginSysConfig(@RequestBody PluginMarketSysConfigSaveBo body) {
        if (body == null || body.getPluginId() == null || body.getPluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        try {
            String tenant = LoginObject.getLoginTenant();
            if (tenant == null || tenant.isBlank()) {
                tenant = PluginHostDbBridgeImpl.PLUGIN_REGISTRY_TENANT;
            }
            pluginMarketSysConfigService.saveForPlugin(body, tenant);
            return R.success();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    @SaCheckRole(value = {"super_admin", "admin"}, mode = SaMode.OR)
    @PostMapping("/purge")
    public R<Void> purge(@RequestBody PluginMarketRemoveBo body) {
        if (body == null || body.getPluginId() == null || body.getPluginId().isBlank()) {
            return R.fail("pluginId 不能为空");
        }
        try {
            sysPluginMarketService.purgePluginDiskAndCatalog(body.getPluginId());
            pluginLifecycleManager.reload();
            return R.success();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        } catch (IOException ex) {
            return R.fail("删除磁盘失败: " + ex.getMessage());
        }
    }
}
