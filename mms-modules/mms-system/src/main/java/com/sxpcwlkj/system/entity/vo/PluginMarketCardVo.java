package com.sxpcwlkj.system.entity.vo;

import com.sxpcwlkj.plugin.PluginSysConfigDef;
import com.sxpcwlkj.plugin.host.PluginManifestView;
import lombok.Data;

import java.util.List;

/**
 * 插件市场卡片：库内元数据 + 运行时状态合并结果。
 */
@Data
public class PluginMarketCardVo {

    private String catalogId;

    private String pluginId;

    private String name;

    private String iconUrl;

    private String description;

    /** 展示用版本（优先已加载，其次磁盘最新） */
    private String displayVersion;

    /**
     * NOT_INSTALLED：未安装；
     * ON_DISK：已安装（磁盘有包，未在当前 JVM 加载或宿主未启用）；
     * LOADED：已安装（运行中）。
     */
    private String runtimeState;

    /**
     * 健康探测汇总（仅对已加载插件有意义）：<br>
     * NONE —；NORMAL 正常；ABNORMAL 异常；NO_SPI 无健康 SPI。
     */
    private String healthState;

    /** 库表标记的当前激活版本（若有） */
    private String catalogActiveVersion;

    /** 库表记录过的版本列表（用于回切） */
    private List<String> recordedVersions;

    private Boolean hostEnabled;

    private String rootDirHint;

    private String diskVersionsLine;

    private PluginManifestView manifest;

    /**
     * 与 {@link PluginManifestView#sysConfig()} 同源：来自 {@code plugin.json}，未加载插件时由磁盘探测填充。
     */
    private List<PluginSysConfigDef> sysConfigSchema;

    private String healthBody;

    /**
     * 磁盘/JAR 布局异常码（与 {@code PluginJarLocationStatus} 同名），用于市场列表标记；
     * 为 null 时表示未发现布局问题（含根目录正常且无需探测的版本）。
     */
    private String diskLayoutWarning;

    /** 与配置项 {@code mms.plugin.subprocess-launch-enabled} 一致。 */
    private Boolean subprocessLaunchEnabled;

    /** 独立子进程有效端口（仅 INDEPENDENT_PROCESS 且已 fork 时有值）。 */
    private Integer subprocessPort;

    /** 宿主 {@link com.sxpcwlkj.plugin.host.internal.PortLeaseTracker} 登记端口（可与 effectivePort 对照抢驻）。 */
    private Integer subprocessHostLeasedPort;

    /**
     * 回环 TCP 探测：无法绑定该端口则通常为 true（已有监听）；用于与子进程声明端口对齐验收。
     */
    private Boolean subprocessTcpPortAppearsBound;

    private Boolean subprocessAlive;

    private Long subprocessPid;

    private String subprocessLastError;
}
