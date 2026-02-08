package com.sxpcwlkj.system.runner;


import com.sxpcwlkj.system.service.SysConfigService;
import com.sxpcwlkj.system.service.SysDictService;
import com.sxpcwlkj.system.service.SysOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author mmsAdmin
 * @ClassName SystemRunner
 * @description: 项目初始化
 * @date 2024年03月29日
 * @version: 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1) // 可以通过@Order注解指定执行顺序，数字越小越先执行
public class SystemRunner implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final SysDictService sysDictService;
    private final SysOssConfigService sysOssConfigService;
    private final SysConfigService sysConfigService;
    @Override
    public void run(String... args) throws Exception {
        // 在这里执行启动时需要执行的操作

        log.info("=== 1.字典已初始化:"+sysDictService.initSysDict(null)+"个: ===");

        log.info("=== 2.Oss配置已初始化:"+sysOssConfigService.initOss()+ "===");

        log.info("=== 3.Base配置已初始化:"+sysConfigService.initBase()+ "===");

        log.info("=== 4.Sms配置已初始化:"+sysConfigService.initSms()+ "===");

        log.info("=== 5.Email配置已初始化:"+sysConfigService.initEmail()+ "===");

        log.info("=== 6.Wx配置已初始化:"+sysConfigService.initWx()+ "===");

        applicationContext.publishEvent(new ContextRefreshedEvent(applicationContext));
    }



}
