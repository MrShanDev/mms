package com.sxpcwlkj.plugin.ops;

import java.util.List;
import java.util.Optional;

/**
 * 宿主侧为插件提供的运维能力（由 {@code mms-plugin-server} 注册 Bean；插件内通过
 * {@code ObjectProvider<PluginHostOpsSupport>} 获取，可能为空，例如未引入 {@code mms-plugin-server} 的进程）。
 * <p>
 * 典型用途：检测某插件是否已加载、反射调用已加载插件 {@link com.sxpcwlkj.plugin.MmsPlugin} 上的公有实例方法、
 * 在企业微信机器人插件已加载时发送 Markdown 告警。
 */
public interface PluginHostOpsSupport {

    /**
     * 当前 JVM 是否已加载指定 {@code pluginId}（以宿主 {@code listManifests} 为准）。
     */
    boolean isPluginRuntimeLoaded(String pluginId);

    /**
     * 若 {@code mms.plugin.wechat-bot} 已加载且已配置 Webhook，则向其发送 Markdown；任何失败仅打日志，不抛异常。
     */
    void tryNotifyWechatMarkdown(String markdown);

    /**
     * 与宿主 HTTP 运维反射等价：在已加载插件的 {@link com.sxpcwlkj.plugin.MmsPlugin} 实现类上查找匹配参数个数的公有实例方法并调用。
     * <p>
     * 插件未加载、多版本歧义、无匹配方法或调用抛错时返回 {@link Optional#empty()}（错误会写入宿主日志）。
     */
    Optional<Object> tryInvokeLoadedPluginMethod(
            String pluginId, String versionOrNull, String methodName, List<?> args);
}
