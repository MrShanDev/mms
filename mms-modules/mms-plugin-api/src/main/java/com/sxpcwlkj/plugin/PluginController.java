package com.sxpcwlkj.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记可由宿主在 {@link PluginRuntimeMode#HOST_MVC} 下扫描并挂到 {@code /plugin/{pluginId}/...} 的 Spring MVC 式控制器。
 * <p>类须具备无参构造；方法暂支持无参或仅 {@code jakarta.servlet.http.HttpServletRequest} 形参，返回任意可序列化对象。</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PluginController {
}
