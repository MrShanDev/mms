package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shanpengnian
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.web")
public class WebThymeleafProperties {

    private Boolean isOpen;
    /**
     * 排除路径
     */
    private String[] excludes;
}
