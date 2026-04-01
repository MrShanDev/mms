package com.sxpcwlkj.plugin.host.web;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 已加载插件的 HTTP 路由表（HTTP 方法 + 路径模板匹配）。
 */
public final class PluginMvcRegistry {

    public record RouteRecord(
            String pluginId,
            String pluginVersion,
            String httpMethod,
            PluginMvcRouteTemplate template,
            Object controller,
            Method handlerMethod) {}

    public record RouteMatch(RouteRecord record, Map<String, String> pathVariables) {}

    private final Map<String, List<RouteRecord>> byPluginId = new ConcurrentHashMap<>();

    public void registerRoutes(String pluginId, List<RouteRecord> routes) {
        byPluginId.put(pluginId, List.copyOf(routes));
    }

    public void unregister(String pluginId) {
        byPluginId.remove(pluginId);
    }

    public RouteMatch match(String pluginId, String httpMethod, List<String> requestSegments) {
        List<RouteRecord> list = byPluginId.get(pluginId);
        if (list == null) {
            return null;
        }
        for (RouteRecord r : list) {
            if (!r.httpMethod().equalsIgnoreCase(httpMethod)) {
                continue;
            }
            var vars = r.template().match(requestSegments);
            if (vars.isPresent()) {
                return new RouteMatch(r, vars.get());
            }
        }
        return null;
    }
}
