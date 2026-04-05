package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginException;
import com.sxpcwlkj.plugin.PluginSchemaAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 受限 DDL：单语句、经 {@link PluginSchemaSqlGuard} 校验后交 JdbcTemplate 执行。
 */
@Slf4j
public final class JdbcPluginSchemaAccess implements PluginSchemaAccess {

    private static final int DDL_AUDIT_MAX_LEN = 800;

    private final JdbcTemplate jdbcTemplate;
    private final String pluginId;
    private final String pluginVersion;
    private final String tablePrefix;
    private final boolean auditEnabled;

    public JdbcPluginSchemaAccess(
            JdbcTemplate jdbcTemplate,
            String pluginId,
            String pluginVersion,
            String tablePrefix,
            boolean auditEnabled) {
        this.jdbcTemplate = jdbcTemplate;
        this.pluginId = pluginId == null ? "" : pluginId;
        this.pluginVersion = pluginVersion == null ? "" : pluginVersion;
        this.tablePrefix = tablePrefix == null ? "" : tablePrefix.trim();
        this.auditEnabled = auditEnabled;
    }

    @Override
    public void executeDdl(String sql) {
        PluginSchemaSqlGuard.assertDdlAllowed(sql, tablePrefix);
        audit(sql);
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            throw new PluginException("DDL 执行失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> listOwnedTables() {
        if (tablePrefix.isBlank()) {
            throw new PluginException("pluginTablePrefix 不能为空");
        }
        String pfxLower = tablePrefix.toLowerCase(Locale.ROOT);
        return jdbcTemplate.execute(
                (ConnectionCallback<List<String>>)
                        con -> {
                            List<String> names = new ArrayList<>();
                            DatabaseMetaData md = con.getMetaData();
                            String catalog = con.getCatalog();
                            try (ResultSet rs =
                                    md.getTables(catalog, null, "%", new String[] {"TABLE"})) {
                                while (rs.next()) {
                                    String t = rs.getString("TABLE_NAME");
                                    if (t != null
                                            && t.toLowerCase(Locale.ROOT).startsWith(pfxLower)) {
                                        names.add(t);
                                    }
                                }
                            }
                            names.sort(Comparator.naturalOrder());
                            return List.copyOf(names);
                        });
    }

    private void audit(String sql) {
        if (!auditEnabled) {
            return;
        }
        String s = sql == null ? "" : sql.replace('\n', ' ').replace('\r', ' ');
        if (s.length() > DDL_AUDIT_MAX_LEN) {
            s = s.substring(0, DDL_AUDIT_MAX_LEN) + "...";
        }
        log.warn("pluginSchemaAccess DDL pluginId={} version={} sql={}", pluginId, pluginVersion, s);
    }
}
