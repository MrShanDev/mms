package com.sxpcwlkj.system.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfficialPluginReleaseServiceTest {
    @Test
    void fixedCatalogExposesOnlyTheEightPublishedPlugins() throws Exception {
        var service = new OfficialPluginReleaseService(null);
        service.loadCatalog();

        for (String pluginId : new String[] {
                "mms.plugin.c-base", "mms.plugin.datasource", "mms.plugin.redis-inspect",
                "mms.plugin.wechat-bot", "mms.plugin.syslog", "mms.plugin.doc",
                "mms.plugin.c-member", "mms.plugin.c-site"
        }) {
            assertNotNull(service.releaseVersion(pluginId), pluginId);
            assertEquals("https://gitee.com/MrShanDev/mms/releases/tag/plugins-2026.10.12",
                    service.releaseUrl(pluginId));
        }
        assertNull(service.releaseVersion("mms.plugin.unknown"));
        assertNull(service.releaseUrl("mms.plugin.unknown"));
    }
}
