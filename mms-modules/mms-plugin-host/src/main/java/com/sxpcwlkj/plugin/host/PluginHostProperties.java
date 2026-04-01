package com.sxpcwlkj.plugin.host;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 插件宿主行为开关与路径。
 */
@Data
@ConfigurationProperties(prefix = "mms.plugin")
public class PluginHostProperties {

    /**
     * 是否在启动时加载插件并调用 {@link com.sxpcwlkj.plugin.MmsPlugin#onLoad}。
     */
    private boolean enabled = false;

    /**
     * 插件根目录；默认可由配置覆盖，为空时使用 user.dir/mms-plugins。
     */
    private String rootDir;

    /**
     * 与主工程 pom {@code revision} 及 {@link com.sxpcwlkj.plugin.PluginDescriptor} 校验一致。
     */
    private int hostMmsRevision = 21;
}
