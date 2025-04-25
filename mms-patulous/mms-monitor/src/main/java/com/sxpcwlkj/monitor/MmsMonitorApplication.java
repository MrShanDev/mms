package com.sxpcwlkj.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAdminServer
@SpringBootApplication
public class MmsMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmsMonitorApplication.class, args);
    }

}
