package com.sxpcwlkj.plugin.host.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.plugin.MmsPlugin;
import com.sxpcwlkj.plugin.PluginDescriptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 在已加载的 {@link MmsPlugin} 上按方法名与参数个数反射调用（公有实例方法）。
 */
public final class PluginReflectionSupport {

    private PluginReflectionSupport() {
    }

    /**
     * @param versionOrNull 为 null 或空白时，若仅有一个匹配 pluginId 的已加载版本则选用；多版本则抛 {@link IllegalArgumentException}
     */
    public static LoadedPluginInstance resolveTarget(
            Map<String, LoadedPluginInstance> loaded, String pluginId, String versionOrNull) {
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        if (versionOrNull != null && !versionOrNull.isBlank()) {
            LoadedPluginInstance one = loaded.get(pluginId.trim() + "@" + versionOrNull.trim());
            if (one == null) {
                throw new IllegalArgumentException("未找到已加载插件: " + pluginId + "@" + versionOrNull);
            }
            return one;
        }
        List<LoadedPluginInstance> matches = new ArrayList<>();
        String prefix = pluginId.trim() + "@";
        for (Map.Entry<String, LoadedPluginInstance> e : loaded.entrySet()) {
            if (e.getKey().regionMatches(true, 0, prefix, 0, prefix.length())) {
                matches.add(e.getValue());
            }
        }
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("未找到已加载插件: " + pluginId);
        }
        if (matches.size() > 1) {
            throw new IllegalArgumentException("插件 " + pluginId + " 存在多个已加载版本，请指定 version");
        }
        return matches.get(0);
    }

    public static Optional<Object> invokeOnLoaded(
            Map<String, LoadedPluginInstance> loaded,
            String pluginId,
            String versionOrNull,
            String methodName,
            Object[] args,
            ObjectMapper jsonCoerceMapper) {
        if (methodName == null || methodName.isBlank()) {
            throw new IllegalArgumentException("methodName 不能为空");
        }
        LoadedPluginInstance target = resolveTarget(loaded, pluginId, versionOrNull);
        return invokeFirstEntryPoint(target, methodName.trim(), args != null ? args : new Object[0], jsonCoerceMapper);
    }

    private static Optional<Object> invokeFirstEntryPoint(
            LoadedPluginInstance loaded, String methodName, Object[] args, ObjectMapper mapper) {
        PluginDescriptor d = loaded.getDescriptor();
        ClassLoader ucl = loaded.getClassLoader();
        ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
        Runnable popMdc = PluginMdc.pushPluginContext(d.getId(), d.getVersion());
        Thread.currentThread().setContextClassLoader(ucl);
        try {
            for (MmsPlugin p : loaded.getEntryPoints()) {
                Method m = findPublicMethod(p.getClass(), methodName, args.length);
                if (m == null) {
                    continue;
                }
                m.setAccessible(true);
                Object[] invokeArgs = args;
                if (mapper != null && args.length > 0) {
                    invokeArgs = coerceArgs(mapper, m, args);
                }
                Object out = m.invoke(p, invokeArgs);
                return Optional.ofNullable(out);
            }
            return Optional.empty();
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "插件方法调用失败: " + d.getId() + "@" + d.getVersion() + "#" + methodName + " — " + ex.getMessage(),
                    ex);
        } finally {
            Thread.currentThread().setContextClassLoader(oldCl);
            popMdc.run();
        }
    }

    private static Object[] coerceArgs(ObjectMapper mapper, Method m, Object[] raw) {
        Class<?>[] types = m.getParameterTypes();
        Object[] out = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Object v = raw[i];
            if (v == null || types[i].isInstance(v)) {
                out[i] = v;
            } else {
                out[i] = mapper.convertValue(v, types[i]);
            }
        }
        return out;
    }

    static Method findPublicMethod(Class<?> clazz, String name, int paramCount) {
        for (Method m : clazz.getMethods()) {
            if (m.getDeclaringClass() == Object.class) {
                continue;
            }
            if (!m.getName().equals(name)) {
                continue;
            }
            if (Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            if (m.getParameterCount() != paramCount) {
                continue;
            }
            return m;
        }
        return null;
    }
}
