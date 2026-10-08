package com.sxpcwlkj.system.plugin;

import com.sxpcwlkj.plugin.BundledInstallSqlExecutionException;
import com.sxpcwlkj.plugin.BundledInstallSqlResult;
import com.sxpcwlkj.plugin.BundledInstallSqlSupport;
import com.sxpcwlkj.plugin.BundledPluginSchemaExecutor;
import com.sxpcwlkj.plugin.BundledPluginSchemaSupport;
import com.sxpcwlkj.plugin.BundledPluginSqlSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * 执行插件 JAR 内 schema.sql（由宿主拆句后传入）。
 */
@Component
@RequiredArgsConstructor
public class BundledPluginSchemaExecutorImpl implements BundledPluginSchemaExecutor {

    private static final int MAX_STATEMENTS = 500;
    private static final int MAX_STATEMENT_LEN = 64_000;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public BundledInstallSqlResult executeBundledInstallSql(String sql) {
        List<String> lines = new ArrayList<>();
        List<String> insertedFunctionIds = new ArrayList<>();
        List<String> insertedDictIds = new ArrayList<>();
        List<String> insertedDictDataIds = new ArrayList<>();
        List<String> stmts = BundledPluginSqlSplitter.splitStatements(sql);
        if (stmts.isEmpty()) {
            lines.add("（install.sql 无有效 SQL 语句，跳过）");
            return new BundledInstallSqlResult(lines, insertedFunctionIds, insertedDictIds, insertedDictDataIds);
        }
        if (stmts.size() > MAX_STATEMENTS) {
            throw new IllegalArgumentException("install.sql 语句条数超过上限 " + MAX_STATEMENTS);
        }
        int unitCount = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            if (stmt.length() > MAX_STATEMENT_LEN) {
                throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
            }
            List<String> units = BundledInstallSqlSupport.expandToSingleRowInserts(stmt);
            for (String unit : units) BundledPluginInstallSqlGuard.assertStatementAllowed(unit);
            unitCount += units.size();
        }
        if (unitCount > MAX_STATEMENTS) {
            throw new IllegalArgumentException("install.sql 展开后的插入条数超过上限 " + MAX_STATEMENTS);
        }
        int n = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            List<String> units = BundledInstallSqlSupport.expandToSingleRowInserts(stmt);
            for (String unit : units) {
                if (unit.length() > MAX_STATEMENT_LEN) {
                    throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
                }
                BundledPluginInstallSqlGuard.assertStatementAllowed(unit);
                n++;
                BundledInstallSqlSupport.InstallSqlInsertKind kind =
                        BundledInstallSqlSupport.classifyInstallInsert(unit);
                if (kind == BundledInstallSqlSupport.InstallSqlInsertKind.UNSUPPORTED) {
                    throw new IllegalArgumentException("install.sql 含不支持的 INSERT（仅允许 sys_function/sys_dict/sys_dict_data）");
                }
                String rowId = BundledInstallSqlSupport.extractFirstInsertRowId(unit);
                if (rowId != null && !rowId.isBlank()) {
                    String idTrim = rowId.trim();
                    String skipWhat = null;
                    switch (kind) {
                        case SYS_FUNCTION -> {
                            if (sysFunctionIdExists(idTrim)) {
                                skipWhat = "sys_function.id";
                            }
                        }
                        case SYS_DICT -> {
                            if (sysDictIdExists(idTrim)) {
                                skipWhat = "sys_dict.id";
                            }
                        }
                        case SYS_DICT_DATA -> {
                            if (sysDictDataIdExists(idTrim)) {
                                skipWhat = "sys_dict_data.id";
                            }
                        }
                        default -> {
                        }
                    }
                    if (skipWhat != null) {
                        lines.add(
                                String.format(
                                        Locale.ROOT,
                                        "[SKIP] #%d %s 已存在，已跳过 — %s",
                                        n,
                                        skipWhat,
                                        preview(unit)));
                        continue;
                    }
                }
                long t0 = System.currentTimeMillis();
                try {
                    jdbcTemplate.execute((Connection c) -> {
                        try (Statement st = c.createStatement()) {
                            st.execute(unit);
                        }
                        return null;
                    });
                    lines.add(String.format(Locale.ROOT, "[OK] #%d (%d ms) %s", n, System.currentTimeMillis() - t0, preview(unit)));
                    if (rowId != null && !rowId.isBlank()) {
                        String idTrim = rowId.trim();
                        switch (kind) {
                            case SYS_FUNCTION -> insertedFunctionIds.add(idTrim);
                            case SYS_DICT -> insertedDictIds.add(idTrim);
                            case SYS_DICT_DATA -> insertedDictDataIds.add(idTrim);
                            default -> {
                            }
                        }
                    }
                } catch (Exception e) {
                    if (isDuplicateKeyException(e)) {
                        lines.add(String.format(Locale.ROOT, "[SKIP] #%d 主键或唯一约束重复，已跳过 — %s", n, preview(unit)));
                        continue;
                    }
                    lines.add(String.format(Locale.ROOT, "[FAIL] #%d %s — %s", n, preview(unit), e.getMessage()));
                    throw new BundledInstallSqlExecutionException(
                            new ArrayList<>(lines),
                            new ArrayList<>(insertedFunctionIds),
                            new ArrayList<>(insertedDictIds),
                            new ArrayList<>(insertedDictDataIds),
                            "install.sql 第 " + n + " 条失败: " + e.getMessage(),
                            e);
                }
            }
        }
        lines.add("install.sql 共处理 " + n + " 条插入（含按主键预检跳过与重复键跳过）。");
        return new BundledInstallSqlResult(lines, insertedFunctionIds, insertedDictIds, insertedDictDataIds);
    }

    private boolean sysFunctionIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM sys_function WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sysDictIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_dict WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sysDictDataIdExists(String id) {
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM sys_dict_data WHERE id = ?", Number.class, id);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isDuplicateKeyException(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof java.sql.SQLException se) {
                String state = se.getSQLState();
                if ("23000".equals(state) || "23505".equals(state)) {
                    return true;
                }
                int code = se.getErrorCode();
                if (code == 1062 || code == 2627) {
                    return true;
                }
            }
            String m = t.getMessage();
            if (m != null) {
                String lower = m.toLowerCase(Locale.ROOT);
                if (lower.contains("duplicate entry")
                        || lower.contains("duplicate key")
                        || lower.contains("unique constraint")
                        || lower.contains("already exists")) {
                    return true;
                }
            }
        }
        return false;
    }

    /** MySQL DDL 自动提交：失败/中断保留记录，禁止静默重放。业务表卸载时保留。 */
    @Override
    public synchronized List<String> executeBundledSchema(String pluginId, String version, String sql) {
        List<String> statements = BundledPluginSqlSplitter.splitStatements(sql);
        if (statements.size() > MAX_STATEMENTS) throw new IllegalArgumentException("SQL 语句过多");
        for (String statement : statements) {
            if (statement.length() > MAX_STATEMENT_LEN) throw new IllegalArgumentException("SQL 语句过长");
            BundledPluginSchemaSqlGuard.assertStatementAllowed(statement.trim());
        }
        String checksum;
        try {
            checksum = java.util.HexFormat.of().formatHex(java.security.MessageDigest.getInstance("SHA-256")
                    .digest(sql.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS sys_plugin_schema_history ("
                + "plugin_id VARCHAR(191) NOT NULL, plugin_version VARCHAR(128) NOT NULL, "
                + "checksum CHAR(64) NOT NULL, state VARCHAR(16) NOT NULL, "
                + "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "PRIMARY KEY(plugin_id, plugin_version))");
        var records = jdbcTemplate.queryForList(
                "SELECT checksum, state FROM sys_plugin_schema_history WHERE plugin_id=? AND plugin_version=?",
                pluginId, version);
        if (!records.isEmpty()) {
            var record = records.get(0);
            if (!checksum.equals(record.get("checksum"))) throw new IllegalStateException("同版本 schema.sql 校验和变化，必须发布新版本");
            if ("APPLIED".equals(record.get("state"))) return List.of("[SKIP] 该版本数据库脚本已执行");
            throw new IllegalStateException("数据库迁移失败或中断，需核对实际结构并恢复执行记录，禁止自动重放");
        }
        // 同一插件的新版本可能只升级 Java/UI，完全相同的 DDL 不再执行。
        Integer previouslyApplied = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_plugin_schema_history WHERE plugin_id=? AND checksum=? AND state='APPLIED'",
                Integer.class, pluginId, checksum);
        if (previouslyApplied != null && previouslyApplied > 0) {
            jdbcTemplate.update("INSERT INTO sys_plugin_schema_history(plugin_id,plugin_version,checksum,state) VALUES (?,?,?,?)",
                    pluginId, version, checksum, "APPLIED");
            return List.of("[SKIP] 相同数据库脚本已由该插件其他版本执行");
        }
        jdbcTemplate.update("INSERT INTO sys_plugin_schema_history(plugin_id,plugin_version,checksum,state) VALUES (?,?,?,?)",
                pluginId, version, checksum, "RUNNING");
        try {
            List<String> result = executeBundledSchema(sql);
            jdbcTemplate.update("UPDATE sys_plugin_schema_history SET state='APPLIED' WHERE plugin_id=? AND plugin_version=?", pluginId, version);
            return result;
        } catch (RuntimeException failure) {
            try {
                jdbcTemplate.update("UPDATE sys_plugin_schema_history SET state='FAILED' WHERE plugin_id=? AND plugin_version=?", pluginId, version);
            } catch (RuntimeException recordFailure) { failure.addSuppressed(recordFailure); }
            throw failure;
        }
    }

    @Override
    public List<String> executeBundledSchema(String sql) {
        List<String> lines = new ArrayList<>();
        List<String> stmts = BundledPluginSqlSplitter.splitStatements(sql);
        if (stmts.isEmpty()) {
            lines.add("（无有效 SQL 语句，跳过）");
            return lines;
        }
        if (stmts.size() > MAX_STATEMENTS) {
            throw new IllegalArgumentException("语句条数超过上限 " + MAX_STATEMENTS);
        }
        for (String raw : stmts) {
            if (raw.length() > MAX_STATEMENT_LEN) throw new IllegalArgumentException("单条语句过长");
            BundledPluginSchemaSqlGuard.assertStatementAllowed(raw.trim());
        }
        int n = 0;
        for (String raw : stmts) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            if (stmt.length() > MAX_STATEMENT_LEN) {
                throw new IllegalArgumentException("单条语句过长（>" + MAX_STATEMENT_LEN + "）");
            }
            BundledPluginSchemaSqlGuard.assertStatementAllowed(stmt);
            Optional<String> createTable = BundledPluginSchemaSupport.extractCreateTableName(stmt);
            if (createTable.isPresent() && physicalTableExists(createTable.get())) {
                n++;
                lines.add(
                        String.format(
                                Locale.ROOT,
                                "[SKIP] #%d 物理表已存在，跳过 CREATE TABLE — %s",
                                n,
                                preview(stmt)));
                continue;
            }
            n++;
            long t0 = System.currentTimeMillis();
            try {
                jdbcTemplate.execute((Connection c) -> {
                    try (Statement st = c.createStatement()) {
                        st.execute(stmt);
                    }
                    return null;
                });
                lines.add(String.format(Locale.ROOT, "[OK] #%d (%d ms) %s", n, System.currentTimeMillis() - t0, preview(stmt)));
            } catch (Exception e) {
                lines.add(String.format(Locale.ROOT, "[FAIL] #%d %s — %s", n, preview(stmt), e.getMessage()));
                throw new IllegalStateException("执行第 " + n + " 条失败: " + e.getMessage(), e);
            }
        }
        lines.add("共执行 " + n + " 条语句。");
        return lines;
    }

    private boolean physicalTableExists(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            return false;
        }
        String tn = tableName.trim();
        try {
            Number c =
                    jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND LOWER(table_name) = LOWER(?)",
                            Number.class,
                            tn);
            return c != null && c.longValue() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static String preview(String stmt) {
        String one = stmt.replace('\n', ' ').trim();
        return one.length() > 160 ? one.substring(0, 157) + "…" : one;
    }
}
