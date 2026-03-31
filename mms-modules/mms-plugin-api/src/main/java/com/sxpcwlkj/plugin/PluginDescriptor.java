package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code plugin.json} 根对象，与 JAR 内 {@link PluginConstants#DESCRIPTOR_PATH_IN_JAR} 对应。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginDescriptor {

    /**
     * 插件唯一 ID，建议反向域名风格，如 {@code com.acme.comments}。
     */
    private String id;

    /**
     * 插件语义化版本，如 {@code 1.0.0}。
     */
    private String version;

    /**
     * 展示名称。
     */
    private String name;

    /**
     * 展示描述。
     */
    private String description;

    /**
     * 与宿主 MMS 及 Spring Boot 的兼容范围。
     */
    private RequiresMmsDescriptor requiresMms;

    /**
     * 可选：SPI 入口实现类全限定名；{@link PluginKind#LIBRARY} 可省略。
     */
    private String entryClass;

    /**
     * 插件形态，默认 extension。
     */
    private PluginKind kind = PluginKind.EXTENSION;

    /**
     * 依赖的其他插件。
     */
    private List<PluginDependencyDescriptor> dependencies = new ArrayList<>();

    /**
     * 可选：面向前端的模块说明（P0 占位，供 mms-ui 动态路由与联邦模块对接）。
     */
    private PluginFrontendHint frontend;
}
