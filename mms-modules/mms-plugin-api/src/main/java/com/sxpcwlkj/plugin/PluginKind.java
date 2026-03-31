package com.sxpcwlkj.plugin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 插件形态：可执行扩展 vs 纯依赖库（供宿主解析依赖图与加载策略使用）。
 */
public enum PluginKind {

    /**
     * 独立业务扩展，通常含入口类并通过 SPI 注册。
     */
    EXTENSION("extension"),

    /**
     * 供其他插件依赖的库包，可不实现 {@link MmsPlugin}。
     */
    LIBRARY("library");

    private final String wireName;

    PluginKind(String wireName) {
        this.wireName = wireName;
    }

    @JsonValue
    public String wireName() {
        return wireName;
    }

    @JsonCreator
    public static PluginKind fromWire(String raw) {
        if (raw == null || raw.isBlank()) {
            return EXTENSION;
        }
        for (PluginKind k : values()) {
            if (k.wireName.equalsIgnoreCase(raw.trim())) {
                return k;
            }
        }
        throw new PluginException("未知的 plugin kind: " + raw);
    }
}
