package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.host.web.PluginWebMvcHandlerRegistrar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 将插件单例挂入宿主 {@link ConfigurableApplicationContext}，并按<strong>加载会话 ID</strong>成组销毁，防止泄漏。
 */
@Slf4j
public final class PluginSpringBeanAttachment {

    private static final Pattern LOGICAL_NAME = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]{0,63}$");

    private final ConfigurableApplicationContext applicationContext;
    private final PluginWebMvcHandlerRegistrar mvcHandlerRegistrar;

    private final Map<String, List<String>> sessionToBeanNames = new ConcurrentHashMap<>();

    public PluginSpringBeanAttachment(
            ConfigurableApplicationContext applicationContext, PluginWebMvcHandlerRegistrar mvcHandlerRegistrar) {
        this.applicationContext = applicationContext;
        this.mvcHandlerRegistrar = mvcHandlerRegistrar;
    }

    /**
     * @param loadSessionId 单次加载唯一 ID（如 UUID）
     * @param logicalName   见 {@link com.sxpcwlkj.plugin.PluginBeanRegistrar}
     */
    public synchronized void registerSingleton(String loadSessionId, String logicalName, Object bean) {
        validateArgs(loadSessionId, logicalName, bean);
        ConfigurableListableBeanFactory bf = applicationContext.getBeanFactory();
        if (!(bf instanceof DefaultListableBeanFactory dlb)) {
            throw new PluginException("仅支持 DefaultListableBeanFactory，当前: " + bf.getClass().getName());
        }
        String fullName = fullBeanName(loadSessionId, logicalName);
        if (dlb.containsSingleton(fullName) || dlb.containsBeanDefinition(fullName)) {
            throw new PluginException("宿主 Bean 名称已占用: " + logicalName + "（本会话或容器冲突）");
        }
        dlb.registerSingleton(fullName, bean);
        sessionToBeanNames.computeIfAbsent(loadSessionId, k -> new ArrayList<>()).add(fullName);
        try {
            dlb.autowireBean(bean);
            Object exposed = dlb.initializeBean(bean, fullName);
            registerMvcIfNeeded(loadSessionId, exposed);
        } catch (Exception e) {
            throw new PluginException("插件单例初始化失败: " + logicalName, e);
        }
    }

    /**
     * 仅登记已由 Spring 创建并初始化的实例，并尝试挂 MVC（不再次 autowire/initialize）。
     */
    public synchronized void registerInitializedSingleton(String loadSessionId, String logicalName, Object bean) {
        validateArgs(loadSessionId, logicalName, bean);
        ConfigurableListableBeanFactory bf = applicationContext.getBeanFactory();
        if (!(bf instanceof DefaultListableBeanFactory dlb)) {
            throw new PluginException("仅支持 DefaultListableBeanFactory，当前: " + bf.getClass().getName());
        }
        String fullName = fullBeanName(loadSessionId, logicalName);
        if (dlb.containsSingleton(fullName) || dlb.containsBeanDefinition(fullName)) {
            throw new PluginException("宿主 Bean 名称已占用: " + logicalName + "（本会话或容器冲突）");
        }
        dlb.registerSingleton(fullName, bean);
        sessionToBeanNames.computeIfAbsent(loadSessionId, k -> new ArrayList<>()).add(fullName);
        try {
            Object exposed = dlb.getBean(fullName);
            registerMvcIfNeeded(loadSessionId, exposed);
        } catch (Exception e) {
            throw new PluginException("插件单例登记后解析失败: " + logicalName, e);
        }
    }

    private static void validateArgs(String loadSessionId, String logicalName, Object bean) {
        if (loadSessionId == null || loadSessionId.isBlank()) {
            throw new PluginException("loadSessionId 不能为空");
        }
        if (logicalName == null || !LOGICAL_NAME.matcher(logicalName).matches()) {
            throw new PluginException("logicalName 非法，须匹配 [a-zA-Z][a-zA-Z0-9_-]{0,63}");
        }
        if (bean == null) {
            throw new PluginException("bean 不能为 null");
        }
    }

    private void registerMvcIfNeeded(String loadSessionId, Object exposed) {
        if (mvcHandlerRegistrar != null && exposed != null) {
            mvcHandlerRegistrar.registerHandlerMethods(loadSessionId, exposed);
        }
    }

    private static String fullBeanName(String loadSessionId, String logicalName) {
        return "mms.plugin.bean." + loadSessionId + "." + logicalName;
    }

    /**
     * 按注册<strong>逆序</strong>{@link DefaultListableBeanFactory#destroySingleton(String)}。
     */
    public synchronized void releaseSession(String loadSessionId) {
        if (loadSessionId == null || loadSessionId.isBlank()) {
            return;
        }
        if (mvcHandlerRegistrar != null) {
            mvcHandlerRegistrar.unregisterSession(loadSessionId);
        }
        List<String> names = sessionToBeanNames.remove(loadSessionId);
        if (names == null || names.isEmpty()) {
            return;
        }
        ConfigurableListableBeanFactory bf = applicationContext.getBeanFactory();
        if (!(bf instanceof DefaultListableBeanFactory dlb)) {
            return;
        }
        for (int i = names.size() - 1; i >= 0; i--) {
            String n = names.get(i);
            try {
                if (dlb.containsSingleton(n)) {
                    dlb.destroySingleton(n);
                }
            } catch (Exception e) {
                log.warn("销毁插件注册 Bean 失败 {}: {}", n, e.getMessage());
            }
        }
    }
}
