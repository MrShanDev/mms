package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginSqlGuardTest {

    private static final String P = "plugin_demo_";

    @Test
    void allowsSelectWithPrefix() {
        assertDoesNotThrow(() -> PluginSqlGuard.assertSelect("SELECT * FROM plugin_demo_task WHERE id = ?", P, null));
    }

    @Test
    void rejectsMultiStatement() {
        assertThrows(PluginException.class, () -> PluginSqlGuard.assertSafePluginSql("SELECT 1; SELECT 2", P, null));
    }

    @Test
    void rejectsDrop() {
        assertThrows(PluginException.class, () -> PluginSqlGuard.assertSafePluginSql("DROP TABLE plugin_demo_x", P, null));
    }

    @Test
    void rejectsMissingPrefix() {
        assertThrows(PluginException.class, () -> PluginSqlGuard.assertSelect("SELECT * FROM other_table", P, null));
    }

    @Test
    void whitelistAllowsListedTable() {
        Set<String> allow = Set.of("plugin_demo_task");
        assertDoesNotThrow(
                () ->
                        PluginSqlGuard.assertSelect(
                                "SELECT * FROM plugin_demo_task WHERE id = ?", P, allow));
    }

    @Test
    void whitelistRejectsUnlistedTable() {
        Set<String> allow = Set.of("plugin_demo_task");
        assertThrows(
                PluginException.class,
                () ->
                        PluginSqlGuard.assertSelect(
                                "SELECT * FROM plugin_demo_other WHERE 1 = 1", P, allow));
    }
}
