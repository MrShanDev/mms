---
name: mms-scaffold-phases
description: 脚手架分阶段路线（缺陷排查、性能、文档、技能沉淀）与 mms-plugin-api P0 边界；规划迭代或向团队同步路线图时使用。
---

# 脚手架分阶段路线（落地对照）

以下与主仓实现及 **`mms-doc`** [脚手架演进与质量路线](https://mmsadmin.cn/mms-admin/scaffold-evolution.html) 对齐。

## 1. 缺陷排查

- 配置：`tenant.exclusionTable` 去重；`mybatis-plus` 默认 `Slf4jImpl`（生产仍 `NoLoggingImpl`）。
- 租户：`LoginObject`、`MybatisPlusConfig` 租户 ID 兜底；`enable` 空值安全。

## 2. 性能

- 租户排除表启动期归一化为 `Set`；忽略 `sys_gen_` 前缀表。

## 3. 文档

- 由 **`mms-doc-sync`** 技能维护：`docs/mms-admin/modules-map.md`、`scaffold-evolution.md`、`tenant.md` 与 `docs/log/index.md`、`config.mts` 侧栏。

## 4. Cursor 技能

- `mms-modules-map`、`mms-tenant-saas`、本文件；主开发规范见 **`mms-kills`（mms-dev-standards）**。

## 5. JAR 插件（P0 / P1）

- **`mms-plugin-api`（P0）**：`plugin.json`、`MmsPlugin`、SPI、`PluginInstallationLayout`。
- **`mms-plugin-host`（P1）**：`mms.plugin.*`、扫描 `lib/*.jar`、**`/status` `/health` `/install`（multipart）`/reload`**（`super_admin`）；示例 JAR **`mms-plugin-sample-health`**（`mvn -pl mms-plugins/mms-plugin-sample-health package`）。完整阶段表见 [JAR 插件分阶段](https://mmsadmin.cn/mms-admin/plugin-jar-phases.html)。
