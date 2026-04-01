package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 与 MMS 主工程版本的兼容声明。主工程 {@code revision}（见根 pom 属性）为整数主线，例如 21。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequiresMmsDescriptor {

    /**
     * 最低兼容的 MMS {@code revision}（含）。
     */
    private Integer revisionMin;

    /**
     * 最高兼容的 MMS {@code revision}（含）；为空表示仅校验下限。
     */
    private Integer revisionMax;

    /**
     * 可选：要求的 Spring Boot 主版本前缀或精确版本，例如 {@code 3.5.8}。宿主可做字符串匹配或语义化比较（本字段仅约定格式，匹配策略由宿主实现）。
     */
    private String springBoot;
}
