---
name: mms-scaffold-phases
description: MMS 脚手架质量与演进路线（缺陷排查、性能基线、文档维护约定、Cursor 技能索引、插件契约与宿主模块）；仅保留在 Cursor 技能，不在 mms-doc 公开。
---

# 脚手架演进与质量路线（落地对照）

MMS 定位为可扩展的企业/个人脚手架：管理端（`mms-api-admin` + `mms-ui`）、可选开放端（`mms-servers*`）、多租户可开关，并逐步增强 **JAR 插件扩展**（`mms-plugin-api` / `mms-plugin-host`）。

**本技能及下表「能力矩阵」不在在线文档站单独成页**；对外 **`mms-modules` 子模块表** 见 [项目简介](https://mmsadmin.cn/index/introduction.html#mms-modules-map)，另见 [多租户](https://mmsadmin.cn/mms-admin/tenant.html)、[JAR 插件入门](https://mmsadmin.cn/mms-admin/plugin-jar-phases.html)、[JAR 开发指南](https://mmsadmin.cn/mms-admin/plugin-develop.html)、[SaaS 现状与缺口](https://mmsadmin.cn/mms-admin/saas-tenant-gap.html)。

---

## 1. 缺陷排查与行为对齐（持续）

- **配置**：`mms-api-admin` 的 `application.yml` 中 `tenant.exclusionTable` 已与运行时代码一致；`mybatis-plus.configuration.logImpl` 默认 **Slf4j**；生产由 `application-prod.yml` 使用 `NoLoggingImpl`。
- **租户 ID 为空**：`LoginObject#getLoginTenant` 在 Redis 未命中时回落为 `000000`；MyBatis 租户拦截器对空字符串再次兜底。
- **租户开关**：`tenant.enable` 为空时按未开启处理（与 `Boolean.TRUE.equals` 一致），避免空指针拆箱。

单表、方法级忽略租户仍可使用 `@InterceptorIgnore(tenantLine = "true")` 等，与租户技能一致。

---

## 2. 性能（已做基线 + 可持续项）

- **租户排除表**：启动时将 `exclusionTable` **归一化（小写、去重）** 为只读 `Set`，`ignoreTable` 为 O(1) 查找；表名以 `sys_gen_` 为前缀的表统一忽略，与代码生成临时表习惯一致。

可持续（按需排期）：热点 SQL 与索引、`PaginationInnerInterceptor` 单页上限、字典/权限缓存、导出大文件流式处理等。

---

## 3. 文档同步（mms-doc）

- **子模块表（对外）**：`docs/index/introduction.md` 内 **Maven 子模块职责** 小节（锚点 `#mms-modules-map`）
- **多租户**：`docs/mms-admin/tenant.md`
- **修订记录**：`docs/log/index.md` 顶部「文档修订」
- 站点菜单与页面增删由 **`mms-doc-sync`** 技能约束；**不再**维护 `scaffold-evolution.md`、`index/scaffold-capability-matrix.md`、`mms-admin/modules-map.md` 独立页面（子模块表并入 **`index/introduction`**；能力矩阵见本节 **§7**）。

---

## 4. Cursor 技能索引

主仓 `.cursor/skills/` 中与脚手架相关：

| 技能 | 用途 |
|------|------|
| `mms-modules-map` | 模块落点速查 |
| `mms-tenant-saas` | 租户配置、`LoginObject`、排除表、关闭租户 |
| `mms-scaffold-phases` | **本文件**：路线与质量对照 |
| `mms-kills`（mms-dev-standards） | CRUD、代码生成、多端 API 规范 |
| `mms-plugin` | 插件宿主、市场、ClassLoader |
| `mms-doc-sync` | 文档站与主仓对齐流程 |

---

## 5. JAR 插件（能力与文档入口）

- **`mms-plugin-api`（契约）**：`plugin.json`、SPI、`META-INF/mms/plugin.example.json`、`PluginInstallationLayout`。
- **`mms-plugin-host`（宿主）**：`mms.plugin.*`、扫描 `lib`、`URLClassLoader`、超级管理员接口 `status` / `manifests` / `health` / `install` / `reload`。

示例：`mms-plugins/mms-plugin-sample-health`，`mvn -pl mms-plugins/mms-plugin-sample-health -am package -DskipTests`。

对外说明：[JAR 插件入门](https://mmsadmin.cn/mms-admin/plugin-jar-phases.html)、[JAR 开发指南](https://mmsadmin.cn/mms-admin/plugin-develop.html)。

---

## 6. 使用场景

规划迭代、向团队同步「已落地 / 待排期」、排障时对照租户与性能基线、**选型裁剪**时查阅 **本 SKILL**；勿依赖已下线的 `scaffold-evolution`、`scaffold-capability-matrix` 文档路由。

---

## 7. 能力矩阵（选型 / 裁剪）

与 **MMS 主仓**（`mms-api-admin`、`mms-modules`、`mms-ui`）对齐的粗粒度能力表；更细的模块依赖 DAG 见主仓 **`version/v1-20260331-脚手架回归与扩展基线.md`**。

| 能力域 | 能力点 | 默认 / 模块 | 说明 |
|--------|--------|-------------|------|
| 运行时 | JDK / Spring Boot | 根 `pom` 约定 | 与 `revision`、`spring-boot.version` 一致 |
| 管理端 HTTP | 聚合启动 | `mms-api-admin` | 依赖 system、gen、plugin-host（可选） |
| 认证 | Sa-Token | `mms-authority` | 与 Redis 会话、多端 device |
| 持久化 | MyBatis-Plus、多数据源 | `mms-datasource` | 租户行插件、演示模式拦截器 |
| 业务基类 | 分页、校验 | `mms-framework` | Excel、Actuator、Undertow |
| 系统域 | 用户/角色/菜单/字典/租户 | `mms-system` | 登录、套餐校验（SaaS） |
| 代码生成 | 模板与生成器 | `mms-gen` | 依赖 framework、log |
| 操作日志 | AOP | `mms-log` | |
| 缓存 | Redis | `mms-redis` | |
| 对象存储 | OSS 抽象 | `mms-oss` | 可选 |
| 短信 / 邮件 / 微信 | 集成 | `mms-sms`、`mms-email`、`mms-wx` | 可选 |
| 消息 / WS | 集成 | `mms-mq`、`mms-websocket` | 可选 |
| 插件契约 | JAR SPI | `mms-plugin-api` | `plugin.json`、SPI |
| 插件宿主 | ClassLoader 加载 | `mms-plugin-host` | status / health / install / reload |

**多租户（摘要）**：`tenant.enable`；排除表与 `sys_gen_` 规则见 `mms-doc` [多租户](https://mmsadmin.cn/mms-admin/tenant.html)；缺口见 [SaaS 现状与缺口](https://mmsadmin.cn/mms-admin/saas-tenant-gap.html)。

**JAR 插件（对外文档）**：[JAR 插件入门](https://mmsadmin.cn/mms-admin/plugin-jar-phases.html)、[JAR 开发指南](https://mmsadmin.cn/mms-admin/plugin-develop.html)、[JAR 插件路由协议](https://mmsadmin.cn/mms-ui/plugin-route-protocol.html)；示例 `mms-plugins/mms-plugin-sample-health`。
