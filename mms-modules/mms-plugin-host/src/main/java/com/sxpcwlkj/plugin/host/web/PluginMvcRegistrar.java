package com.sxpcwlkj.plugin.host.web;

import com.sxpcwlkj.plugin.PluginController;
import com.sxpcwlkj.plugin.PluginDescriptor;
import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.host.internal.PluginMdc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 扫描插件 ClassLoader 中带 {@link PluginController} 的类并注册路由。
 * <p>卸载顺序：全量重载或卸载时由 {@link com.sxpcwlkj.plugin.host.PluginLifecycleManager} 先 {@link #unregister}，
 * 再调用插件 {@link com.sxpcwlkj.plugin.MmsPlugin#onUnload} 与关闭 ClassLoader（见 {@code LoadedPluginInstance#close}）。</p>
 */
@RequiredArgsConstructor
@Slf4j
public class PluginMvcRegistrar {

    private final PluginMvcRegistry registry;

    public void unregister(String pluginId) {
        registry.unregister(pluginId);
    }

    public void registerMvc(PluginDescriptor descriptor, URLClassLoader ucl) throws Exception {
        Runnable popMdc = PluginMdc.pushPluginContext(descriptor.getId(), descriptor.getVersion());
        try {
            registerMvcInner(descriptor, ucl);
        } catch (Exception ex) {
            log.error("插件 HOST_MVC 路由注册失败: {}@{}", descriptor.getId(), descriptor.getVersion(), ex);
            throw ex;
        } finally {
            popMdc.run();
        }
    }

    private void registerMvcInner(PluginDescriptor descriptor, URLClassLoader ucl) throws Exception {
        String entry = descriptor.getEntryClass();
        if (entry == null || entry.isBlank()) {
            log.info("插件 HOST_MVC 跳过扫描（未配置 entryClass）: {}@{}", descriptor.getId(), descriptor.getVersion());
            return;
        }
        Class<?> entryClass = Class.forName(entry.trim(), false, ucl);
        if (entryClass.getPackage() == null) {
            throw new PluginException("entryClass 不能位于默认包");
        }
        String basePackage = entryClass.getPackageName();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.setResourceLoader(new PathMatchingResourcePatternResolver(ucl));
        scanner.addIncludeFilter(new AnnotationTypeFilter(PluginController.class, false, false));
        Set<BeanDefinition> defs = scanner.findCandidateComponents(basePackage);

        String pid = descriptor.getId();
        String pver = descriptor.getVersion();
        List<PluginMvcRegistry.RouteRecord> routes = new ArrayList<>();
        for (BeanDefinition def : defs) {
            Class<?> clazz = Class.forName(def.getBeanClassName(), false, ucl);
            if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
                continue;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String classPath = normalizeRawPath(extractClassLevelPath(clazz));
            for (Method m : clazz.getDeclaredMethods()) {
                if (Modifier.isStatic(m.getModifiers())) {
                    continue;
                }
                collectMethodRoutes(pid, pver, instance, m, classPath, routes);
            }
        }
        registry.registerRoutes(descriptor.getId(), routes);
        log.info("插件 HOST_MVC 路由已注册: {} 条", routes.size());
    }

    private static void collectMethodRoutes(
            String pluginId,
            String pluginVersion,
            Object instance,
            Method m,
            String classPath,
            List<PluginMvcRegistry.RouteRecord> routes) {
        boolean hasShortcut = AnnotatedElementUtils.hasAnnotation(m, GetMapping.class)
                || AnnotatedElementUtils.hasAnnotation(m, PostMapping.class)
                || AnnotatedElementUtils.hasAnnotation(m, PutMapping.class)
                || AnnotatedElementUtils.hasAnnotation(m, DeleteMapping.class)
                || AnnotatedElementUtils.hasAnnotation(m, PatchMapping.class);

        if (!hasShortcut) {
            RequestMapping mr = AnnotatedElementUtils.findMergedAnnotation(m, RequestMapping.class);
            if (mr != null && (mr.path().length > 0 || mr.value().length > 0)) {
                RequestMethod[] ms = mr.method();
                List<String> httpMethods;
                if (ms.length == 0) {
                    httpMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
                } else {
                    httpMethods = Arrays.stream(ms).map(Enum::name).toList();
                }
                for (String p : firstPaths(mr.path(), mr.value())) {
                    PluginMvcRouteTemplate tmpl = PluginMvcRouteTemplate.parse(join(classPath, p));
                    for (String hm : httpMethods) {
                        routes.add(new PluginMvcRegistry.RouteRecord(
                                pluginId, pluginVersion, hm, tmpl, instance, m));
                    }
                }
                return;
            }
        }

        GetMapping gm = AnnotatedElementUtils.findMergedAnnotation(m, GetMapping.class);
        if (gm != null) {
            for (String p : firstPaths(gm.path(), gm.value())) {
                routes.add(new PluginMvcRegistry.RouteRecord(
                        pluginId,
                        pluginVersion,
                        "GET",
                        PluginMvcRouteTemplate.parse(join(classPath, p)),
                        instance,
                        m));
            }
            return;
        }
        PostMapping pm = AnnotatedElementUtils.findMergedAnnotation(m, PostMapping.class);
        if (pm != null) {
            for (String p : firstPaths(pm.path(), pm.value())) {
                routes.add(new PluginMvcRegistry.RouteRecord(
                        pluginId,
                        pluginVersion,
                        "POST",
                        PluginMvcRouteTemplate.parse(join(classPath, p)),
                        instance,
                        m));
            }
            return;
        }
        PutMapping put = AnnotatedElementUtils.findMergedAnnotation(m, PutMapping.class);
        if (put != null) {
            for (String p : firstPaths(put.path(), put.value())) {
                routes.add(new PluginMvcRegistry.RouteRecord(
                        pluginId,
                        pluginVersion,
                        "PUT",
                        PluginMvcRouteTemplate.parse(join(classPath, p)),
                        instance,
                        m));
            }
            return;
        }
        DeleteMapping dm = AnnotatedElementUtils.findMergedAnnotation(m, DeleteMapping.class);
        if (dm != null) {
            for (String p : firstPaths(dm.path(), dm.value())) {
                routes.add(new PluginMvcRegistry.RouteRecord(
                        pluginId,
                        pluginVersion,
                        "DELETE",
                        PluginMvcRouteTemplate.parse(join(classPath, p)),
                        instance,
                        m));
            }
            return;
        }
        PatchMapping ptm = AnnotatedElementUtils.findMergedAnnotation(m, PatchMapping.class);
        if (ptm != null) {
            for (String p : firstPaths(ptm.path(), ptm.value())) {
                routes.add(new PluginMvcRegistry.RouteRecord(
                        pluginId,
                        pluginVersion,
                        "PATCH",
                        PluginMvcRouteTemplate.parse(join(classPath, p)),
                        instance,
                        m));
            }
        }
    }

    private static List<String> firstPaths(String[] path, String[] value) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        if (path != null) {
            for (String s : path) {
                if (s != null && !s.isBlank()) {
                    set.add(s);
                }
            }
        }
        if (value != null) {
            for (String s : value) {
                if (s != null && !s.isBlank()) {
                    set.add(s);
                }
            }
        }
        if (set.isEmpty()) {
            return List.of("");
        }
        return List.copyOf(set);
    }

    private static String extractClassLevelPath(Class<?> c) {
        RequestMapping rm = AnnotatedElementUtils.findMergedAnnotation(c, RequestMapping.class);
        if (rm == null) {
            return "";
        }
        if (rm.path().length > 0) {
            return rm.path()[0];
        }
        if (rm.value().length > 0) {
            return rm.value()[0];
        }
        return "";
    }

    private static String join(String classPath, String methodPath) {
        String a = normalizeRawPath(classPath);
        String b = normalizeRawPath(methodPath);
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        return a + "/" + b;
    }

    /** 去掉首尾 /，不改动大小写（保留 {var}）。 */
    private static String normalizeRawPath(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        String s = raw.trim().replace('\\', '/');
        while (s.startsWith("/")) {
            s = s.substring(1);
        }
        while (s.endsWith("/") && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
