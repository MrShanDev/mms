package com.sxpcwlkj.framework.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.common.annotation.RateLimit;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统框架
 *
 * @author shanpengnian
 */
@RestController
@RequiredArgsConstructor
public class FrameworkController {


    @Value("${sxpcwlkj.name}")
    private String name;

    @Value("${sxpcwlkj.version}")
    private String version;

    @Value("${sxpcwlkj.describe}")
    private String describe;

    @Value("${server.port}")
    private String port;

    /**
     * 系统入口
     *
     * @return 系统信息
     */
//    @RateLimit(permitsPerSecond = 1.0) // 每秒允许 1 个请求
//    @SaIgnore
//    @GetMapping("/")
//    public String index() {
//        return "Hello " + name + "! V" + version;
//    }

    @PostConstruct
    public void init() {

    }
}
