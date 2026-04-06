package com.sxpcwlkj;

import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author mmsAdmin
 */
@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.sxpcwlkj\\.website\\.controller\\..*"))
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.sxpcwlkj.**.mapper"})
@EnableFileStorage
public class MmsAdminApplication {

    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(MmsAdminApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        // 须用 getActiveProfiles()：getProperty("spring.profiles.active") 常仍为 application.yml 里的默认值，与真实激活不一致
        String profiles = String.join(",", env.getActiveProfiles());
        if (profiles.isEmpty()) {
            profiles = "default";
        }
        System.out.println("后端: 系统启动成功,当前环境为: " + profiles);
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running!  Access URLs:\n\t" +
                "Local: \t\thttp://localhost:{}\n\t" +
                "External: \thttp://{}:{}\n\t" +
                "Doc: \t\t{}\n" +
                "----------------------------------------------------------",
            env.getProperty("sxpcwlkj.name"),
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"),
            env.getProperty("sxpcwlkj.docUrl"));

    }



}
