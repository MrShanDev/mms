package com.sxpcwlkj.framework.controller;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {


    private final ServletContext servletContext;

    // 注入上下文路径（如 /myapp）
    @ModelAttribute("ctx")
    public String contextPath() {
        return servletContext.getContextPath();
    }

    // 可选：注入静态资源 CDN 前缀（需在配置文件中定义）
    @Value("${spring.thymeleaf.static-resource-url:}")
    private String staticResourceUrl;

    @ModelAttribute("staticCdn")
    public String staticCdn() {
        return staticResourceUrl;
    }
}
