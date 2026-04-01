package com.sxpcwlkj.plugin.host.web;

import com.sxpcwlkj.plugin.host.internal.PluginMdc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 将 {@code /plugin/{pluginId}/...} 分发给插件控制器方法。
 * <p>与 {@code mms-api-admin} 共用 Sa-Token 拦截器链（{@code sa-token.excludes} 未放行 {@code /plugin/**} 时需登录），
 * 租户/权限与常规管理端接口一致；仅子进程 peer 等明确 {@code @SaIgnore} 的路径例外。</p>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PluginMvcDispatcher {

    private static final String BASE = "/plugin/";

    private final PluginMvcRegistry registry;
    private final PluginMvcExecutionGuard executionGuard;
    private final ObjectProvider<PluginMvcExecutorRegistry> executorRegistryProvider;

    @RequestMapping(
            value = BASE + "**",
            method = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.PATCH
            })
    public ResponseEntity<?> dispatch(HttpServletRequest request) throws Exception {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());
        }
        if (!uri.startsWith(BASE)) {
            return ResponseEntity.notFound().build();
        }
        String rest = uri.substring(BASE.length());
        int slash = rest.indexOf('/');
        if (slash <= 0) {
            return ResponseEntity.notFound().build();
        }
        String pluginId = rest.substring(0, slash);
        String subPath = rest.substring(slash + 1).trim();
        while (subPath.startsWith("/")) {
            subPath = subPath.substring(1);
        }
        List<String> segments = splitSegments(subPath);

        String httpMethod = request.getMethod();
        PluginMvcRegistry.RouteMatch match = registry.match(pluginId, httpMethod, segments);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        if (!executionGuard.allowBeforeInvoke(pluginId)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("插件路由被熔断或触发限流");
        }
        Method m = match.record().handlerMethod();
        m.setAccessible(true);
        ClassLoader ucl = m.getDeclaringClass().getClassLoader();
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        Runnable popMdc = PluginMdc.pushPluginContext(match.record().pluginId(), match.record().pluginVersion());
        Thread.currentThread().setContextClassLoader(ucl);
        try {
            Object[] invokeArgs;
            try {
                invokeArgs = PluginMvcInvocationSupport.buildArgs(
                        m,
                        request,
                        match.pathVariables(),
                        match.record().template().variableNamesInOrder());
            } catch (IllegalArgumentException ex) {
                executionGuard.afterFailedInvoke(pluginId, ex);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("插件路由参数绑定失败: " + ex.getMessage());
            }
            Callable<Object> task = () -> m.invoke(match.record().controller(), invokeArgs);
            PluginMvcExecutorRegistry pool = executorRegistryProvider.getIfAvailable();
            Object out;
            try {
                if (pool != null) {
                    out = pool.execute(pluginId, task);
                } else {
                    out = task.call();
                }
            } catch (TimeoutException te) {
                executionGuard.afterFailedInvoke(pluginId, te);
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("插件路由执行超时");
            } catch (ExecutionException ee) {
                Throwable c = ee.getCause() != null ? ee.getCause() : ee;
                log.warn("插件 MVC 线程池提交失败 pluginId={} {} {}", pluginId, httpMethod, subPath, c);
                if (c instanceof Exception ex2) {
                    throw ex2;
                }
                throw ee;
            }
            if (out == null) {
                executionGuard.afterSuccessfulInvoke(pluginId);
                return ResponseEntity.noContent().build();
            }
            if (out instanceof String s) {
                executionGuard.afterSuccessfulInvoke(pluginId);
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(s);
            }
            executionGuard.afterSuccessfulInvoke(pluginId);
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            executionGuard.afterFailedInvoke(pluginId, e);
            log.warn("插件 MVC 调用失败 pluginId={} {} {}", pluginId, httpMethod, subPath, e);
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(old);
            popMdc.run();
        }
    }

    private static List<String> splitSegments(String subPath) {
        if (subPath == null || subPath.isBlank()) {
            return List.of();
        }
        String norm = subPath.replaceAll("/+", "/");
        while (norm.endsWith("/") && norm.length() > 1) {
            norm = norm.substring(0, norm.length() - 1);
        }
        String[] parts = norm.split("/");
        List<String> out = new ArrayList<>();
        for (String p : parts) {
            if (!p.isEmpty()) {
                out.add(p);
            }
        }
        return out;
    }
}
