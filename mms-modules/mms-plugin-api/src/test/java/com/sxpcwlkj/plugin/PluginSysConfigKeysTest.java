package com.sxpcwlkj.plugin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginSysConfigKeysTest {

    @Test
    void fullKeyAndPrefix() {
        String id = "com.example.plugin";
        assertEquals("mms.plugin.com.example.plugin.wechat.url", PluginSysConfigKeys.fullKey(id, "wechat.url"));
        assertTrue(PluginSysConfigKeys.belongsToPlugin("mms.plugin.com.example.plugin.a", id));
        assertEquals("wechat.url", PluginSysConfigKeys.extractSuffix(id, "mms.plugin.com.example.plugin.wechat.url"));
    }

    @Test
    void invalidSuffix() {
        assertThrows(PluginException.class, () -> PluginSysConfigKeys.fullKey("com.a.b", ""));
        assertThrows(PluginException.class, () -> PluginSysConfigKeys.fullKey("com.a.b", "9bad"));
    }

    @Test
    void resolveOwningPluginId_prefersLongestRegisteredPrefix() {
        String key = PluginSysConfigKeys.fullKey("com.foo.bar", "api");
        assertEquals(
                "com.foo.bar",
                PluginSysConfigKeys.resolveOwningPluginId(key, List.of("com.foo", "com.foo.bar")).orElseThrow());
        assertEquals(
                "com.foo",
                PluginSysConfigKeys.resolveOwningPluginId(
                                PluginSysConfigKeys.fullKey("com.foo", "api"), List.of("com.foo", "com.foo.bar"))
                        .orElseThrow());
        assertFalse(PluginSysConfigKeys.resolveOwningPluginId("sys.login.title", List.of("com.foo")).isPresent());
    }
}
