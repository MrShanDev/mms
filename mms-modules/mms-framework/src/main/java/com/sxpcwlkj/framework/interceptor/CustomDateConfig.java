package com.sxpcwlkj.framework.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

/**
 * 日期转换器
 *
 * @author shanpengnian
 */
@Configuration
public class CustomDateConfig {

    @Bean
    public FormattingConversionService mvcConversionServiceMy() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        // 添加你的自定义Converter或其他配置...
        return conversionService;
    }
}
