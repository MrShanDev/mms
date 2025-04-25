package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author shanpengnian
 *  WebSocketProperties
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "encryption")
public class EncryptionProperties {

    /**
     * 是否开启
     */
    private Boolean enable;
    /**
     * 加密类型
     */
    private List<String> types;
    /**
     * 时间戳有效时间单位秒
     * 客户端与服务端请求时间差最大值单位：毫秒
     */
    private Integer validTime;
    /**
     * 请求时间间隔
     * 接口重复请求最大间隔时间单位：毫秒
     */
    private Integer repeatedTime;
}
