package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanpengnian
 *  WebSocketProperties
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {

    /**
     * 是否开启websocket
     */
    private Boolean enable;
}
