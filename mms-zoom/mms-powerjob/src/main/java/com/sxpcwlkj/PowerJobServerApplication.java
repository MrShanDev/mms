package com.sxpcwlkj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import tech.powerjob.server.common.utils.PropertyUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description TODO
 * @Author sxpcwlkj
 * @Version v1.0.0
 */
@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = "tech.powerjob.server")
public class PowerJobServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        PropertyUtils.init();
//        SpringApplication.run(tech.powerjob.server.PowerJobServerApplication.class, args);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(tech.powerjob.server.PowerJobServerApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        System.out.println("PowerJob启动成功,当前环境为: " + env.getProperty("spring.profiles.active"));
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application PowerJob is running!  Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \t\t{}\n" +
                        "----------------------------------------------------------",

                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                "https://www.yuque.com/powerjob/guidence/problem");

    }
}