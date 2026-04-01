package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginDataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public final class JdbcPluginDataAccess implements PluginDataAccess {

    private static final int SQL_AUDIT_MAX_LEN = 500;

    private final JdbcTemplate jdbcTemplate;
    private final String pluginId;
    private final String pluginVersion;
    private final String tablePrefix;
    private final Set<String> allowedFullTableNamesLowercase;
    private final boolean auditEnabled;
    private final boolean autoTenant;
    private final String tenantColumn;
    private final Optional<String> currentTenantId;

    public JdbcPluginDataAccess(
            JdbcTemplate jdbcTemplate,
            String pluginId,
            String pluginVersion,
            String tablePrefix,
            Set<String> allowedFullTableNamesLowercase,
            int queryTimeoutSeconds,
            boolean auditEnabled,
            boolean autoTenant,
            String tenantColumn,
            Optional<String> currentTenantId) {
        this.jdbcTemplate = jdbcTemplate;
        this.pluginId = pluginId == null ? "" : pluginId;
        this.pluginVersion = pluginVersion == null ? "" : pluginVersion;
        this.tablePrefix = tablePrefix;
        this.allowedFullTableNamesLowercase = allowedFullTableNamesLowercase;
        this.auditEnabled = auditEnabled;
        this.autoTenant = autoTenant;
        this.tenantColumn = tenantColumn == null ? "tenant_id" : tenantColumn;
        this.currentTenantId = currentTenantId == null ? Optional.empty() : currentTenantId;
        if (queryTimeoutSeconds > 0) {
            jdbcTemplate.setQueryTimeout(queryTimeoutSeconds);
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        PluginSqlGuard.assertSelect(sql, tablePrefix, allowedFullTableNamesLowercase);
        PluginTenantSqlAugmenter.Result tenant =
                autoTenant
                        ? PluginTenantSqlAugmenter.applyIfEnabled(
                                sql, args, tenantColumn, currentTenantId.orElse(null))
                        : new PluginTenantSqlAugmenter.Result(sql, args, false);
        audit("query", tenant.sql(), tenant.args());
        return jdbcTemplate.queryForList(tenant.sql(), tenant.args());
    }

    @Override
    public int update(String sql, Object... args) {
        PluginSqlGuard.assertWrite(sql, tablePrefix, allowedFullTableNamesLowercase);
        PluginTenantSqlAugmenter.Result tenant =
                autoTenant
                        ? PluginTenantSqlAugmenter.applyIfEnabled(
                                sql, args, tenantColumn, currentTenantId.orElse(null))
                        : new PluginTenantSqlAugmenter.Result(sql, args, false);
        audit("update", tenant.sql(), tenant.args());
        return jdbcTemplate.update(tenant.sql(), tenant.args());
    }

    private void audit(String op, String sql, Object[] args) {
        if (!auditEnabled) {
            return;
        }
        int argLen = args == null ? 0 : args.length;
        String s = sql == null ? "" : sql.replace('\n', ' ').replace('\r', ' ');
        if (s.length() > SQL_AUDIT_MAX_LEN) {
            s = s.substring(0, SQL_AUDIT_MAX_LEN) + "...";
        }
        log.info(
                "pluginDataAccess pluginId={} version={} op={} argCount={} sql={}",
                pluginId,
                pluginVersion,
                op,
                argLen,
                s);
    }
}
