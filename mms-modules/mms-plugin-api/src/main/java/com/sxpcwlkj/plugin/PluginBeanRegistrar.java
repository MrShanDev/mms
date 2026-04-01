package com.sxpcwlkj.plugin;

/**
 * 将对象注册为宿主 Spring 容器中的<strong>单例</strong>，在插件卸载时按注册逆序销毁（行为与
 * {@link org.springframework.beans.factory.DisposableBean}、单例销毁钩子一致，具体以 Spring 版本为准）。
 * <p>{@code logicalName} 仅在<strong>单次加载会话</strong>内唯一；宿主会为每次 onLoad 分配独立命名空间，避免热替换同名冲突。</p>
 */
public interface PluginBeanRegistrar {

    /**
     * @param logicalName 插件内唯一逻辑名：建议 {@code [a-zA-Z][a-zA-Z0-9_-]{0,63}}，不可含 {@code .}
     * @param bean 非 null；宜实现 {@link org.springframework.beans.factory.DisposableBean} 做资源清理
     */
    void registerSingleton(String logicalName, Object bean);

    /**
     * 已由宿主 {@link org.springframework.beans.factory.config.AutowireCapableBeanFactory#createBean(Class)} 等方式完成装配与初始化的实例：
     * 仅登记单例并（若为控制器）挂到宿主 DispatcherServlet；不再对 {@code bean} 执行 autowire/initialize。
     */
    void registerInitializedSingleton(String logicalName, Object bean);
}
