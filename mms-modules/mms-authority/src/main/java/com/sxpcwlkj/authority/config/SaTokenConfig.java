package com.sxpcwlkj.authority.config;

import cn.dev33.satoken.SaManager;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.json.SaJsonTemplateForJackson;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxpcwlkj.common.properties.SaTokenProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SaTokenConfig
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2022/12/4 21:05
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SaTokenProperties saTokenProperties;
    private final ObjectMapper objectMapper;

    /**
     * 统一 Sa-Token 的 JSON 解析器 - 增强版
     */
    @PostConstruct
    public void setSaTokenJson() {
        // 创建自定义的JSON模板，增加容错处理
        SaJsonTemplateForJackson template = new SaJsonTemplateForJackson() {
            @Override
            public <T> T jsonToObject(String jsonString, Class<T> clazz) {
                try {
                    return super.jsonToObject(jsonString, clazz);
                } catch (Exception e) {
                    log.warn("JSON反序列化失败，返回null值。原始JSON: {}",
                        jsonString != null ? jsonString.substring(0, Math.min(100, jsonString.length())) : "null");

                    // 如果是Session对象，返回null让Sa-Token创建新的
                    if (cn.dev33.satoken.session.SaSession.class.isAssignableFrom(clazz)) {
                        return null;
                    }

                    // 尝试清理可能损坏的数据
                    if (jsonString != null && jsonString.contains("�")) {
                        log.warn("检测到损坏字符，尝试修复JSON");
                        String cleaned = jsonString.replaceAll("[^\\x20-\\x7E\\x0A\\x0D]", "");
                        try {
                            return super.jsonToObject(cleaned, clazz);
                        } catch (Exception ex) {
                            // 修复失败，返回null
                            return null;
                        }
                    }

                    return null;
                }
            }
        };

        // 配置Jackson以处理更多异常情况
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        template.objectMapper = objectMapper;
        SaManager.setSaJsonTemplate(template);

        log.info("Sa-Token JSON解析器已配置增强容错");
    }

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的拦截器，自动处理@SaIgnore等注解，并排除配置文件中定义的路径
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**")
                // 排除不需要拦截的路径（来自配置文件）
                .excludePathPatterns(saTokenProperties.getExcludes());
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (简单模式)
        return new StpLogicJwtForSimple();
    }

}
