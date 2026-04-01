package com.sxpcwlkj.plugin;

/**
 * 宿主可聚合为 HTTP 探测（见 {@code mms-plugin-host} {@code /system/pluginHost/health}）。
 * <p><b>项目约定</b>：今后新封装的插件须实现本接口并注册
 * {@code META-INF/services/com.sxpcwlkj.plugin.PluginHealthContributor}（可与 {@link MmsPlugin} 同类或独立类）。</p>
 * <p><b>实现 {@link #health()} 时易错</b>：若在 Spring {@code ApplicationContext} 中判断
 * 「{@link PluginRuntimeContext#pluginBeanRegistrar()} 已注册的 Bean」是否存在，宿主侧真实 Bean 名为
 * {@code mms.plugin.bean.{loadSessionId}.{logicalName}}（宿主
 * {@code com.sxpcwlkj.plugin.host.internal.PluginSpringBeanAttachment#fullBeanName}），<b>不要</b>对裸
 * {@code logicalName} 使用 {@code containsBean(logicalName)}（否则恒为 false、健康误报异常）。应枚举 Bean 名并按后缀/模式匹配。
 * 说明见仓库 {@code mms-plugins/插件封装踩坑与注意事项.md} §一.5 与 §二 清单第 10 条。</p>
 */
@FunctionalInterface
public interface PluginHealthContributor {

    /**
     * 简短健康结果，如 {@code UP}、单行 {@code JSON}。业务不可用时请 <b>抛异常</b>，勿仅返回带 {@code DOWN} 的 JSON（宿主仍可能记为成功行）。
     */
    String health();
}
