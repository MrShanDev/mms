package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcPluginBackupAccessTest {

    @Test
    void splitRespectsSemicolonInString() {
        List<String> parts =
                JdbcPluginBackupAccess.splitSqlStatements("INSERT INTO t VALUES ('a;b');\nSET NAMES utf8;");
        assertEquals(2, parts.size());
    }

    @Test
    void importAllowsCreateAndInsert() {
        JdbcPluginBackupAccess.assertImportAllowed("CREATE TABLE x (id INT)");
        JdbcPluginBackupAccess.assertImportAllowed("INSERT INTO x VALUES (1)");
    }

    @Test
    void importRejectsGrant() {
        assertThrows(PluginException.class, () -> JdbcPluginBackupAccess.assertImportAllowed("GRANT ALL ON *.* TO u"));
    }
}
