package com.sxpcwlkj.plugin.host.internal;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcPluginBackupAccessTest {
    @Test void exportedCommentsDoNotHideDropAndCreateStatements() {
        String sql = "-- MMS plugin logical backup\nSET NAMES utf8mb4;\n"
                + "-- TABLE plugin_ds_test\nDROP TABLE IF EXISTS `plugin_ds_test`;\n"
                + "CREATE TABLE `plugin_ds_test` (id BIGINT PRIMARY KEY);\n"
                + "INSERT INTO `plugin_ds_test` VALUES (1);\n";
        assertEquals(List.of("SET NAMES utf8mb4", "DROP TABLE IF EXISTS `plugin_ds_test`",
                "CREATE TABLE `plugin_ds_test` (id BIGINT PRIMARY KEY)",
                "INSERT INTO `plugin_ds_test` VALUES (1)"), JdbcPluginBackupAccess.splitSqlStatements(sql));
    }
    @Test void commentMarkerInsideQuotedDataIsPreserved() {
        assertEquals(List.of("INSERT INTO t VALUES ('line1\n-- keep this value')"),
                JdbcPluginBackupAccess.splitSqlStatements("INSERT INTO t VALUES ('line1\n-- keep this value');"));
    }
}
