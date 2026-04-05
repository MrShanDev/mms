package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginSchemaSqlGuardTest {

    private static final String P = "plugin_ds_";

    @Test
    void allowsCreateTable() {
        assertDoesNotThrow(
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "CREATE TABLE plugin_ds_foo (\n  id BIGINT NOT NULL PRIMARY KEY\n)", P));
    }

    @Test
    void allowsCreateTableIfNotExists() {
        assertDoesNotThrow(
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "CREATE TABLE IF NOT EXISTS plugin_ds_bar ( id INT )", P));
    }

    @Test
    void allowsDropTableIfExists() {
        assertDoesNotThrow(
                () -> PluginSchemaSqlGuard.assertDdlAllowed("DROP TABLE IF EXISTS plugin_ds_foo", P));
    }

    @Test
    void allowsAlterAddColumn() {
        assertDoesNotThrow(
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "ALTER TABLE plugin_ds_foo ADD COLUMN x VARCHAR(32)", P));
    }

    @Test
    void allowsAlterDropColumn() {
        assertDoesNotThrow(
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "ALTER TABLE plugin_ds_foo DROP COLUMN x", P));
    }

    @Test
    void rejectsWrongPrefix() {
        assertThrows(
                PluginException.class,
                () -> PluginSchemaSqlGuard.assertDdlAllowed("CREATE TABLE other_foo ( id INT )", P));
    }

    @Test
    void rejectsSemicolon() {
        assertThrows(
                PluginException.class,
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "CREATE TABLE plugin_ds_a ( id INT ); DROP TABLE plugin_ds_a", P));
    }

    @Test
    void rejectsInformationSchema() {
        assertThrows(
                PluginException.class,
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "CREATE TABLE plugin_ds_x ( id INT, FOREIGN KEY (a) REFERENCES information_schema.tables )",
                                P));
    }

    @Test
    void rejectsAlterModify() {
        assertThrows(
                PluginException.class,
                () ->
                        PluginSchemaSqlGuard.assertDdlAllowed(
                                "ALTER TABLE plugin_ds_foo MODIFY COLUMN x INT", P));
    }
}
