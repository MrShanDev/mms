package com.sxpcwlkj.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName SaTokenProperties
 * @Description TODO
 * @Author 西决
 * @Date 2022/12/4 21:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "sa-token")
public class SaTokenProperties {

    private Boolean infoTimeOpen;
    /**
     * 排除路径
     */
    private String[] excludes;

}
