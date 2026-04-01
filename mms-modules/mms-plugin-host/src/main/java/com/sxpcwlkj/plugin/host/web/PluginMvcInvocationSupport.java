package com.sxpcwlkj.plugin.host.web;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 将 {@link HttpServletRequest}、路径变量按声明顺序绑定到插件控制器方法形参。
 */
public final class PluginMvcInvocationSupport {

    private PluginMvcInvocationSupport() {
    }

    public static Object[] buildArgs(
            Method m, HttpServletRequest request, Map<String, String> pathVars, List<String> variableNamesInOrder) {
        Class<?>[] types = m.getParameterTypes();
        Object[] args = new Object[types.length];
        int pv = 0;
        for (int i = 0; i < types.length; i++) {
            Class<?> t = types[i];
            if (t == HttpServletRequest.class) {
                args[i] = request;
            } else if (t == String.class) {
                if (pv >= variableNamesInOrder.size()) {
                    throw new IllegalArgumentException(
                            "方法 " + m.getName() + " 形参多于路径变量模板，缺少 {var} 声明");
                }
                String key = variableNamesInOrder.get(pv++);
                args[i] = pathVars.get(key);
            } else if (t == long.class || t == Long.class) {
                if (pv >= variableNamesInOrder.size()) {
                    throw new IllegalArgumentException("方法 " + m.getName() + " 缺少路径变量");
                }
                String key = variableNamesInOrder.get(pv++);
                args[i] = Long.parseLong(pathVars.get(key));
            } else if (t == int.class || t == Integer.class) {
                if (pv >= variableNamesInOrder.size()) {
                    throw new IllegalArgumentException("方法 " + m.getName() + " 缺少路径变量");
                }
                String key = variableNamesInOrder.get(pv++);
                args[i] = Integer.parseInt(pathVars.get(key));
            } else {
                throw new IllegalArgumentException("不支持的形参类型: " + t.getName());
            }
        }
        if (pv != variableNamesInOrder.size()) {
            throw new IllegalArgumentException("路径变量个数与方法可绑定形参不一致");
        }
        return args;
    }
}
