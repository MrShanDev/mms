package com.sxpcwlkj.plugin.wechatbot;

import com.sxpcwlkj.plugin.PluginRuntimeContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Optional;

/**
 * 绑定已加载的企微机器人插件为 {@link WechatBotMessaging}（进程内对等调用）。
 */
public final class WechatBotPeers {

    /** 默认插件 id，与 {@code META-INF/mms/plugin.json} 一致。 */
    public static final String DEFAULT_PLUGIN_ID = "mms.plugin.wechat-bot";

    private WechatBotPeers() {}

    public static WechatBotMessaging bind(PluginRuntimeContext ctx) {
        return bind(ctx, DEFAULT_PLUGIN_ID);
    }

    public static WechatBotMessaging bind(PluginRuntimeContext ctx, String pluginId) {
        Objects.requireNonNull(ctx, "ctx");
        Objects.requireNonNull(pluginId, "pluginId");
        InvocationHandler h = (proxy, method, args) -> handle(ctx, pluginId, proxy, method, args);
        return (WechatBotMessaging)
                Proxy.newProxyInstance(
                        WechatBotMessaging.class.getClassLoader(),
                        new Class<?>[] {WechatBotMessaging.class},
                        h);
    }

    private static Object handle(
            PluginRuntimeContext ctx, String pluginId, Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return switch (method.getName()) {
                case "equals" -> proxy == args[0];
                case "hashCode" -> System.identityHashCode(proxy);
                case "toString" -> "WechatBotMessaging{pluginId=" + pluginId + "}";
                default -> method.invoke(proxy, args);
            };
        }
        Object[] invokeArgs = args != null ? args : new Object[0];
        Optional<Object> out = ctx.tryInvokePeerPlugin(pluginId, method.getName(), invokeArgs);
        if (out.isEmpty()) {
            throw new IllegalStateException(
                    "企微机器人调用失败（插件未加载、方法不匹配或返回 null）: " + pluginId + "#" + method.getName());
        }
        return out.get();
    }
}
