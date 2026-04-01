---
name: mms-plugin
description: MMS JAR 插件（mms-plugin-api / mms-plugin-host）、插件市场页（mms-ui）、sys_plugins 与 pluginMarket/removeCatalog、磁盘布局探测 diskLayoutWarning、宿主 status 字段、启动期 Sa-Token 与多租户、SPI 与 ClassLoader。用户讨论插件安装/卸载/市场卡片/根目录异常/JAR 未找到/ApplicationReady 加载失败时使用。
---

# MMS JAR 插件（宿主 · 契约 · 开发）

## 何时阅读本技能

- 实现或排查 **独立 JAR 插件**、**插件市场 / sys_plugins**、**上传安装 / 激活版本 / 卸载**。
- 澄清 **插件 ClassLoader** 与 **能否依赖 mms-common / mms-system**。
- **更新在线文档**时与 **`.cursor/skills/mms-doc-sync/SKILL.md`** 配合：正文在同级 `mms-doc`，路由见下文「文档」。

## 模块与代码位置（主仓 `mms-modules`）

| 模块 | 职责 |
|------|------|
| **`mms-plugin-api`** | 契约：`MmsPlugin` SPI、`PluginDescriptor` / `plugin.json` 解析与校验、`PluginInstallationLayout`、`PluginHealthContributor`（可选）、`META-INF/mms/plugin.example.json` |
| **`mms-plugin-host`** | 宿主：`PluginLifecycleManager`（扫描磁盘、`lib/*.jar`、URLClassLoader、安装/重载/卸载）、`PluginHostController` 运维 API、`PluginHostProperties`（`mms.plugin.*`） |
| **`mms-plugin-sample-health`** | **示例插件**：仅 `provided` 依赖 `mms-plugin-api`，演示 SPI + health；**不**随 `mms-api-admin` 打包 |

管理端聚合已引入宿主时，运维接口挂在本进程内（路径见下）。

## 配置（`mms-api-admin` 的 yml）

前缀 **`mms.plugin`**（`PluginHostProperties`）：

| 键 | 含义 |
|----|------|
| `enabled` | 默认 `false`；`true` 时启动/重载会扫描并加载插件 |
| `root-dir` | 插件根目录；未配时常为 `${user.dir}` 下的 `mms-plugins`（以代码与配置为准） |
| `host-mms-revision` | 与根 `pom` 的 **`revision`** 对齐，用于校验 `plugin.json` 中 `requiresMms` |

## 磁盘布局（安装约定）

由 **`PluginInstallationLayout`** 描述：

- `<pluginsRoot>/<safePluginId>/<safeVersion>/**`
- **`lib/`**：插件及依赖 JAR（宿主从此加载）
- **`data/`**、**`tmp/`**：运行时数据与临时文件（插件应限制写入范围）
- 根下可有 **`plugin.json`** 描述副本（与 JAR 内 `META-INF/mms/plugin.json` 对齐）

`safeSegment` 会将 `id/version` 中的 `/` `\` `:` 等替换为 `_`。

## JAR 内必备元数据

- 描述文件首选：**`META-INF/mms/plugin.json`**（或兼容根路径 `plugin.json`，见 `PluginConstants`）
- SPI：**`META-INF/services/com.sxpcwlkj.plugin.MmsPlugin`** → 实现类全限定名（每行一个）
- `entryClass` 与 `MmsPlugin` 实现一致时，宿主在加载后调用 `onLoad` / `onUnload`

校验逻辑在 **`PluginDescriptorValidator`**；示例 JSON 在 **`mms-plugin-api/src/main/resources/META-INF/mms/plugin.example.json`**。

## 运维 HTTP API（仅超级管理员）

`PluginHostController`：**`@RequestMapping("system/pluginHost")`**，方法均需 **`@SaCheckRole("super_admin")`**：

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/status` | 开关、配置 `rootDir`、`hostMmsRevision`、**`resolvedPluginsRoot`**（解析后的绝对路径）、**`pluginsRootReady`**（是否为已存在目录）、已加载摘要 |
| POST | `/reload` | 全量重载插件 |
| GET | `/health` | 各插件健康行 |
| GET | `/manifests` | 已加载插件 manifest（含 `frontend` 提示，与 UI 协议衔接） |
| POST | `/uninstall` | 按 `pluginId` / 可选 `version` 从磁盘删除目录后重载 |
| POST | `/activateVersion` | 磁盘已有版本时切换库表激活版本并重载（需 `PluginHostDbBridge`） |
| POST | `/install` | `multipart` 字段 **`file`**，单 **`.jar`**；校验通过后落盘、可选入库、重载 |

