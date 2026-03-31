---
name: mms-dev-standards
description: MMS 架构与仓库布局、mms-gen 生成流程与排查、mms-ui 组件与字典 SQL、手写 CRUD/SQL、管理端与 mms-servers 多端 API；在扩表、权限菜单、生成代码或开放接口时使用。
---

# MMS 开发规范（架构 · CRUD · 代码生成 · 多端 API）

## 快速开始

当用户新增功能、修复、重构、接口调整或配置变更时：

0. 若用户要求 **更新 / 同步 mms-doc 在线文档**，优先阅读并遵循 **`.cursor/skills/mms-doc-sync/SKILL.md`**（同级 `mms-doc` 仓库、`docs/` 正文、`docs/.vitepress/config.mts` 菜单与 `docs/log/index.md` 修订记录）。  
0b. **JAR 插件、plugin.json、插件市场 / 宿主 API、SPI 与 ClassLoader**：见 **`.cursor/skills/mms-plugin/SKILL.md`**。
0a. **模块地图 / 多租户 / 分阶段脚手架**：见 **`.cursor/skills/mms-modules-map/SKILL.md`**、**`mms-tenant-saas/SKILL.md`**、**`mms-scaffold-phases/SKILL.md`**；**依赖 DAG、冒烟、压测基线** 见主仓 **`version/v1-20260331-脚手架回归与扩展基线.md`**（与 `mms-doc` 中 `index/scaffold-capability-matrix`、`mms-admin/modules-map`、`plugin-jar-phases`、`mms-ui/plugin-route-protocol` 等交叉维护）。
1. 先确定流量入口：**管理端（mms-api-admin + mms-ui）** 还是 **开放端（mms-servers*）**  
2. 按本规范统一 **响应结构 / 分页 / 错误码 / 权限 / 日志 / 配置**  
3. 对外回调、SSE、下载等特殊接口保持原样  
4. 若涉及业务流程或页面逻辑变更，**先更新本项目的版本需求文档（`version/` 目录）**，再开始编码（以项目 `.cursor/rules` 为准）  
5. 在与用户的对话中，**所有 AI 分析与回答一律使用简体中文输出**  

## 仓库与架构布局（当前 monorepo）

| 路径 | 职责 |
|------|------|
| `mms-api-admin` | 后台聚合启动（依赖 `mms-gen`、`mms-system` 等），管理端 HTTP 入口 |
| `mms-modules/*` | 业务与基础能力：`mms-system`（系统/用户/字典）、`mms-framework`、`mms-datasource`、`mms-common`、`mms-gen`（模板与生成逻辑）、`mms-oss`、`mms-log` 等 |
| `mms-ui` | Vue 管理后台：`src/views/<module>/<feature>/`、`utils/request.ts` |
| `mms-servers` | 对 C 端/App/小程序等暴露的 API 应用（按子模块划分） |
| `script/db` | 基准库脚本（如 `mms.sql`）、可参考的 DDL/DML |
| `mms-modules/mms-gen/src/main/resources/template/gen` | Freemarker 模板：`config.json` 登记输出文件 |

新增业务优先落在 **`mms-system` 同包风格** 的业务模块目录，或与现有模块一致；具体包名以表生成配置中的 `packageName` / `moduleName` 为准。

## 模块与依赖 DAG（核心 vs 可选）

