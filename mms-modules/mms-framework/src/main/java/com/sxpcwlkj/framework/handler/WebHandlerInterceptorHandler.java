package com.sxpcwlkj.framework.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.sxpcwlkj.common.utils.JsonUtil;
import com.sxpcwlkj.common.utils.SpringUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.common.context.DemoModeContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.util.Map;

/**
 * @author mmsAdmin
 */
@Slf4j

public class WebHandlerInterceptorHandler implements HandlerInterceptor {

    private final String prodProfile = "prod";

    // Alibaba线程池
    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();

    /**
     * 方法执行前
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!prodProfile.equals(SpringUtil.getActiveProfile())) {
            String url = request.getMethod() + " " + request.getRequestURI();

            log.debug("[PLUS]开始请求 => URL[{}]", url);

            StopWatch stopWatch = new StopWatch();
            invokeTimeTL.set(stopWatch);
            stopWatch.start();
        }

        // 获取处理method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        if (com.sxpcwlkj.authority.LoginObject.getCurrentLoginRealm()==com.sxpcwlkj.authority.LoginRealm.SYSTEM_ADMIN && !method.hasMethodAnnotation(cn.dev33.satoken.annotation.SaIgnore.class)) {
            var policy=SpringUtil.getBeanFactory().getBeanProvider(com.sxpcwlkj.common.tenant.TenantRequestPolicy.class).getIfAvailable();
            if(policy!=null) {
                policy.check();
                var permission=method.getMethodAnnotation(cn.dev33.satoken.annotation.SaCheckPermission.class);
                if(permission!=null) for(String p:permission.value()) policy.checkPermission(p);
            }
        }
        if (org.springframework.core.annotation.AnnotatedElementUtils.hasAnnotation(method.getBeanType(), com.sxpcwlkj.common.annotation.PlatformAdmin.class)
                || method.hasMethodAnnotation(com.sxpcwlkj.common.annotation.PlatformAdmin.class))
            com.sxpcwlkj.authority.LoginObject.requirePlatformAdmin();
        // Sa-Token 1.43.0+ 注解检查已集成到框架,无需手动调用
        // Method method = ((HandlerMethod) handler).getMethod();
        // SaStrategy.me.checkMethodAnnotation.accept(method);

        return true;
    }

    /**
     * 方法执行后
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @throws Exception 异常
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 视图渲染完毕后执⾏，最后执⾏
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        if (!prodProfile.equals(SpringUtil.getActiveProfile())) {
            StopWatch stopWatch = invokeTimeTL.get();
            if(stopWatch!=null){
                stopWatch.stop();
                log.debug("[PLUS]结束请求 => URL[{}],耗时:[{}]毫秒", request.getMethod() + " " + request.getRequestURI(), stopWatch.getTime());
                invokeTimeTL.remove();
            }
        }
        DemoModeContextHolder.clear();
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return StringUtil.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }
}
