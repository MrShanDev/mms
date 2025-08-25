package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName SxpcwkjProperties
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2022/12/4 20:30
 */
@Data
@Component
@ConfigurationProperties(prefix = "sxpcwlkj")
public class MssAdminProperties {
    /**
     * 名称
     */
    private String name;

    /**
     * 文档
     */
    private String docUrl;

    /**
     * 年份
     */
    private String copyrightYear;

    /**
     * 机构组织
     */
    private String organization;
    /**
     * 版本
     */
    private String version;
    /**
     * 描述
     */
    private String describe;
}
