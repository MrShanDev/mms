package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件声明的前端片段（JSON 契约；实际加载与 mms-ui 另行约定）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginFrontendHint {

    /**
     * 例如 {@code @acme/comments-ui} 或 npm 包名，由宿主/前端约定解释。
     */
    private String modulePackage;

    /**
     * 兼容的 mms-ui 大版本或 git tag 要求（文案/semver 均可，宿主可不解析）。
     */
    private String compatibleMmsUi;

    /**
     * 可选：由插件提供的 Vue 路由 path 前缀列表，例如 {@code /comments}。
     */
    private List<String> routePrefixes = new ArrayList<>();
}
