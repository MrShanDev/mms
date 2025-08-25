package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;
/**
 * @author mmsAdmin
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.mode")
public class DemoModeProperties {
    private boolean enabled;
    private String messageTemplate;
    // 白名单用户（允许在演示模式下修改）
    private Set<String> allowedUsers;

    // 白名单方法（允许在演示模式下修改）
    private Set<String> allowedMethods;

    public boolean isAllowedUser(String username) {
        return allowedUsers.contains(username);
    }

    public boolean isAllowedMethod(String methodName) {
        return allowedMethods.contains(methodName);
    }

}