与业务协作、权限细节以当前 Controller 为准；**插件内 @Controller 动态注册属后续阶段**（见分阶段文档）。

### 磁盘布局探测（市场列表标记）

- **`PluginJarLocationStatus`**（`mms-plugin-host`）：`OK`、`ROOT_NOT_DIRECTORY`、`VERSION_DIR_MISSING`、`LIB_DIR_MISSING`、`JAR_NOT_FOUND`。
- **`PluginLifecycleManager`**：`isPluginsRootDirectory()`；`probeVersionLayout(pluginId, version)`（与加载时 `lib` 扫描一致）。
- **`PluginMarketCardVo.diskLayoutWarning`**：`SysPluginMarketServiceImpl` 合并卡片时写入枚举名；根不可用则**所有**卡片标记 `ROOT_NOT_DIRECTORY`；有库表激活版本则探测该版本目录；磁盘有插件目录但所有槽位无 jar 则 `JAR_NOT_FOUND`。列表枚举磁盘槽位时用**全部** `DiskPluginSlot`（含无 jar 目录），避免漏卡片。

## 插件市场（管理端 UI + 市场 API）

| 位置 | 说明 |
|------|------|
| `mms-ui/src/views/system/pluginMarket/index.vue` | 插件市场页：卡片仅 **「详情」** 打开弹窗；**卸载**=只删**磁盘**安装目录（`pluginHost/uninstall`），**不动库表**，可再 **安装**；**删除**=只清 **库表** 登记（`removeCatalog`），**不删磁盘**。二者不同，勿把卸载当成「删除插件条目」。 |
| `mms-ui/src/views/system/pluginMarket/api.ts` | `hostBase`=`/system/pluginHost`，`marketBase`=`/system/pluginMarket` |

**`PluginMarketController`**（`mms-system`，`super_admin`）：

- `GET /system/pluginMarket/cards`：合并库表与宿主状态，含 **`diskLayoutWarning`**。
- `POST /system/pluginMarket/removeCatalog`，body `{"pluginId":"..."}`：删 **`sys_plugin_version`** 该插件行 + **`sys_plugins`** 上架行（`PLUGIN_REGISTRY_TENANT`），**事务提交后** `pluginLifecycleManager.reload()`；**不删磁盘**。用户表述的「删除插件」若指去掉市场/登记记录，对应此接口；若指去掉磁盘文件，用 **`pluginHost/uninstall`**，且卸载后可重新安装。

**卡片按钮约定**（`runtimeState`）：

- **`LOADED`（运行中）**：不展示 **安装**、**删除**（升级用页顶上传或先卸载）；展示 **详情**、**卸载**。
- **`NOT_INSTALLED` / `ON_DISK`**：展示 **安装**（上传 JAR，确认 `plugin.json` id）、**删除**（按需）、**卸载**（非未安装时）。

页顶 **上传插件包** 始终可用（不依赖卡片）。

## 库表与市场

- 安装成功后可经 **`PluginHostDbBridge`**（在 `mms-system` 侧实现）写入 **`sys_plugins`** 等表并维护**激活版本**；未部署库表时部分能力返回明确错误（如 `activateVersion`）。
- 增量 SQL 通常在主仓 **`script/db/`**（如 `increment_*sys_plugin*.sql`、`plugin_market_menu`）；改表或菜单后按团队流程执行并更新 **`version/`** 需求文档（见项目 `.cursor/rules`）。

