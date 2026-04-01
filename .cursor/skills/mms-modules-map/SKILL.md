---
name: mms-modules-map
description: mms-modules 子模块职责与落点速查；新增业务、排查依赖、接入插件 API 时优先查阅。
---

# mms-modules 模块地图

权威列表以主仓 **`mms-modules/pom.xml`** 的 `<modules>` 为准。**依赖 DAG、冒烟用例、压测接口清单** 见 **`version/v1-20260331-脚手架回归与扩展基线.md`**。以下为开发时常用对照；**对外同步**表文在 `mms-doc` [项目简介 — 子模块速览](https://mmsadmin.cn/index/introduction.html#mms-modules-map)。

| 模块 | 落点提示 |
|------|----------|
| `mms-common` | 通用属性（如 `TenantProperties`）、工具、枚举 |
| `mms-plugin-api` | JAR 插件契约：`plugin.json`、`MmsPlugin` SPI、安装路径工具类 |
| `mms-plugin-host` | JAR 插件宿主：扫描 `lib`、ClassLoader、SPI、`/system/pluginHost` |
| `mms-framework` | `BaseServiceImpl` 等与框架协作的基类 |
| `mms-datasource` | MyBatis-Plus、多数据源、**租户拦截器**、`BaseEntity` 填充 |
| `mms-authority` | `LoginObject`、登录态与租户 Redis 读取 |
| `mms-system` | 用户、角色、菜单、字典、租户、配置等系统域 |
| `mms-gen` | 代码生成与模板 |
| `mms-log` | 操作日志 AOP |
| `mms-redis` / `mms-oss` / `mms-sms` / `mms-email` | 集成能力 |
| `mms-mq` / `mms-wx` / `mms-aliyun` / `mms-ai` / `mms-websocket` / `mms-thymeleaf` / `mms-demo` | 按业务选用 |

**管理端入口**：`mms-api-admin` 通过 Maven 聚合依赖；新功能表优先落在 `mms-system` 或生成器配置指定包。
