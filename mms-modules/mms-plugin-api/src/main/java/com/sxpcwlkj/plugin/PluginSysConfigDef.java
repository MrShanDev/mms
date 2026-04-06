package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件在 {@code plugin.json} 中声明的 {@code sys_config} 配置项（供市场页展示为固定行，仅改值、保存）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginSysConfigDef {

    /**
     * 键后缀，与 {@link PluginSysConfigKeys#fullKey} 中 suffix 一致。
     */
    private String keySuffix;

    /**
     * 展示名称（写入 sys_config.config_name）。
     */
    private String configName;

    /**
     * 市场页说明（不入库）。
     */
    private String description;

    /**
     * 库中无记录时的表单默认值（不入库直至用户保存）。
     */
    private String defaultValue;

    /**
     * 管理端表单控件类型（小写）；缺省按 {@code text}。
     * <p>与 {@link #valueCardinality} 正交：例如 {@code textarea}+{@link PluginSysConfigValueShapes#LIST} 表示多行编辑 JSON 数组。</p>
     * <p>约定：</p>
     * <ul>
     *   <li>{@code text} — 单行文本</li>
     *   <li>{@code textarea} — 多行</li>
     *   <li>{@code password} — 密文单行</li>
     *   <li>{@code number} — 数字（存为十进制字符串）</li>
     *   <li>{@code switch} — 布尔，存 {@code true}/{@code false}</li>
     *   <li>{@code select} — 单选，须配 {@link #options}</li>
     *   <li>{@code multiselect} — 多选，值为 JSON 数组字符串，须配 {@link #options}（隐含 {@link PluginSysConfigValueShapes#LIST}）</li>
     *   <li>{@code json} — 原始 JSON 文本（多行）</li>
     *   <li>{@code file} / {@code image} — 存 URL 或路径字符串（界面可为普通文本框，便于粘贴上传接口返回值）</li>
     *   <li>{@code color} — 颜色，如 {@code #409EFF}</li>
     *   <li>{@code date} — 日期/时间字符串（由前端 date-picker 定格式，存库为字符串）</li>
     * </ul>
     */
    private String valueType;

    /**
     * 存库形态：{@link PluginSysConfigValueShapes#SCALAR}（默认）、{@link PluginSysConfigValueShapes#LIST}、
     * {@link PluginSysConfigValueShapes#OBJECT}。JSON 中也可写 {@code array}，与 {@code list} 同义。
     */
    private String valueCardinality;

    /**
     * {@link #valueType} 为 {@code select} / {@code multiselect} 时的候选项。
     */
    private List<PluginSysConfigOption> options = new ArrayList<>();
}
