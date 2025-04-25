package com.sxpcwlkj.authority.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.sxpcwlkj.common.properties.SaTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SaTokenConfig
 * @Description TODO
 * @Author 西决
 * @Date 2022/12/4 21:05
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SaTokenProperties saTokenProperties;
    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
                    // 登录验证 -- 排除多个路径
                    SaRouter
                            // 获取所有的
                            .match("/**")
                            // 对未排除的路径进行检查
                            .check(() -> {
                                // 检验当前会话是否已经登录, 如果未登录，则抛出异常：NotLoginException
                                StpUtil.checkLogin();

                                // 有效率影响 用于临时测试
                                 if (saTokenProperties.getInfoTimeOpen()) {
                                     log.debug("剩余有效时间: {}", StpUtil.getTokenTimeout());
                                     log.debug("临时有效时间: {}", StpUtil.getTokenActiveTimeout());
                                 }

                            });
                })).addPathPatterns("/**")
                // 排除不需要拦截的路径
                .excludePathPatterns(saTokenProperties.getExcludes());
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (简单模式)
        return new StpLogicJwtForSimple();
    }

}
