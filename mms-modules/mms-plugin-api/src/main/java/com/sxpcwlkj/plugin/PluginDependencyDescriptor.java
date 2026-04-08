package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 插件间依赖（仅元数据；版本区间解析由宿主实现）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginDependencyDescriptor {

    /**
     * 依赖插件的逻辑 ID，与 {@link PluginDescriptor#getId()} 一致。
     */
    private String id;

    /**
     * 可选：Maven 风格版本区间，如 {@code [1.0.0,2.0.0)}、{@code 1.+}（具体解析待定）。
     */
    private String versionRange;

    /**
     * 是否必选；默认 true。
     */
    private Boolean optional;

    /**
     * 给人看的说明（不参与依赖解析）；用于描述本依赖与可选/必选的产品策略，避免误读为「全平台唯一规则」。
     */
    private String remark;
}
