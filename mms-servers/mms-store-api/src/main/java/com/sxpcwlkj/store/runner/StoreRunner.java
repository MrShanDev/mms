package com.sxpcwlkj.store.runner;

import com.sxpcwlkj.base.service.ApiSysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(10) // 可以通过@Order注解指定执行顺序，数字越小越先执行
public class StoreRunner implements CommandLineRunner {

    private final ApiSysConfigService apiSysConfigService;
    @Override
    public void run(String... args) throws Exception {
        apiSysConfigService.initStoreSysConfig();
        log.info("=== 快递鸟物流初始化 ===");
    }
}
