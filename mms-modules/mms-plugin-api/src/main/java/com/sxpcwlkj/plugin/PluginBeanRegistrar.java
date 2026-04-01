package com.sxpcwlkj.plugin;

/**
 * 将插件创建的对象注册为宿主 Spring 容器中的<strong>单例</strong>，在插件卸载时按注册逆序销毁（行为与
 * {@link org.springframework.beans.factory.DisposableBean}、单例销毁钩子一致，具体以 Spring 版本为准）。
 * <p>{@code logicalName} 仅在<strong>单次加载会话</strong>内唯一；宿主会为每次 onLoad 分配独立命名空间，避免热替换同名冲突。</p>
 */
public interface PluginBeanRegistrar {

    /**
     * @param logicalName 插件内唯一逻辑名：建议 {@code [a-zA-Z][a-zA-Z0-9_-]{0,63}}，不可含 {@code .}
     * @param bean 非 null；宜实现 {@link org.springframework.beans.factory.DisposableBean} 做资源清理
     */
    void registerSingleton(String logicalName, Object bean);
}
