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
     * 可选：面向前端的模块说明（占位，供 mms-ui 动态路由与联邦模块对接）。
     */
    private PluginFrontendHint frontend;

    /**
     * 运行模式；未声明时视为 {@link PluginRuntimeMode#SPI_ONLY}（兼容旧插件）。
     */
    private PluginRuntimeMode runtimeMode;

    /**
     * 插件依赖的宿主 {@link HostServices} 最低契约版本；未声明视为 1。
     */
    private Integer hostServicesContractVersion;

    /**
     * 独立进程模式下的对外端口（1024–65535）；仅对 {@link PluginRuntimeMode#INDEPENDENT_PROCESS} 有意义。
     */
    private Integer independentPort;

    /**
     * 独立进程主类全限定名；{@link PluginRuntimeMode#INDEPENDENT_PROCESS} 时必填。
     */
    private String mainClass;

    /**
     * 插件自有表/SQL 前缀建议（如 {@code plugin_xxx_}），供宿主白名单校验使用。
     */
    private String pluginTablePrefix;

    /**
     * 可选：{@link PluginConstants#DEPS_FINGERPRINT_MANIFEST} 文件内容的 SHA-256（64 位十六进制，大小写有皆可比对）。
     */
    private String dependencyFingerprintSha256;

    /**
     * 可选：插件数据表相对名（不加前缀），与 {@link #pluginTablePrefix} 拼接成物理表名；
     * 非空时 {@link PluginDataAccess} 仅允许 SQL 中出现这些完整表名（及前缀匹配校验）。
     */
    private List<String> pluginDataTables = new ArrayList<>();

    /**
     * 为 true 时宿主可提供 {@link HostServices#pluginBackupAccess(PluginDescriptor)}（全库/指定表逻辑备份与还原）。
     * 宜仅赋给受控运维类插件；默认 null/false。
     */
    private Boolean backupOperator;

    /**
     * 可选：声明本插件在 {@code sys_config} 中的可配置项（键为 {@code mms.plugin.{id}.{keySuffix}}）；
     * 每项可含 {@link PluginSysConfigDef#getValueType()} / {@link PluginSysConfigDef#getOptions()} 驱动管理端表单；
     * 市场页按此固定行展示，仅支持改值与保存。未声明时仅允许更新库中已有键。
     */
    private List<PluginSysConfigDef> sysConfig = new ArrayList<>();

    /**
     * 可选：安装成功时在各租户下幂等写入 {@code sys_function}（固定主键、{@code remark = "plugin:{id}"}）；
     * 插件从库表彻底卸载时由宿主删除对应行及 {@code sys_role_function}。
     */
    private PluginMenuBootstrapDef menuBootstrap;

    /**
     * 未声明 {@link #runtimeMode} 时的默认行为。
     */
    public PluginRuntimeMode runtimeModeOrDefault() {
        return runtimeMode != null ? runtimeMode : PluginRuntimeMode.SPI_ONLY;
    }

    /**
     * 未声明 {@link #hostServicesContractVersion} 时按契约版本 1 处理（与首版宿主对齐）。
     */
    public int requiredHostServicesContractVersionOrDefault() {
        return hostServicesContractVersion != null ? hostServicesContractVersion : 1;
    }
}
