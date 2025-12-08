package com.sxpcwlkj;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 西决
 */
@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.sxpcwlkj.**.mapper"})
public class MmsDocApiApplication {
    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(MmsDocApiApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        System.out.println("文档API端: 系统启动成功,当前环境为: " + env.getProperty("spring.profiles.active"));
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
