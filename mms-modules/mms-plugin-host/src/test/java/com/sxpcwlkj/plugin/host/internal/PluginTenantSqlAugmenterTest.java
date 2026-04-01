package com.sxpcwlkj.plugin.host.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginTenantSqlAugmenterTest {

    @Test
    void appendsTenantForSelect() {
        var r = PluginTenantSqlAugmenter.applyIfEnabled(
                "select * from plugin_x_t WHERE a = ?", new Object[] {1}, "tenant_id", "000000");
        assertTrue(r.tenantAppended());
        assertTrue(r.sql().contains("tenant_id"));
        assertEquals(2, r.args().length);
        assertEquals("000000", r.args()[1]);
    }

    @Test
    void skipsWhenColumnPresent() {
        var r = PluginTenantSqlAugmenter.applyIfEnabled(
                "select * from plugin_x_t where tenant_id = ?", new Object[] {"x"}, "tenant_id", "000000");
        assertFalse(r.tenantAppended());
    }

    @Test
    void skipsInsert() {
        var r = PluginTenantSqlAugmenter.applyIfEnabled(
                "insert into plugin_x_t (a) values (?)", new Object[] {1}, "tenant_id", "000000");
        assertFalse(r.tenantAppended());
    }
}
