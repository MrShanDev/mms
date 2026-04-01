package com.sxpcwlkj.plugin;

import java.nio.file.Path;
import java.util.Optional;

/**
 * 宿主在调用 {@link MmsPlugin#onLoad} 时注入的运行环境（接口由宿主模块实现）。
 */
public interface PluginRuntimeContext {

    /**
     * 当前加载中的插件描述（与本 JAR 内 {@code META-INF/mms/plugin.json} 一致）。
     */
    PluginDescriptor pluginDescriptor();

    /**
     * 宿主受控能力（禁止插件直接依赖宿主 Spring Bean）。
     */
    HostServices hostServices();

    /**
     * 将对象注册进宿主 Spring 容器（单例），卸载本插件时销毁；未使用则可忽略。
     */
    PluginBeanRegistrar pluginBeanRegistrar();

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

    /**
     * 反射调用已加载的另一插件 {@link MmsPlugin} 公有实例方法，参数个数须与 {@code args} 一致。
     * 无匹配方法时返回 {@link Optional#empty()}；若方法本身返回 {@code null}，当前实现亦返回 empty。
     */
    Optional<Object> tryInvokePeerPlugin(String targetPluginId, String methodName, Object... args);

    /**
     * 注册卸载钩子：在 {@link MmsPlugin#onUnload} 之后、关闭 ClassLoader 之前按注册逆序执行。
     */
    void addUnloadHook(Runnable hook);
}
