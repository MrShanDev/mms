package com.sxpcwlkj.framework.handler;


import com.sxpcwlkj.common.properties.WebThymeleafProperties;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.function.Consumer;

/**
 * @author mmsAdmin
 * 跨域请求配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfigurerHandler implements WebMvcConfigurer {


    private final WebThymeleafProperties webThymeleafProperties;

    @Value("${mms.cors.allowed-origins:*}")
    private String allowedOrigins;
    @Value("${mms.cors.allowed-headers:*}")
    private String allowedHeaders;
    @Value("${mms.cors.allowed-methods:*}")
    private String allowedMethods;
    @Value("${mms.cors.allow-credentials:true}")
    private boolean allowCredentials;
    @Value("${mms.cors.max-age:1800}")
    private long maxAge;
    @Override
    public void addInterceptors(@org.jetbrains.annotations.NotNull InterceptorRegistry registry) {
        // 全局访问性能拦截
        if (webThymeleafProperties.getIsOpen()) {
            registry.addInterceptor(new WebHandlerInterceptorHandler())
            .addPathPatterns("/**")
                .addPathPatterns("/**");
        }else {
            registry.addInterceptor(new WebHandlerInterceptorHandler());
        }
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/static/")
            .resourceChain(false);
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(allowCredentials);
        // 设置访问源地址
        applyAllowedList(allowedOrigins, config::addAllowedOriginPattern);
        // 设置访问源请求头
        applyAllowedList(allowedHeaders, config::addAllowedHeader);
        // 设置访问源请求方法
        applyAllowedList(allowedMethods, config::addAllowedMethod);
        // 有效期
        config.setMaxAge(maxAge);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    private void applyAllowedList(String rawValues, Consumer<String> applier) {
        if (!StringUtils.hasText(rawValues)) {
            return;
        }
        for (String value : StringUtils.commaDelimitedListToStringArray(rawValues)) {
            String trimmed = value.trim();
            if (StringUtils.hasText(trimmed)) {
                applier.accept(trimmed);
            }
        }
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedOriginPattern("*");
//        corsConfiguration.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }

}
