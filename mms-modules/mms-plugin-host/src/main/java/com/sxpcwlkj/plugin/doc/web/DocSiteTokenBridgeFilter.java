package com.sxpcwlkj.plugin.doc.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 文档站 Token 桥接：VitePress 等仍写 {@code docToken} cookie 时，对 {@code /doc/v1/**} 在无 {@code Authorization}
 * 头时映射为请求头，以适配宿主 Sa-Token。
 * <p>须由 Spring Boot 在容器启动期通过 {@link org.springframework.boot.web.servlet.FilterRegistrationBean} 注册
 * （{@link com.sxpcwlkj.plugin.host.config.PluginHostAutoConfiguration}）；插件晚于容器初始化加载，无法在运行时
 * {@link jakarta.servlet.ServletContext#addFilter}。</p>
 */
public final class DocSiteTokenBridgeFilter extends OncePerRequestFilter {

    /** 与文档插件安装阶段检测用的名称一致，避免重复注册。 */
    public static final String SERVLET_REGISTRATION_NAME = "mmsPluginDocSiteTokenBridge";

    private static final String PREFIX = "/doc/v1";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri == null || !startsWithDocV1(uri)) {
            chain.doFilter(request, response);
            return;
        }
        if (hasNonBlankHeader(request, "Authorization")) {
            chain.doFilter(request, response);
            return;
        }
        String docTok = readCookie(request, "docToken");
        if (docTok == null || docTok.isBlank()) {
            chain.doFilter(request, response);
            return;
        }
        String token = docTok;
        HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if ("Authorization".equalsIgnoreCase(name)) {
                    return token;
                }
                return super.getHeader(name);
            }
        };
        chain.doFilter(wrapped, response);
    }

    private static boolean startsWithDocV1(String uri) {
        return uri.startsWith(PREFIX + "/") || uri.equals(PREFIX);
    }

    private static boolean hasNonBlankHeader(HttpServletRequest request, String name) {
        String v = request.getHeader(name);
        return v != null && !v.isBlank();
    }

    private static String readCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (cookieName.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
