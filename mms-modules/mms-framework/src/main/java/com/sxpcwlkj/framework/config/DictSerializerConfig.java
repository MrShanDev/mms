package com.sxpcwlkj.framework.config;


import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 修改被@Dict注解标注的属性字典序列化方式
 *
 * @author liujing
 */
@Configuration
public class DictSerializerConfig {

    @Resource
    private DictSensitiveAnnotationIntrospector dictSensitiveAnnotationIntrospector;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.annotationIntrospector(dictSensitiveAnnotationIntrospector);
    }
}
