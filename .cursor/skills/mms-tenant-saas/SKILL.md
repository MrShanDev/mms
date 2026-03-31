---
name: mms-tenant-saas
description: MMS 多租户开关、排除表、LoginObject 租户 ID、与 MyBatis 租户插件行为对齐；关闭租户或排错时使用。
---

# 多租户与 SaaS 基线

## 配置

- **`tenant.enable`**：`true` 注册 `TenantLineInnerInterceptor`；为 `null` 或 `false` 时不注册（`Boolean.TRUE.equals`）。
- **`tenant.column`**：行级租户字段名，默认 `tenant_id`。
- **`tenant.exclusionTable`**：不追加租户条件的表名列表；启动时 **小写、去重** 后为只读 `Set`，查找为 O(1)。
- **表名前缀 `sys_gen_`**：一律忽略租户条件（代码生成临时表），不必全部写入 YAML。

配置源：`mms-api-admin` → `application.yml`；生产 MyBatis 日志见 `application-prod.yml`（`NoLoggingImpl`）。

## 运行时

- **登录 SaaS 校验**（`tenant.enable=true` 时）：`SysLoginServiceImpl#verfyTenement` 校验租户存在且启用；**`expireTime`** 已过期则拒绝登录；若配置了 **`packageId`**，则 **`sys_tenant_package`** 须存在且 `status` 为启用。详见 `mms-doc` [SaaS 现状与缺口](https://mmsadmin.cn/mms-admin/saas-tenant-gap.html)。
- **`LoginObject.getLoginTenant()`**：已登录读 Redis；未登录或 Redis 无值时 **回落 `000000`**，禁止向 SQL 注入 `null` 租户。
- **`MybatisPlusConfig`**：租户表达式对上述结果再次做空/空白兜底。
- **实体填充**：`MybatisPlusMetaObjectHandler` 在已登录时对 `BaseEntity.tenantId` 填充（与 `StpUtil.isLogin()` 一致）。

## 单方法忽略

Mapper 上使用 MyBatis-Plus `@InterceptorIgnore(tenantLine = "true")` 等。

## 文档与联调

在线文档：[多租户](https://mmsadmin.cn/mms-admin/tenant.html)。若文档与代码冲突 **以代码为准** 并更新 `mms-doc`。
