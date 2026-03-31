package com.sxpcwlkj.plugin.host;

/**
 * 插件安装目录相对于 {@link PluginInstallationLayout} 的 JAR 布局探测结果（用于市场页标记）。
 */
public enum PluginJarLocationStatus {

    /** 根目录存在且版本目录 lib 下存在 .jar（或未配置待探测版本） */
    OK,
    /** 配置的插件根路径不存在或不是目录 */
    ROOT_NOT_DIRECTORY,
    /** 约定版本目录不存在 */
    VERSION_DIR_MISSING,
    /** 版本目录下 lib 不存在或不是目录 */
    LIB_DIR_MISSING,
    /** lib 下无 .jar */
    JAR_NOT_FOUND
}
