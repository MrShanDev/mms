package com.sxpcwlkj.plugin.host.internal;

import com.sxpcwlkj.plugin.PluginBackupAccess;
import com.sxpcwlkj.plugin.PluginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * MySQL 为主的逻辑备份：SHOW CREATE TABLE + 行级 INSERT。
 */
@Slf4j
public final class JdbcPluginBackupAccess implements PluginBackupAccess {

    private static final Pattern SAFE_TABLE = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final int AUDIT_SQL_MAX = 500;

    private final JdbcTemplate jdbcTemplate;
    private final boolean auditEnabled;
    private final String pluginId;
    private final String pluginVersion;

    public JdbcPluginBackupAccess(
            JdbcTemplate jdbcTemplate, boolean auditEnabled, String pluginId, String pluginVersion) {
        this.jdbcTemplate = jdbcTemplate;
        this.auditEnabled = auditEnabled;
        this.pluginId = pluginId == null ? "" : pluginId;
        this.pluginVersion = pluginVersion == null ? "" : pluginVersion;
    }

    @Override
    public List<String> listCatalogTables() {
        return jdbcTemplate.execute(
                (ConnectionCallback<List<String>>)
                        con -> {
                            requireMysql(con);
                            String catalog = con.getCatalog();
                            DatabaseMetaData md = con.getMetaData();
                            List<String> names = new ArrayList<>();
                            try (ResultSet rs = md.getTables(catalog, null, "%", new String[] {"TABLE"})) {
                                while (rs.next()) {
                                    String n = rs.getString("TABLE_NAME");
                                    if (n != null && SAFE_TABLE.matcher(n).matches()) {
                                        names.add(n);
                                    }
                                }
                            }
                            Collections.sort(names);
                            return names;
                        });
    }