## 启动加载与多租户（`ApplicationReadyEvent`）

- 插件在 **`PluginHostRunner`**（`ApplicationReadyEvent`）中调用 **`PluginLifecycleManager.loadAll()`**，进而通过 **`PluginHostDbBridge`** 访问 **`SysPluginVersionMapper`**。
- MyBatis **多租户拦截器**会调用 **`LoginObject.getLoginTenant()` → `getLoginId()` → `StpUtil.getSession()`**。此时无 HTTP 上下文，Sa-Token 可能抛 **`SaTokenContextException`**。
- **`LoginObject.getLoginId()`**（`mms-authority`）需与 **`NotWebContextException` 一并捕获 `SaTokenContextException`**，返回 `null`，使 `getLoginTenant()` 回退 **`"000000"`**，与 **`PluginHostDbBridgeImpl.PLUGIN_REGISTRY_TENANT`** 及租户插件默认租户一致。

## 引用 mms-modules 的开发边界

- **类加载**：插件 JAR 由宿主 **`URLClassLoader`** 加载，**父加载器一般为应用主 ClassLoader**，因此主程序已加载的类（如 **`mms-common`** 中已在宿主 classpath 的类）可被插件 **以同一类型** 使用 —— 前提是 **插件不要重复打包同名冲突版本**。
- **Maven**：对 **`mms-plugin-api`**（及仅需编译期类型的 **`mms-common`** 等）常用 **`provided`**，避免Fat JAR 与宿主版本漂移；具体以示例 `mms-plugins/mms-plugin-sample-health/pom.xml` 为准。
- **不建议**：插件直接依赖并调用 **`mms-system` 的 Spring Bean / Service / Mapper**（无 Spring 注入、生命周期与事务边界不清）。需要系统能力时优先 **HTTP 调用管理端已有接口**，或后续由官方扩展 **受控宿主 API**。
- **可复用**：纯工具类、DTO、常量等 **无状态且 ABI 稳定** 的 API；注意 **双向兼容性**（宿主升级后插件仍应能通过 `requiresMms` 校验）。

## 与健康检查

实现 **`PluginHealthContributor`**（与 `MmsPlugin` 可同时由同一 JAR 提供）可向 `/system/pluginHost/health` 汇总输出行；示例见 **`mms-plugin-sample-health`**。

## 文档（mms-doc）

优先保持与代码一致；站点路径（VitePress）：

- [插件体系介绍](/mms-admin/plugin-overview)
- [插件开发指南](/mms-admin/plugin-developer-guide)
- [JAR 插件分阶段](/mms-admin/plugin-jar-phases)
- 前端路由协议：[插件与动态路由协议](/mms-ui/plugin-route-protocol)

同步菜单与 **`docs/log/index.md`** 见 **`mms-doc-sync`**。

## 排查清单

```
- [ ] mms.plugin.enabled 与 root-dir 是否符合预期；status 中 pluginsRootReady、resolvedPluginsRoot
- [ ] 根 pom revision 与 plugin.json requiresMms、host-mms-revision 是否一致
- [ ] 目录是否为 <root>/<pluginId>/<version>/lib/*.jar，且 SPI/META-INF 齐全
- [ ] 上传/install 失败时先看校验错误（PluginDescriptorValidator）与日志
- [ ] 激活版本 / 市场展示是否已执行对应增量 SQL 与菜单
- [ ] 启动报错 SaTokenContext：查 LoginObject.getLoginId 是否捕获 SaTokenContextException
- [ ] 市场卡片 diskLayoutWarning 与「卸载 / removeCatalog」语义是否与客户预期一致
```

## 与 mms-kills 的关系

通用 **CRUD、权限、分页、mms-ui** 仍以 **`.cursor/skills/mms-kills/SKILL.md`** 为准；**仅插件隔离、SPI、宿主行为**以本技能为准。
