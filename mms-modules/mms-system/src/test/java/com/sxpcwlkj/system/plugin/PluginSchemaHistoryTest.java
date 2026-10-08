package com.sxpcwlkj.system.plugin;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class PluginSchemaHistoryTest {
    private static final String SQL = "CREATE TABLE IF NOT EXISTS plugin_test (id BIGINT);";
    @Test void invalidSecondStatementWritesNothing() {
        var jdbc = mock(JdbcTemplate.class);
        var executor = new BundledPluginSchemaExecutorImpl(jdbc);
        assertThrows(IllegalArgumentException.class, () -> executor.executeBundledSchema("p", "1", SQL + "\nDELETE FROM sys_user;"));
        verifyNoInteractions(jdbc);
    }
    @Test void appliedMigrationDoesNotReplayDdl() throws Exception {
        var jdbc = mock(JdbcTemplate.class);
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(SQL.getBytes(StandardCharsets.UTF_8)));
        when(jdbc.queryForList(anyString(), eq("p"), eq("1"))).thenReturn(List.of(Map.of("checksum", hash, "state", "APPLIED")));
        assertTrue(new BundledPluginSchemaExecutorImpl(jdbc).executeBundledSchema("p", "1", SQL).get(0).contains("SKIP"));
        verify(jdbc, never()).update(anyString(), any(Object[].class));
    }
    @Test void changedChecksumRejected() {
        var jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForList(anyString(), eq("p"), eq("1"))).thenReturn(List.of(Map.of("checksum", "wrong", "state", "APPLIED")));
        assertThrows(IllegalStateException.class, () -> new BundledPluginSchemaExecutorImpl(jdbc).executeBundledSchema("p", "1", SQL));
    }
    @Test void interruptedMigrationIsNotRetried() throws Exception {
        var jdbc = mock(JdbcTemplate.class);
        String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(SQL.getBytes(StandardCharsets.UTF_8)));
        when(jdbc.queryForList(anyString(), eq("p"), eq("1"))).thenReturn(List.of(Map.of("checksum", hash, "state", "RUNNING")));
        assertThrows(IllegalStateException.class, () -> new BundledPluginSchemaExecutorImpl(jdbc).executeBundledSchema("p", "1", SQL));
    }
}
