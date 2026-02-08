---
name: mms-dev-standards
description: Enforce this project’s backend/frontend development standards. Use when adding or changing APIs, pagination, response models, permissions/logging, configs, OSS storage, or when aligning code with mm-ui expectations.
---

# MMS 开发规范

## 快速开始

当用户新增功能、修复、重构、接口调整或配置变更时：

1. 先确定是否为 **mm-ui 对接接口**  
2. 按本规范统一 **响应结构 / 分页 / 错误码 / 权限 / 日志 / 配置**  
3. 对外回调、SSE、下载等特殊接口保持原样  

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

## 前端目录与命名规范

- 视图目录：`mms-ui/src/views/system/<module>/`
- 统一 `index.vue` + `index.ts` + `type.ts`
- API 入口：`/views/system/<module>/index.ts`
- 类型定义：`/views/system/<module>/type.ts`
- 列表页默认 `state.tableData.data / state.tableData.total`

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
- `mms-admin/src/main/resources/application-*.yml`

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
