package com.sxpcwlkj.framework.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author shanpengnian
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ThymeleafConfig {

    @Value("${spring.thymeleaf.static.resource.version:/}")
    private String staticResourceVersion;

    @Value("${spring.thymeleaf.static.resource.cdn:/}")
    private String staticResourceCdn;

    // 注入上下文路径（如 /myapp）
    @ModelAttribute("ctx")
    public String contextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    @ModelAttribute("cdn")
    public String staticCdn() {
        return staticResourceCdn;
    }
    @ModelAttribute("version")
    public String staticVersion() {
        return staticResourceVersion;
    }
}
