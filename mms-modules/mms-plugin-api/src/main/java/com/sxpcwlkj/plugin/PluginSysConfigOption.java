package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * {@link PluginSysConfigDef} 中 {@code select} / {@code multiselect} 的候选项（入库值为 {@link #value}）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginSysConfigOption {

    private String label;

    /** 写入 {@code sys_config.config_value} 的取值（建议使用简短稳定 token）。 */
    private String value;
}