- **主干（管理端最小闭环）**：`mms-common` → `mms-redis` / `mms-authority` → `mms-datasource` → `mms-framework` → `mms-log` → **`mms-gen` + `mms-system`**（`mms-api-admin` 聚合）。
- **插件链**：`mms-plugin-api`（契约）← `mms-plugin-host`（宿主）；**`mms-plugin-sample-health`** 为示例 JAR，独立 `package`。细节与运维路径见 **`mms-plugin`** skill。
- **可选集成**：`mms-oss`、`mms-sms`、`mms-email`、`mms-wx`、`mms-mq`、`mms-websocket`、`mms-aliyun`、`mms-ai`、`mms-thymeleaf`、`mms-demo` 等，按需引入。
- **完整 Mermaid、ClassLoader vs 进程级评审、冒烟表、压测接口**：见 **`version/v1-20260331-脚手架回归与扩展基线.md`**；在线 **脚手架能力矩阵**：[脚手架能力矩阵](https://mmsadmin.cn/index/scaffold-capability-matrix.html)。

## mms-gen 代码生成：业务流程与排查要点

**推荐执行顺序（新表上线）：**

1. 库中建表 → 代码生成器 **导入表** → 配置字段（表单/列表/查询、`formDict`、**`formLayout`**：1 列表 2 树 3 单表单页）  
2. **生成/预览** → 将 Java / Vue / Mapper / `menu/*.sql` / `menu/*_dict.sql` 合入工程  
3. 在目标库执行：**字典 SQL**（若有）→ **菜单 SQL** → 刷新权限/路由（按现有运维习惯）  
4. 管理端登录后验证：**列表分页 / 树展开 / 单页保存**、按钮权限 `module:resource:action`

**`formLayout` 与接口对应关系（避免前后端路径不一致）：**

| 布局 | 列表数据 | 单条查询 | 写操作 |
|------|-----------|-----------|--------|
| 1 列表 | `POST .../list`（`TableDataInfo`） | `GET .../{id}` | POST/PUT/DELETE 标准 CRUD |
| 2 树 | `POST .../list`（`R<List<Vo>>`） | `GET .../{id}` | 同左 + 弹窗维护 |
| 3 单表单 | 无 list | `GET .../singleton` | `PUT .../singleton`（无 `GET /{id}`，避免与 `singleton` 路径冲突） |

**生成侧已处理的问题（排查时可知）：** `formLayout==3` 前端不再调用不存在的 `list`；树表 `selectVoById` 空指针；`deleteById` 多 ID 使用 `Arrays.asList`；编辑成功后 **列表/树** 会 `getTableData()` 刷新；字典 SQL 仅对非内置 `formDict` 生成，历史库中 `isHttps` 等小写编码已加入跳过名单。

**仍在接入时注意：** 单表单语义为多行时仅持久化 **主键升序第一条**；字典脚本重复执行可能主键冲突，执行前核对 `sys_dict.field_name`。

## 手写后端 CRUD（不经过生成器时的最小闭环）

以下与管理端生成代码结构对齐，便于复制扩展：

1. **表与实体**  
   - `entity/<Entity>.java`：`@TableName`，主键 `@TableId`  
   - `entity/bo/<Xxx>Bo.java`、`entity/vo/<Xxx>Vo.java`：查询/表单与展示；需分页时 Bo 可带 `PageQuery`  
   - 复杂导出加 `entity/export/<Xxx>Export.java`  

2. **Mapper**  
   - `mapper/<Xxx>Mapper.java` 继承 `BaseMapperPlus<Entity, Vo>`  
   - 若有复杂 SQL：`src/main/resources/mapper/<module>/<Xxx>Mapper.xml`  

3. **Service**  
   - 接口继承 `BaseService<Entity, Vo, Bo>`，实现继承 `BaseServiceImpl<...>`，`getBaseMapper()` 返回当前 Mapper  
   - 分页列表常用 `selectListVoPage(bo, bo.getPageQuery())`（与生成器一致）  

4. **Controller**  
   - `@RequestMapping("{moduleName}/{functionName}")`，继承 `BaseController`  
   - `@SaCheckPermission("module:resource:action")` 与菜单 SQL 中 `permission` 一致  
   - 写操作加 `@MmsLog`、`@Validated(ValidatedGroupConfig.insert|update|query.class)`（按方法）  

5. **权限标识习惯**  
   - `模块:资源:list | query | insert | edit | delete | import | export | print`（与 `sys_function.permission` 一致）

## SQL 与脚本约定

- **建表 / 增量**：团队统一目录（如 `script/db` 或模块内 `sql/`），变更可回溯。  
- **菜单**：生成物 `menu/${tableName}_menu.sql`，插入 `sys_function`；单表单页布局下权限与模板已裁剪（仅 list/query/edit 等必要项，以当前 `menu.sql.ftl` 为准）。  
- **字典**：生成物 `menu/${tableName}_dict.sql`；内置字典勿重复插入；列注释建议写 `1:上架 2:下架` 等便于解析。  
- **安全**：勿将含 `DROP`/`TRUNCATE` 的脚本交给生成器的「执行 SQL」能力在生产直接使用（生成器侧已有基本拦截）。

## 管理端 API 与 mms-ui 联调

- **Base URL**：以 `mms-ui` 环境变量 `VITE_APP_BASE_API` 为准，请求封装见 `mms-ui/src/utils/request.ts`。  
- **分页**：列表接口需兼容响应中的 `rows`、`total`（及必要时 `data` 内嵌）。  
- **非分页**：业务数据在 `data`，错误用 `code`/`msg`。  
- **前端结构**：`views/<module>/<function>/index.vue`、`index.ts`（API）、`type.ts`（Bo/Vo 类型）；加密头与现有模块保持一致。

## 开放端 / 多端 API（mms-servers）

- 与 **管理端 Session/Sa-Token + 菜单权限** 不同，开放接口通常走 **会员 Token / AppId** 等既有约定。  
- 路径多为 **`xxx-api/v1/**` 或业务前缀**（以具体 `Controller` 为准）；新增接口需单独做 **鉴权、防刷、参数脱敏**。  
- 可复用 **`mms-modules` 下 Service / Mapper**；避免在开放 Controller 中直接拼 SQL。  
- 技能前文 **「mms-servers-api 功能清单」** 用于快速定位已有能力；**新增模块** 按同目录风格增加 Controller + Service，并补充网关/文档若项目有要求。

## 自定义注解（`mms-common` / `mms-framework` / `mms-log` / `mms-gen`）

| 注解 | 作用 | 生效方式 | 说明 |
|------|------|----------|------|
| `@Dict` | 字典「码 → 文案」 | Jackson：`DictSerializer` 经 `DictSensitiveAnnotationIntrospector` 注册；Excel：`DictExcelConverter` | 依赖 Redis 键 `SYS_DICT:{dictCode}:{value}`；未命中时原样输出码值 |
| `@SensitivityEncrypt` | 脱敏（手机、身份证等） | `@JsonSerialize` + `SensitivitySerializer` | **仅 JSON 序列化**生效；**已修复**：`null` 输出 null，非脱敏场景不再错误写成 `""` |
| `@MssSafety` | 请求解密、响应加密、防重复提交 | `RequestBodyHandlerAdvice` + 响应Advice（与同模块配置一致） | 常见于 `mms-servers` 管理端 Controller |
| `@IgnoreSign` | 加签时忽略字段 | `SignUtil` | 用于签名字段排除 |
| `@RateLimit` | 单机 QPS 限流 | `RateLimitAspect`（Guava `RateLimiter`） | 按「类+方法」维度；**非分布式** |
| `@PrintColumn` | 打印/导出列标题与类型 | `PrintObject` 组装打印数据时反射读取 | 与 `@PrintColumn` 配套导出实体 |
| `@MmsLog` | 操作日志 | 日志模块 AOP | 管理端写操作常用 |
| `@EncryptParameter` | 参数字段加解密 | `mms-gen` 的 `EncryptParameterAspect` | **仅代码生成模块**内数据源配置等，非运行时全局面 |
| `@DataPermissionGroup` / `@DataColumn` | 部门 / 本人 / 自定义 SQL 片段裁剪 | `mms-datasource`：`DataPermissionInnerInterceptor` 仅处理 **SELECT** 且 **单层 PlainSelect** | 超管、未登录不拼接；`mms.data-permission.enabled`；`LoginObject.getLoginDeptIdSoft()` 读 Redis 用户缓存 `deptId` |
| `@I18nKey` | MessageSource 翻译字段值 | `I18nKeyAnnotationIntrospector` + `I18nMessageSerializer`；与 `DictSerializer` 链式 `pair` | 键 = `{前缀}.{运行时字段值字符串}`；示例键见 `i18n/messages.properties` 的 `demo.status.*` |
| （工具）`SensitiveOps` | 脱敏 | 静态方法，无注解 | Excel、日志、手拼文案时调用；JSON 仍用 `@SensitivityEncrypt` |

**`@AuthLoginAnnotation`**：已 **`@Deprecated`**，无拦截器实现；请使用 **Sa-Token**（`@SaCheckPermission`、`StpUtil.checkLogin()` 等）。

**Jackson `AnnotationIntrospector`：** 顺序为 `pair(I18nKey, pair(Dict, JacksonAnnotationIntrospector))`，避免覆盖标准 Jackson 注解。

## 核心模块其它风险点（排查备忘，非本次必改）

- **多租户**：`TenantLineInnerInterceptor` 的 `getTenantId()` 依赖 **`LoginObject.getLoginTenant()`**；无登录用户时依赖 **`getLoginId()`** 返回 `null` 后回退 **`"000000"`**。若在**应用就绪、定时任务等无 Sa-Token 上下文**的线程中访问 `StpUtil`，可能抛 **`SaTokenContextException`**（需在进入 `getLoginTenant()` 前由 **`getLoginId()`** 吞掉，见 **`mms-authority` `LoginObject`**、**`.cursor/skills/mms-plugin/SKILL.md`**「启动加载与多租户」）。其它业务若在异步线程 / 就绪回调中跑 Mapper，需同理或 `@InterceptorIgnore` / 显式租户。  
- **限流**：`@RateLimit` 仅进程内；集群需网关或 Redis 限流。

## 功能开发速查清单（AI 与人类共用）

```
- [ ] 表结构 + 索引 + 租户/软删除字段是否与 BaseEntity 一致
- [ ] Entity / Bo / Vo / Mapper / Service / Controller 是否齐全
- [ ] 权限串是否与菜单 SQL、前端 v-auth 一致
- [ ] 分页与 R 包装是否与 mms-ui request 拦截器兼容
- [ ] 写操作是否有 @MmsLog、@Transactional(rollbackFor = Exception.class)
- [ ] 是否需字典：sys_dict / sys_dict_data 或生成 *_dict.sql
- [ ] 开放端是否需单独 Controller 与鉴权，不可直接暴露管理接口
```

## AI 分析与回答约定

- 助手在分析需求、设计数据结构、接口、表结构或任何业务逻辑时，应先用**简体中文**说明自己的理解和推理过程，再给出结论或代码建议。  
- 若存在多种可选方案，需用简体中文对比优劣，并明确推荐理由。  
- 如因外部文档/代码为英文而引用示例，允许在代码或字段名中保留英文，但解释和讨论仍需使用简体中文。  

## mms-servers-api 功能清单（对 nuxt-ai 暴露的后端能力）

> 模块位置：主仓 `mms-api-unix`（原 `mms-mobile-api` 已移除，移动端/开放接口统一由此启动），应用入口 `MmsMobileApiApplication`。  
> 以下按 Controller 维度梳理主要对外功能，便于在分析页面需求或接口时快速定位。

- **基础能力 `/api/base/v1`（Jar：`mms-base` → `ApiBaseOpenV1Controller`，业务在 `BaseApiSupport`）**  
  - 网站配置：`GET /website`，按 key：`GET /configs/{key}`（前缀 `website_`）。  
  - 短信验证码：`POST /sms-code`（类型 1–6 同原约定）。  
  - 文件上传：`POST /uploads`；邮件验证码：`POST /email-codes`；省市区：`GET /store-tool-areas`（Deprecated）。

- **登录注册（`ApiLoginController`，前缀 `/api/member/v1`）**  
  - `GET /token-login`、`GET /code-open-id-login`、`GET /code-phone-register-or-login`、`POST /login`（短信）、`POST /account-login`、`POST /find-password`、`POST /oauth-authorize`、`POST /oauth-polling`、`POST /logout`。  
  - 登录成功统一写 Token、最近登录信息、Redis 缓存。

- **会员中心（`ApiMemberController`，前缀 `/api/member/v1`）**  
  - `GET /info`；`POST /update-member`；实名与资料：`/authentication`、`/authentication-with-bound-phone`、`/bind-email`、`/signature`、`/tags`、`/member-bg-img`。  
  - 地址：`POST /addresses/list`、`GET /addresses/{id}`、`POST /addresses/edit`、`GET /addresses/default`、`POST /addresses/insert`、`GET /addresses/delete/{id}`。  
  - 提现账户：`POST /wallet-accounts/bind`、`DELETE /wallet-accounts/unbind/{id}`、`PUT /wallet-accounts/default/{id}`、`GET /wallet-accounts`。

- **话题与社区 `/api/bbs/v1`（`ApiBbsController`）**  
  - 分类 `GET /categories`；话题 `GET|POST /topics`（分页查询 / 发布）、`GET /topics/detail`、`GET /topics/delete`。  
  - 评论：`GET /comments`、`GET /comments/delete`、`GET /comments/list`；互动：`/interactions/like|collect|attention`；个人：`/me/stats`、`/me/topics`、`/me/received-likes`、`/me/following`、`/me/at-me`。

- **网站 CMS `/api/cms/v1`（`ApiCmsController`）**  
  - `GET /navigation`、`GET /quick-entries`、`GET /search-hot`。

- **广告 `/api/ad/v1`（`ApiStoreAdvertisingController`）**  
  - `GET /locations`、`GET /by-code`。

- **文章 `/api/article/v1`（`ApiStoreArticleController`）**  
  - `POST /categories`；`GET /articles`、`GET /articles/by-id`、`GET /articles/detail`；`POST /articles/publish|edit`、`DELETE /articles`；`GET /articles/stats`；`POST /articles/mine`。  

- **游戏模块 `game/v1`（`ApiGameController`）**  
  - 游戏列表：`/getGameList`，支持按关键字、状态、是否热门筛选并分页返回游戏列表。  
  - 游戏首页数据：`/getGameIndexData`，返回热门游戏 + 按字母索引分组的全部游戏，用于前端首页展示。  
  - 其它接口：围绕 `GameAccount`、`GameAttribute`、`GameAttributeType` 等实体，提供游戏账号配置、属性配置相关的查询与管理接口。  

- **App 版本 `/api/app/v1`（`ApiAppVersionController`）**  
  - `GET /upgrade-check`：平台类型（1 iOS / 2 Android）查询最新版本列表（版本号、更新说明、下载地址、是否强更）。  

## 版本需求文档规范（nuxt-ai 前端）

- 目录：项目根下 `version/`，用于存放本项目的「版本需求文档」。  
- 命名规范：`vX-YYYYMMDD-说明.md`，例如：`v1-20260209-登录与数据建模.md`。  
- 使用流程：
  - 用户在对话中描述需求或改动 → 助手先进行**需求分析与澄清**，直到逻辑清晰。
  - 基于共识，由助手在 `version/` 目录生成或更新对应版本的需求文档。
  - 后端接口 / 数据表设计 / 前端改造，均以最新版本需求文档为准。  
  - 需求有变更时，先更新 `version/` 中的文档，再修改代码。  

## API 返回结构

- **统一返回 `R<T>`**
- **分页统一返回 `R<PageResult<T>>`**
- `R` 已自动填充 `rows/total` 兼容字段（mm-ui 旧逻辑）

示例：
```java
@PostMapping("/list")
public R<PageResult<SysRoleVo>> listPage(@RequestBody SysRoleBo bo) {
    return R.success(baseService.selectPageUserList(bo, bo.getPageQuery()).toPageResult());
}
```

参考：
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/utils/R.java`
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/code/entity/PageResult.java`
- `mms-modules/mms-datasource/src/main/java/com/sxpcwlkj/datasource/entity/page/TableDataInfo.java`

## 分页规范

- Bo 继承 `PageQuery`
- Service 返回 `TableDataInfo`，Controller 转 `R<PageResult>`
- `PageQuery.build()` 生成分页对象

参考：
- `mms-modules/mms-datasource/src/main/java/com/sxpcwlkj/datasource/entity/page/PageQuery.java`

## 错误码与异常

- HTTP 与业务错误码使用 `HttpStatusEnum` / `ErrorCodeEnum`
- 业务异常用 `MmsException`

参考：
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/enums/HttpStatusEnum.java`
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/enums/ErrorCodeEnum.java`
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/exception/MmsException.java`

## 权限 / 日志 / 校验

- 权限：`@SaCheckPermission("module:resource:action")`
- 公共接口：`@SaIgnore`
- 操作日志：`@MmsLog(...)`
- 参数校验：`@Validated(ValidatedGroupConfig.xxx.class)`
 - 写操作建议 `@Transactional(rollbackFor = Exception.class)`

参考：
- `mms-modules/mms-log/src/main/java/com/sxpcwlkj/log/annotation/MmsLog.java`
- `mms-modules/mms-framework/src/main/java/com/sxpcwlkj/framework/config/ValidatedGroupConfig.java`

## 配置分层

- 公共：`application.yml`
- 环境：`application-dev.yml / application-local.yml / application-prod.yml`
- 敏感信息全部外置环境变量
 - 配置新增优先放环境文件，避免污染公共配置

## OSS 动态配置（数据库驱动）

- OSS 配置来自数据库表 `sys_oss_config`
- 启动时自动从 DB 加载并初始化存储
- 变更配置后触发 `initOss()` 更新

参考：
- `mms-modules/mms-system/src/main/java/com/sxpcwlkj/system/service/impl/SysOssConfigServiceImpl.java`
- `mms-modules/mms-oss/src/main/java/com/sxpcwlkj/oss/service/impl/MyFileStorageServiceImpl.java`

## mm-ui 兼容要求

- 分页接口返回 `R<PageResult>`，必须兼容 `rows/total`
- 非分页接口必须有 `code/msg`（通过 `R` 返回）
- `request.ts` 目前支持 `rows/total` 与 `data.rows/data.total`

参考：
- `mms-ui/src/utils/request.ts`

## 特殊接口例外

以下保持原样，不强制 `R`：
- SSE 流式接口（`SseEmitter`）
- Webhook/回调（微信/支付）
- 文件下载流
- 根路径简单字符串输出

## 分层与命名规范

- 模块结构：`controller / service / service.impl / mapper / entity / entity.bo / entity.vo / entity.export`
- Controller 继承 `BaseController`
- Service 接口继承 `BaseService<T,V,B>`，实现继承 `BaseServiceImpl<T,V,B>`
- Mapper 继承 `BaseMapperPlus<T,V>`
- 方法命名：`listPage / list / queryById / insert / edit / delete`

参考：
- `mms-modules/mms-common/src/main/java/com/sxpcwlkj/common/code/controller/BaseController.java`
- `mms-modules/mms-framework/src/main/java/com/sxpcwlkj/framework/service/BaseService.java`
- `mms-modules/mms-framework/src/main/java/com/sxpcwlkj/framework/service/impl/BaseServiceImpl.java`
- `mms-modules/mms-datasource/src/main/java/com/sxpcwlkj/datasource/mapper/BaseMapperPlus.java`

## 编码与日志规范

- 业务异常只抛 `MmsException`
- 日志必须包含业务主键/关键上下文
- 禁止在 Controller 直接操作数据库
- 参数对象优先使用 `Bo`，返回使用 `Vo`

## mms-ui 代码生成：表单项与本地组件映射

生成/维护 `mms-gen` 的 `template/gen/vue/*.ftl` 时，**优先使用 `mms-ui/src/components` 下已有封装**，与手工页面（如 `views/system/config/*.vue`）保持一致，避免用纯 `el-input` 顶替上传、字典、富文本等能力。

| 生成器 `formType` / 场景 | 推荐组件（标签） | 源码路径 |
|--------------------------|------------------|----------|
| `editor` | `fast-editor` | `fast-editor/src/fast-editor.vue` |
| `select` / `checkbox` + 字典 | `fast-select`（checkbox 时 `type="checkbox"`） | `fast-select/src/fast-select.vue` |
| `radio` + 字典（非 `status` 特例） | `fast-radio-group` | `fast-radio-group/src/fast-radio-group.vue` |
| `status` 等开关（与字典 `SYS_STATE` 搭配） | `fast-switch` | `fast-switch/src/fast-switch.vue` |
| `file` | `fast-file` | `fast-upload/file.vue` |
| `image` | `fast-img` | `fast-upload/img.vue` |
| `images` | `fast-imgs` | `fast-upload/imgs.vue` |
| `video`（若业务扩展） | `Video` / `fast-upload/video.vue` | 按需 |
| 列表字典列展示 | `fast-table-column` + `dict-type` | `fast-table-column/src/fast-table-column.vue` |
| 省市区（字段若约定为地区） | `City`（`city/index.vue`） | 需在生成器字段类型中单独约定 |
| 表格工具条 | `TableTool` | `table-tool/index.vue` |

说明：

- `unplugin-vue-components` 会为部分组件生成全局类型（见 `mms-ui/components.d.ts`），生成代码仍可显式 `import`，与现有 `config` 模块写法一致。
- 弹窗表单（`dialog.vue.ftl`）与单表单页（`index.vue.ftl` 的 `formLayout==3`）应对 **同一 `formType` 使用相同组件**，避免一种布局可上传、另一种只剩输入框。

### 字典（sys_dict / sys_dict_data）与代码生成

- 表 `sys_dict`：一行表示一个字典类型，`field_name` 为前端 `dict-type` / 生成字段上的 **formDict**（如 `ORDER_STATUS`）。
- 表 `sys_dict_data`：`field_name` 与上级相同，`value` / `label` 为选项键值与展示文案。
- **自动生成**：`mms-gen` 在生成代码时会额外输出 `${backendPath}/menu/${tableName}_dict.sql`（模板 `sql/dict.sql.ftl`）。  
  - 收集当前表字段中 **非内置** `formDict`（已排除 `SYS_STATE`、`SYS_IS`、`SYS_SEX`、`IS_HTTPS` / `isHttps` 等，见 `GenDictBootstrapUtil`）。  
  - 仅包含真实使用字典的字段：表单 `select`/`radio`/`checkbox`、列表带字典展示、查询为 `select`/`radio`/`checkbox` 且配置了 `formDict`。  
  - 字典项优先从 **该字典第一个代表字段的数据库列注释** 解析（支持 `1:上架`、`1上架` 等形式）；解析失败则生成「选项一 / 选项二」占位。  
- 执行生成的字典 SQL 前请确认 `field_name` 未与现网冲突；内置字典勿重复插入。

## 前端目录与命名规范

- 手写页面常与 `mms-ui/src/views/system/<业务目录>/` 对齐；**代码生成**路径为 `mms-ui/src/views/${moduleName}/${functionName}/`（由生成表配置决定）。  
- 统一 `index.vue` + `index.ts`（API）+ `type.ts`（Bo/Vo）。  
- 列表页默认 `state.tableData.data / state.tableData.total`；树列表使用 `res.data`；单表单页走 `getSingleton` / `saveSingleton`。

## 接口命名与路径规范

- 列表：`/list`（POST）
- 详情：`/{id}`（GET）
- 新增：`/`（POST）
- 修改：`/`（PUT）
- 删除：`/{ids}`（DELETE）
- 分页接口优先 `listPage` 方法命名

## SQL / 索引 / Mapper 规范

- Mapper 继承 `BaseMapperPlus`
- 分页必须使用 `PageQuery.build()`
- 条件构造使用 `LambdaQueryWrapper`
- 新增字段需同步 SQL 脚本与实体

## 安全规范

- 禁止硬编码密钥/密码
- Swagger/Actuator 在生产环境默认关闭或受限
- CORS 统一由全局配置控制

参考：
- `mms-api-admin/src/main/resources/application-*.yml`

## 测试与回归

- 新增接口需覆盖：成功路径 + 失败路径
- 与 mm-ui 联调后至少验证分页与错误提示
- 变更返回结构必须确认 `request.ts` 兼容

## 前端联调要点

- 列表接口返回 `rows/total`（分页）
- 详情接口返回 `data`
- 失败必须返回 `code/msg`

参考：
- `mms-ui/src/utils/request.ts`

## 输出模板

### 变更检查清单

```
- [ ] Controller 返回类型是否为 R<T>
- [ ] 分页是否为 R<PageResult> 且 rows/total 兼容
- [ ] 权限/日志/校验注解是否齐全
- [ ] 配置是否分环境且敏感信息外置
- [ ] mm-ui 是否可直接使用 rows/total
- [ ] 新增接口是否联调并验证报错提示
```

### 简短变更报告

```
## 变更内容
- ...

## 影响范围
- ...

## 兼容性
- mm-ui: 兼容 / 需调整
```

### 提交信息规范

```
feat(module): <简短描述>
fix(module): <简短描述>
refactor(module): <简短描述>
```

### 接口契约（分页）

```
{
  "code": 200,
  "msg": "操作成功",
  "data": { "rows": [...], "total": 123 },
  "rows": [...],
  "total": 123
}
```
