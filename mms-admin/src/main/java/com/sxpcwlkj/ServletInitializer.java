package com.sxpcwlkj;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 自定义Web容器配置
 *
 * @name: ServletInitializer
 * @author: 西决
 * @date: 2022/12/01
 **/

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(MmsAdminApplication.class);
    }

}
