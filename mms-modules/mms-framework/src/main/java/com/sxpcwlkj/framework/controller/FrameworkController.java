package com.sxpcwlkj.framework.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.Console;
import com.sxpcwlkj.common.annotation.RateLimit;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统框架
 *
 * @author shanpengnian
 */
@Controller
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
    @RateLimit(permitsPerSecond = 1.0) // 每秒允许 1 个请求
    @SaIgnore
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello " + name + "! V" + version;
    }

    @RateLimit(permitsPerSecond = 1.0) // 每秒允许 1 个请求
    @SaIgnore
    @GetMapping("/ai")
    public String ai() {
        return "ai/index";
    }

    @PostConstruct
    public void init() {
        Console.log("========== Hello" + name + "! V" + version+" ==========");
    }
}
