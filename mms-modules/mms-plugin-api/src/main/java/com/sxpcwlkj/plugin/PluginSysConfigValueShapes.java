package com.sxpcwlkj.plugin;

import java.util.Locale;

/**
 * {@link PluginSysConfigDef} 在 {@code sys_config.config_value} 中的存库形态（与 {@link PluginSysConfigDef#getValueType()} 正交）。
 * <p>插件读取配置时：先按 {@code keySuffix} 取字符串，再按声明的 {@code valueCardinality} 解析（标量 / JSON 数组 / JSON 对象）。</p>
 */
public final class PluginSysConfigValueShapes {

    /** 单个标量字符串（文本、开关、单选值、数字字符串等）。 */
    public static final String SCALAR = "scalar";

    /** JSON 数组，例如字符串列表 {@code ["a","b"]}；可与 {@code textarea} 组合便于手写。 */
    public static final String LIST = "list";

    /** JSON 对象，例如 {@code {"k":"v"}}；宜与 {@code json}/{@code textarea} 组合。 */
    public static final String OBJECT = "object";

    private PluginSysConfigValueShapes() {}

    /**
     * 规范化声明：{@code null}/空白 → {@link #SCALAR}；{@code array} 与 {@link #LIST} 同义。
     *
     * @return 仅 {@link #SCALAR}、{@link #LIST}、{@link #OBJECT}
     * @throws PluginException 无法识别的取值
     */
    public static String normalizeDeclaredCardinality(String raw) {
        if (raw == null || raw.isBlank()) {
            return SCALAR;
        }
        String c = raw.trim().toLowerCase(Locale.ROOT);
        if ("array".equals(c)) {
            return LIST;
        }
        if (SCALAR.equals(c) || LIST.equals(c) || OBJECT.equals(c)) {
            return c;
        }
        throw new PluginException(
                "valueCardinality 须为 scalar | list | object（array 等同于 list），当前: " + raw);
    }
}
