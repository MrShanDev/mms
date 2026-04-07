package com.sxpcwlkj.plugin.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.plugin.host.PluginManifestView;
import com.sxpcwlkj.plugin.ops.PluginHostOpsSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * {@link PluginHostOpsSupport} 的默认实现（依赖 {@link PluginLifecycleManager}）。
 */
@Slf4j
@Component
@ConditionalOnBean(PluginLifecycleManager.class)
public class PluginHostOpsSupportImpl implements PluginHostOpsSupport {

    private static final String WECHAT_BOT_PLUGIN_ID = "mms.plugin.wechat-bot";

    private final PluginLifecycleManager pluginLifecycleManager;
    private final ObjectMapper objectMapper;

    public PluginHostOpsSupportImpl(PluginLifecycleManager pluginLifecycleManager, ObjectMapper objectMapper) {
        this.pluginLifecycleManager = pluginLifecycleManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean isPluginRuntimeLoaded(String pluginId) {
        if (pluginId == null || pluginId.isBlank()) {
            return false;
        }
        String id = pluginId.trim();
        for (PluginManifestView m : pluginLifecycleManager.listManifests()) {
            if (id.equals(m.id())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tryNotifyWechatMarkdown(String markdown) {
        if (markdown == null) {
            markdown = "";
        }
        if (!isPluginRuntimeLoaded(WECHAT_BOT_PLUGIN_ID)) {
            log.debug("企微机器人插件未加载，跳过 Markdown 通知");
            return;
        }
        try {
            pluginLifecycleManager.invokePluginMethodForOps(
                    WECHAT_BOT_PLUGIN_ID,
                    null,
                    "sendMarkdown",
                    List.of(markdown),
                    objectMapper);
        } catch (Exception e) {
            log.warn("企业微信 Markdown 通知失败: {}", e.getMessage());
        }
    }

    @Override
    public Optional<Object> tryInvokeLoadedPluginMethod(
            String pluginId, String versionOrNull, String methodName, List<?> args) {
        if (pluginId == null || pluginId.isBlank() || methodName == null || methodName.isBlank()) {
            return Optional.empty();
        }
        try {
            return pluginLifecycleManager.invokePluginMethodForOps(
                    pluginId.trim(),
                    versionOrNull,
                    methodName.trim(),
                    args == null ? List.of() : args,
                    objectMapper);
        } catch (IllegalArgumentException ex) {
            log.debug(
                    "反射调用插件方法跳过: pluginId={} method={} — {}",
                    pluginId,
                    methodName,
                    ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.warn("反射调用插件方法失败: pluginId={} method={} — {}", pluginId, methodName, ex.getMessage());
            return Optional.empty();
        }
    }
}
