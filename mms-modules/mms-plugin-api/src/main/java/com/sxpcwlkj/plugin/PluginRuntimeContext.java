package com.sxpcwlkj.plugin;

import java.nio.file.Path;

/**
 * 宿主在调用 {@link MmsPlugin#onLoad} 时注入的运行环境（P0 接口；宿主实现类在后续模块提供）。
 */
public interface PluginRuntimeContext {

    /**
     * 当前 MMS 主工程 pom {@code revision} 整型值。
     */
    int hostMmsRevision();

    /**
     * 当前 Spring Boot 版本，如 {@code 3.5.8}。
     */
    String hostSpringBootVersion();

    /**
     * 该插件版本在安装根下的目录，例如 {@code .../mms-plugins/com.acme.foo/1.0.0}。
     */
    Path pluginInstallRoot();

    /**
     * {@link com.sxpcwlkj.plugin.PluginConstants#SUBDIR_DATA} 目录（已确保存在由宿主负责）。
     */
    Path pluginDataDirectory();

    /**
     * {@link com.sxpcwlkj.plugin.PluginConstants#SUBDIR_TMP} 目录。
     */
    Path pluginTemporaryDirectory();
}