    @Override
    public byte[] exportLogicalSql(List<String> tables, boolean gzip) throws Exception {
        List<String> target = resolveTables(tables);
        String sql = buildExportScript(target);
        byte[] raw = sql.getBytes(StandardCharsets.UTF_8);
        audit("export logical backup tables=" + target.size() + " bytes=" + raw.length);
        if (!gzip) {
            return raw;
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream(Math.min(raw.length + 64, raw.length * 2));
        try (GZIPOutputStream gz = new GZIPOutputStream(bout)) {
            gz.write(raw);
        }
        return bout.toByteArray();
    }

    @Override
    public void importLogicalSql(byte[] data, boolean gzip) throws Exception {
        if (data == null || data.length == 0) {
            throw new PluginException("备份数据为空");
        }
        byte[] raw = data;
        if (gzip) {
            try (GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data))) {
                raw = in.readAllBytes();
            }
        }
        String script = new String(raw, StandardCharsets.UTF_8);
        audit("import logical backup scriptChars=" + script.length());
        List<String> statements = splitSqlStatements(script);
        for (String stmt : statements) {
            assertImportAllowed(stmt);
            jdbcTemplate.execute(stmt);
        }
    }

    private List<String> resolveTables(List<String> tables) {
        if (tables == null || tables.isEmpty()) {
            return listCatalogTables();
        }
        List<String> out = new ArrayList<>();
        for (String t : tables) {
            if (t == null || t.isBlank()) {
                continue;
            }
            String n = t.trim();
            if (!SAFE_TABLE.matcher(n).matches()) {
                throw new PluginException("非法表名: " + n);
            }
            out.add(n);
        }
        if (out.isEmpty()) {
            return listCatalogTables();
        }
        return out;
    }

    private String buildExportScript(List<String> tables) {
        StringBuilder sb = new StringBuilder();
        sb.append("-- MMS plugin logical backup\n");
        sb.append("SET NAMES utf8mb4;\n");
        sb.append("SET FOREIGN_KEY_CHECKS=0;\n");
        jdbcTemplate.execute(
                (ConnectionCallback<Void>)
                        con -> {
                            try {
                                requireMysql(con);
                                for (String t : tables) {
                                    sb.append("\n-- TABLE ").append(t).append("\n");
                                    sb.append("DROP TABLE IF EXISTS `").append(t).append("`;\n");
                                    String create = readShowCreateTable(con, t);
                                    sb.append(create).append(";\n");
                                    appendInserts(con, t, sb);
                                }
                            } catch (SQLException e) {
                                throw new PluginException("导出构建失败: " + e.getMessage(), e);
                            }
                            return null;
                        });
        sb.append("SET FOREIGN_KEY_CHECKS=1;\n");
        return sb.toString();
    }

    private static void requireMysql(Connection con) throws SQLException {
        String p = con.getMetaData().getDatabaseProductName();
        if (p == null || !p.toLowerCase(Locale.ROOT).contains("mysql")) {
            throw new PluginException("逻辑备份/还原当前仅支持 MySQL 系列（当前 product=" + p + "）");
        }
    }

    private static String readShowCreateTable(Connection con, String table) throws SQLException {
        try (var st = con.createStatement();
                ResultSet rs = st.executeQuery("SHOW CREATE TABLE `" + table + "`")) {
            if (!rs.next()) {
                throw new PluginException("SHOW CREATE TABLE 无结果: " + table);
            }
            return rs.getString(2);
        }
    }

    private static void appendInserts(Connection con, String table, StringBuilder sb) throws SQLException {
        try (var st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM `" + table + "`")) {
            ResultSetMetaData md = rs.getMetaData();
            int cc = md.getColumnCount();
            List<String> cols = new ArrayList<>();
            for (int i = 1; i <= cc; i++) {
                cols.add(md.getColumnLabel(i));
            }
            while (rs.next()) {
                sb.append("INSERT INTO `").append(table).append("` (");
                for (int i = 0; i < cols.size(); i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append('`').append(cols.get(i).replace("`", "``")).append('`');
                }
                sb.append(") VALUES (");
                for (int i = 1; i <= cc; i++) {
                    if (i > 1) {
                        sb.append(',');
                    }
                    sb.append(sqlLiteral(rs.getObject(i), md.getColumnType(i)));
                }
                sb.append(");\n");
            }
        }
    }

    private static String sqlLiteral(Object v, int sqlType) throws SQLException {
        if (v == null) {
            return "NULL";
        }
        if (v instanceof Boolean b) {
            return b ? "1" : "0";
        }
        if (v instanceof Number n && !(v instanceof BigDecimal)) {
            return n.toString();
        }
        if (v instanceof BigDecimal bd) {
            return bd.toPlainString();
        }
        if (v instanceof byte[] bytes) {
            return mysqlHexLiteral(bytes);
        }
        if (v instanceof Timestamp src) {
            return "'" + src.toString().replace("'", "''") + "'";
        }
        if (v instanceof java.sql.Date d) {
            return "'" + d.toString() + "'";
        }
        if (v instanceof java.sql.Time time) {
            return "'" + time.toString() + "'";
        }
        if (v instanceof LocalDateTime ldt) {
            return "'" + ldt.toString().replace("'", "''") + "'";
        }
        if (v instanceof LocalDate ld) {
            return "'" + ld + "'";
        }
        if (v instanceof LocalTime lt) {
            return "'" + lt + "'";
        }
        if (sqlType == Types.BIT || sqlType == Types.TINYINT || sqlType == Types.SMALLINT
                || sqlType == Types.INTEGER
                || sqlType == Types.BIGINT) {
            if (v instanceof String s) {
                return s;
            }
            return String.valueOf(v);
        }
        String s = v.toString();
        return "'" + escapeSqlString(s) + "'";
    }

    private static String mysqlHexLiteral(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2 + 3);
        sb.append("X'");
        for (byte b : bytes) {
            sb.append(String.format(Locale.ROOT, "%02X", b));
        }
        sb.append('\'');
        return sb.toString();
    }

    private static String escapeSqlString(String s) {
        return s.replace("\\", "\\\\").replace("'", "''");
    }

    static List<String> splitSqlStatements(String script) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inSingle = false;
        for (int i = 0; i < script.length(); i++) {
            char c = script.charAt(i);
            if (c == '\'' && !inSingle) {
                inSingle = true;
                cur.append(c);
            } else if (c == '\'' && inSingle) {
                if (i + 1 < script.length() && script.charAt(i + 1) == '\'') {
                    cur.append("''");
                    i++;
                } else {
                    inSingle = false;
                    cur.append(c);
                }
            } else if (c == ';' && !inSingle) {
                String block = cur.toString().trim();
                if (!block.isEmpty() && !block.startsWith("--")) {
                    out.add(block);
                }
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        String tail = cur.toString().trim();
        if (!tail.isEmpty() && !tail.startsWith("--")) {
            out.add(tail);
        }
        return out;
    }

    static void assertImportAllowed(String stmt) {
        String t = stmt.trim();
        if (t.isEmpty()) {
            return;
        }
        String u = t.toUpperCase(Locale.ROOT);
        if (u.startsWith("SET ") || u.startsWith("LOCK TABLES") || u.startsWith("UNLOCK TABLES")) {
            return;
        }
        if (u.startsWith("CREATE TABLE ")) {
            return;
        }
        if (u.startsWith("DROP TABLE IF EXISTS ")) {
            return;
        }
        if (u.startsWith("INSERT INTO ") || u.startsWith("INSERT IGNORE INTO ")
                || u.startsWith("REPLACE INTO ")) {
            return;
        }
        throw new PluginException(
                "导入语句不在白名单（仅允许 SET/LOCK/CREATE TABLE/DROP TABLE IF EXISTS/INSERT）: "
                        + t.substring(0, Math.min(120, t.length())));
    }

    private void audit(String msg) {
        if (!auditEnabled) {
            return;
        }
        String m = msg.length() > AUDIT_SQL_MAX ? msg.substring(0, AUDIT_SQL_MAX) + "..." : msg;
        log.warn("pluginBackupAccess pluginId={} version={} — {}", pluginId, pluginVersion, m);
    }
}
