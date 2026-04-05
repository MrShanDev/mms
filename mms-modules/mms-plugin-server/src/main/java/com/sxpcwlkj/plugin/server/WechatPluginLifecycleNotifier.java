package com.sxpcwlkj.plugin.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.host.PluginLifecycleEvent;
import com.sxpcwlkj.plugin.host.PluginLifecycleEventListener;
import com.sxpcwlkj.plugin.host.PluginLifecycleManager;
import com.sxpcwlkj.plugin.host.PluginManifestView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 将插件宿主生命周期事件交给企业微信机器人插件处理：Webhook 与开关均在插件内默认 JSON / 插件市场参数中配置，不依赖主应用 yml。
 */
@Slf4j
@Component
public class WechatPluginLifecycleNotifier implements PluginLifecycleEventListener {

    private static final String WECHAT_PLUGIN_ID = "mms.plugin.wechat-bot";

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    private final PluginLifecycleManager pluginLifecycleManager;
    private final ObjectMapper objectMapper;

    public WechatPluginLifecycleNotifier(PluginLifecycleManager pluginLifecycleManager, ObjectMapper objectMapper) {
        this.pluginLifecycleManager = pluginLifecycleManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onPluginLifecycleEvent(PluginLifecycleEvent event) {
        if (!isWechatPluginLoaded()) {
            return;
        }
        try {
            pluginLifecycleManager.invokePluginMethodForOps(
                    WECHAT_PLUGIN_ID,
                    null,
                    "sendPluginLifecycleMarkdown",
                    List.of(buildMarkdown(event)),
                    objectMapper);
        } catch (Exception e) {
            log.debug("企业微信插件 sendPluginLifecycleMarkdown 失败: {}", e.getMessage());
        }
    }

    private boolean isWechatPluginLoaded() {
        for (PluginManifestView m : pluginLifecycleManager.listManifests()) {
            if (WECHAT_PLUGIN_ID.equals(m.id())) {
                return true;
            }
        }
        return false;
    }

    private static String buildMarkdown(PluginLifecycleEvent e) {
        StringBuilder sb = new StringBuilder();
        sb.append("## ").append(escapeMd(e.type().title())).append("\n\n");
        sb.append("**插件坐标**：`").append(escapeMd(e.pluginKey())).append("`\n");
        sb.append("**时间**：").append(TIME_FMT.format(e.occurredAt())).append("\n");
        if (e.detailMessage() != null && !e.detailMessage().isBlank()) {
            sb.append("**详情**：").append(escapeMd(truncate(e.detailMessage(), 800))).append("\n");
        } else {
            sb.append("**详情**：—\n");
        }
        if (e.error() != null) {
            sb.append("**异常**：").append(escapeMd(shortError(e.error()))).append("\n");
        }
        return sb.toString();
    }

    private static String shortError(Throwable t) {
        String m = t.getMessage();
        if (m != null && !m.isBlank()) {
            return truncate(t.getClass().getSimpleName() + ": " + m, 800);
        }
        return t.getClass().getSimpleName();
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }

    private static String escapeMd(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
