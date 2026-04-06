package com.sxpcwlkj.system.plugin;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 市场「执行插件自带 schema.sql」白名单：仅允许建表/索引/安全 ALTER，禁止 DML 与危险 DDL。
 */
public final class BundledPluginSchemaSqlGuard {

    private static final Pattern SYS_DIR =
            Pattern.compile(
                    "\\b(information_schema|pg_catalog|mysql\\s*\\.|sys\\.|performance_schema)\\b",
                    Pattern.CASE_INSENSITIVE);

    private BundledPluginSchemaSqlGuard() {}

    public static void assertStatementAllowed(String stmt) {
        if (stmt == null || stmt.isBlank()) {
            return;
        }
        String s = stmt.trim();
        if (s.indexOf(';') >= 0) {
            throw new IllegalArgumentException("单条语句不得包含分号");
        }
        if (SYS_DIR.matcher(s).find()) {
            throw new IllegalArgumentException("禁止引用系统目录或危险前缀");
        }
        String u = s.toUpperCase(Locale.ROOT);
        if (u.startsWith("CREATE ")) {
            if (u.startsWith("CREATE TABLE ")
                    || u.startsWith("CREATE INDEX ")
                    || u.startsWith("CREATE UNIQUE INDEX ")
                    || u.startsWith("CREATE FULLTEXT INDEX ")) {
                return;
            }
            throw new IllegalArgumentException("CREATE 仅允许 TABLE / INDEX");
        }
        if (u.startsWith("ALTER TABLE ")) {
            String tail = u.substring("ALTER TABLE ".length()).trim();
            int sp = tail.indexOf(' ');
            if (sp > 0) {
                String rest = tail.substring(sp + 1).trim().toUpperCase(Locale.ROOT);
                if (rest.startsWith("ADD ")
                        || rest.startsWith("ADD COLUMN ")
                        || rest.startsWith("DROP COLUMN ")
                        || rest.startsWith("MODIFY ")
                        || rest.startsWith("CHANGE ")) {
                    return;
                }
            }
            throw new IllegalArgumentException("ALTER TABLE 仅允许 ADD/DROP/MODIFY/CHANGE 等列级变更");
        }
        throw new IllegalArgumentException("仅允许 CREATE TABLE/INDEX 或受限 ALTER TABLE");
    }
}
