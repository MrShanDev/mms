package com.sxpcwlkj.plugin.host.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 将插件注册到宿主容器的 @RestController / @Controller 挂到主 {@link RequestMappingHandlerMapping}（与 mms-admin 同一 DispatcherServlet）。
 */
@Slf4j
public final class PluginWebMvcHandlerRegistrar {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final Map<String, List<RequestMappingInfo>> sessionToMappings = new ConcurrentHashMap<>();
    private final Method getMappingForMethod;

    public PluginWebMvcHandlerRegistrar(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        Method gm = ReflectionUtils.findMethod(
                RequestMappingHandlerMapping.class, "getMappingForMethod", Method.class, Class.class);
        if (gm == null) {
            throw new IllegalStateException("RequestMappingHandlerMapping.getMappingForMethod 不可用");
        }
        gm.setAccessible(true);
        this.getMappingForMethod = gm;
    }

    public void registerHandlerMethods(String loadSessionId, Object handlerBean) {
        if (handlerBean == null || loadSessionId == null || loadSessionId.isBlank()) {
            return;
        }
        Class<?> userType = ClassUtils.getUserClass(handlerBean);
        if (!isWebController(userType)) {
            return;
        }
        List<RequestMappingInfo> registered = new CopyOnWriteArrayList<>();
        for (Method method : ReflectionUtils.getUniqueDeclaredMethods(userType)) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            try {
                RequestMappingInfo info =
                        (RequestMappingInfo) getMappingForMethod.invoke(
                                requestMappingHandlerMapping, method, userType);
                if (info != null) {
                    requestMappingHandlerMapping.registerMapping(info, handlerBean, method);
                    registered.add(info);
                }
            } catch (Exception ex) {
                log.warn(
                        "插件 SpringMVC 映射注册失败 {}#{}: {}",
                        userType.getName(),
                        method.getName(),
                        ex.getMessage());
            }
        }
        if (!registered.isEmpty()) {
            sessionToMappings
                    .computeIfAbsent(loadSessionId, k -> new CopyOnWriteArrayList<>())
                    .addAll(registered);
            log.info(
                    "插件控制器已挂到宿主 DispatcherServlet: {} 条映射 (session={}, type={})",
                    registered.size(),
                    loadSessionId,
                    AopUtils.getTargetClass(handlerBean).getName());
        }
    }

    public void unregisterSession(String loadSessionId) {
        if (loadSessionId == null || loadSessionId.isBlank()) {
            return;
        }
        List<RequestMappingInfo> infos = sessionToMappings.remove(loadSessionId);
        if (infos == null || infos.isEmpty()) {
            return;
        }
        List<RequestMappingInfo> snapshot = new ArrayList<>(infos);
        for (int i = snapshot.size() - 1; i >= 0; i--) {
            try {
                requestMappingHandlerMapping.unregisterMapping(snapshot.get(i));
            } catch (Exception e) {
                log.debug("unregisterMapping: {}", e.getMessage());
            }
        }
    }

    private static boolean isWebController(Class<?> t) {
        return AnnotatedElementUtils.hasAnnotation(t, RestController.class)
                || AnnotatedElementUtils.hasAnnotation(t, Controller.class);
    }
}
