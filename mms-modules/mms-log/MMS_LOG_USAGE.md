# MMS 日志注解使用文档

## 快速开始

### 1. 基本使用

```java
// 最简写法 - 自动识别所有信息
@MmsLog
@PostMapping("/add")
public R<Boolean> addUser(@RequestBody SysUserBo bo) {
    return success(userService.save(bo));
}
```

**自动识别结果:**
- module = "用户管理" (从类名 SysUserController 识别)
- operType = INSERT (从 @PostMapping 识别)
- description = "新增用户" (从方法名识别)

---

## 使用方式

### 方式 1: 注解方式 - 完全自动 (推荐)

```java
@MmsLog
@PostMapping
public R add() { }  // 新增

@MmsLog
@PutMapping
public R edit() { }  // 修改

@MmsLog
@DeleteMapping("/{id}")
public R delete(@PathVariable Long id) { }  // 删除

@MmsLog
@GetMapping("/list")
public R list() { }  // 查询
```

### 方式 2: 注解方式 - 部分自定义

```java
// 自定义模块名
@MmsLog(module = "会员管理")
@PostMapping
public R add() { }

// 自定义描述
@MmsLog(description = "批量删除用户")
@DeleteMapping("/batch")
public R batchDelete() { }

// 自定义操作类型
@MmsLog(operType = OperationType.EXPORT)
@GetMapping("/export")
public void export() { }
```

### 方式 3: 注解方式 - 完全自定义

```java
@MmsLog(
    module = "用户管理",
    operType = OperationType.DELETE,
    description = "删除用户",
    saveBeforeData = true,  // 记录操作前数据
    saveResponseData = true  // 记录响应数据
)
@DeleteMapping("/{id}")
public R delete(@PathVariable Long id) { }
```

### 方式 4: 静态方法 - 一个参数 (推荐) ⭐

**无需注入，自动识别模块，最简洁！**

```java
// 在 UserController 中 - 自动识别为 "用户管理"
@PostMapping("/add")
public R add() {
    userService.save();
    MmsLog.insert("新增用户");  // ✅ 只写描述
    return R.ok();
}

// 在 RoleController 中 - 自动识别为 "角色管理"
@DeleteMapping("/{id}")
public R delete(@PathVariable Long id) {
    roleService.delete(id);
    MmsLog.delete("删除角色");  // ✅ 自动识别
    return R.ok();
}

// 所有操作
MmsLog.insert("新增用户");    // 新增
MmsLog.update("修改用户");    // 修改
MmsLog.delete("删除用户");    // 删除
MmsLog.select("查询用户");    // 查询
MmsLog.export("导出用户");    // 导出
MmsLog.imports("导入用户");   // 导入
MmsLog.clean("清空缓存");     // 清空
```

### 方式 5: 静态方法 - 两个参数 (手动指定模块)

```java
// 需要自定义模块名时
MmsLog.insert("会员管理", "新增VIP会员");
MmsLog.update("订单管理", "修改订单状态");
MmsLog.delete("商品管理", "删除商品");
```

### 方式 6: 静态方法 - 带结果或异常

```java
// 记录带结果
Map<String, Object> result = userService.process();
MmsLog.log("用户管理", OperationType.UPDATE, "处理数据", result);

// 记录错误
try {
    userService.importData();
} catch (Exception e) {
    MmsLog.logError("用户管理", OperationType.IMPORT, "导入失败", e);
}
```

---

## 注解参数

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|--------|------|
| `module` | String | "" | 模块名称,留空自动识别 |
| `operType` | OperationType | OTHER | 操作类型,自动识别 HTTP 方法 |
| `description` | String | "" | 操作描述,留空自动识别 |
| `saveRequestData` | boolean | true | 是否保存请求参数 |
| `saveResponseData` | boolean | false | 是否保存响应数据 |
| `saveBeforeData` | boolean | false | 是否记录操作前数据 |
| `savePolicy` | LogSavePolicy | DATABASE | 保存策略 |
| `excludeParams` | String[] | {"password"...} | 排除的敏感参数 |

---

## 操作类型

```java
public enum OperationType {
    OTHER(0),    // 其它
    INSERT(1),   // 新增
    UPDATE(2),   // 修改
    DELETE(3),   // 删除
    SELECT(4),   // 查询
    EXPORT(5),   // 导出
    IMPORT(6),   // 导入
    LOGIN(7),    // 登录
    LOGOUT(8),   // 退出
    GRANT(9),    // 授权
    CLEAN(10);   // 清空
}
```

---

## 保存策略

```java
@MmsLog(savePolicy = LogSavePolicy.DATABASE)  // 保存到数据库(默认)
@MmsLog(savePolicy = LogSavePolicy.FILE)      // 保存到文件
@MmsLog(savePolicy = LogSavePolicy.BOTH)      // 同时保存
```

---

## 自动识别规则

### 模块识别 (从类名)

| 类名包含 | 识别结果 |
|---------|---------|
| User | 用户管理 |
| Role | 角色管理 |
| Dept | 部门管理 |
| Menu | 菜单管理 |
| Dict | 字典管理 |

### 操作识别 (从注解)

| HTTP 注解 | 识别结果 |
|----------|---------|
| @PostMapping | INSERT (新增) |
| @PutMapping | UPDATE (修改) |
| @DeleteMapping | DELETE (删除) |
| @GetMapping + export | EXPORT (导出) |
| @GetMapping + import | IMPORT (导入) |

### 描述识别 (组合)

操作 + 对象,例如:
- `addUser` + `@PostMapping` → "新增用户"
- `editRole` + `@PutMapping` → "修改角色"
- `deleteDept` + `@DeleteMapping` → "删除部门"

---

## 记录的信息

系统自动记录以下信息:

**基础信息:**
- 操作时间、模块、类型、描述
- 请求方法、URL、执行方法

**用户信息:**
- 用户 ID、用户名、租户 ID、角色

**网络信息:**
- IP 地址、归属地、浏览器、操作系统

**执行信息:**
- 请求参数、响应结果、执行状态、耗时

---

## 使用示例

### 用户管理 - 注解方式

```java
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @MmsLog
    @PostMapping
    public R add(@RequestBody SysUserBo bo) { }

    @MmsLog
    @PutMapping
    public R edit(@RequestBody SysUserBo bo) { }

    @MmsLog
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable String ids) { }

    @MmsLog
    @PostMapping("/import")
    public R importData(@RequestParam("file") MultipartFile file) { }

    @MmsLog
    @GetMapping("/export")
    public void export() { }
}
```

### 用户管理 - 静态方法 (推荐)

```java
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    // 无需注入 OperLogService

    @PostMapping
    public R add(@RequestBody SysUserBo bo) {
        userService.save(bo);
        MmsLog.insert("新增用户");  // ✅ 自动识别为 "用户管理"
        return R.ok();
    }

    @PutMapping
    public R edit(@RequestBody SysUserBo bo) {
        userService.update(bo);
        MmsLog.update("修改用户");  // ✅ 超简洁
        return R.ok();
    }

    @DeleteMapping("/{ids}")
    public R delete(@PathVariable String ids) {
        userService.delete(ids);
        MmsLog.delete("删除用户");
        return R.ok();
    }

    @PostMapping("/import")
    public R importData(@RequestParam("file") MultipartFile file) {
        try {
            int count = userService.importExcel(file);
            MmsLog.imports("导入用户: " + count + " 条");
            return R.ok();
        } catch (Exception e) {
            MmsLog.logError("用户管理", OperationType.IMPORT, "导入失败", e);
            return R.fail(e.getMessage());
        }
    }
}
```

### Service 层使用

```java
@Service
public class UserServiceImpl {
    
    public void processUser(Long userId) {
        // 业务逻辑
        doProcess(userId);
        
        // 记录日志 - 自动识别为 "用户管理"
        MmsLog.update("处理用户数据: " + userId);
    }
    
    public void batchProcess(List<Long> ids) {
        int success = 0;
        for (Long id : ids) {
            try {
                process(id);
                success++;
            } catch (Exception e) {
                // 继续
            }
        }
        MmsLog.update(String.format("批量处理: 成功 %d/%d", success, ids.size()));
    }
}
```

### 定时任务中使用

```java
@Scheduled(cron = "0 0 2 * * ?")
public void cleanTask() {
    try {
        int count = dataService.cleanExpired();
        MmsLog.clean("清理过期数据: " + count + " 条");
    } catch (Exception e) {
        MmsLog.logError("系统管理", OperationType.CLEAN, "清理失败", e);
    }
}
```

### 重要操作 (记录操作前数据)

```java
@MmsLog(
    description = "重置用户密码",
    saveBeforeData = true
)
@PutMapping("/resetPwd")
public R resetPwd(@RequestBody ResetPwdBo bo) { }
```

### 敏感操作 (双重保存)

```java
@MmsLog(
    module = "系统管理",
    description = "清空系统缓存",
    savePolicy = LogSavePolicy.BOTH
)
@DeleteMapping("/clearCache")
public R clearCache() { }
```

---

## 数据库表结构

```sql
CREATE TABLE `sys_log` (
    `oper_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `module` VARCHAR(50) COMMENT '模块名称',
    `oper_type` INT COMMENT '操作类型',
    `description` VARCHAR(200) COMMENT '操作描述',
    `user_id` VARCHAR(50) COMMENT '操作人员ID',
    `user_name` VARCHAR(50) COMMENT '操作人员',
    `oper_ip` VARCHAR(128) COMMENT 'IP地址',
    `oper_location` VARCHAR(255) COMMENT '操作地点',
    `oper_time` DATETIME COMMENT '操作时间',
    `cost_time` BIGINT COMMENT '消耗时间(毫秒)',
    `status` INT COMMENT '状态(0成功 1失败)',
    PRIMARY KEY (`oper_id`)
) COMMENT='操作日志表';
```

---

## 自动识别规则 (静态方法)

### 模块识别 (从调用类名)

| 类名包含 | 识别为 |
|---------|--------|
| User | 用户管理 |
| Role | 角色管理 |
| Dept | 部门管理 |
| Menu | 菜单管理 |
| Dict | 字典管理 |
| Config | 参数管理 |
| Notice | 通知管理 |
| Post | 岗位管理 |
| Order | 订单管理 |
| Product | 商品管理 |
| Log | 日志管理 |
| 其他 | 系统管理 |

**示例:**
- 在 `SysUserController` 中调用 → 识别为 "用户管理"
- 在 `OrderService` 中调用 → 识别为 "订单管理"
- 在 `CommonUtil` 中调用 → 识别为 "系统管理"

---

## 注意事项

1. **敏感参数过滤**: 默认过滤 `password`、`oldPassword`、`newPassword`、`confirmPassword`
2. **数据长度限制**: 请求参数和响应数据最多保存 2000 字符
3. **异步执行**: 日志记录异步执行,不阻塞业务
4. **虚拟线程**: 基于 JDK 21 虚拟线程,支持高并发
5. **无需注入**: `MmsLog` 静态方法全局可用,无需注入

---

## 常见问题

**Q: `MmsLog.update()` 是新增还是更新日志?**  
A: **每次调用都是新增一条日志记录！**
- `MmsLog.insert("描述")` → 新增一条日志,operType=INSERT
- `MmsLog.update("描述")` → 新增一条日志,operType=UPDATE
- `MmsLog.delete("描述")` → 新增一条日志,operType=DELETE
- 方法名只表示**业务操作类型**,不是数据库操作类型
- 示例: 调用 3 次 `MmsLog.update()` 会在数据库新增 3 条日志记录

**Q: 注解方式和静态方法有什么区别?**  
A: 
- 注解方式: 自动记录完整的 HTTP 请求信息、参数、响应
- 静态方法: 手动调用,记录关键业务节点,更灵活
- 推荐: Controller 用注解,Service/定时任务用静态方法

**Q: 复杂流程应该记录几次日志?**  
A: 根据实际需求:
- 简单流程: 只记录最终结果 (1 次)
- 复杂流程: 记录关键业务节点 (3-5 次)
- 避免: 每个小步骤都记录,会产生大量日志

**Q: 如何不记录某个方法的日志?**  
A: 不添加 `@MmsLog` 注解,也不调用 `MmsLog.xxx()` 方法

**Q: 静态方法如何自定义模块名?**  
A: 使用两个参数: `MmsLog.insert("自定义模块", "描述")`

**Q: 如何只记录失败的操作?**  
A: 日志自动区分成功/失败,查询时过滤即可

**Q: 如何扩展模块识别规则?**  
A: 修改 `MmsLog.autoModule()` 方法,添加更多规则

**Q: 日志文件保存在哪里?**  
A: `./logs/oper/` 目录,按日期分文件

---

## 最佳实践

### Controller 层 - 优先使用注解

```java
// ✅ 推荐 - 简洁高效
@MmsLog
@PostMapping
public R add() { }

// ✅ 推荐 - 重要操作自定义
@MmsLog(description = "删除用户", saveBeforeData = true)
@DeleteMapping
public R delete() { }

// ❌ 不推荐 - 过度配置
@MmsLog(
    module = "用户管理",  // 可自动识别
    operType = OperationType.INSERT,  // 可自动识别
    description = "新增用户"  // 可自动识别
)
@PostMapping
public R add() { }
```

### Service 层 - 使用静态方法

```java
// ✅ 推荐 - 一个参数,自动识别
@Service
public class UserServiceImpl {
    public void processUser() {
        // 业务逻辑
        MmsLog.update("处理用户数据");  // 自动识别为 "用户管理"
    }
}

// ✅ 推荐 - 需要自定义模块时
public void processOrder() {
    MmsLog.update("订单管理", "处理订单数据");
}

// ❌ 不推荐 - 不要注入 OperLogService
private final OperLogService operLogService;  // 不需要!
```

### 复杂流程 - 记录关键节点

**重要:** 每次调用 `MmsLog.xxx()` 都会新增一条日志记录！

#### 方式 1: 只记录最终结果 (推荐)

```java
@PostMapping("/process")
public R process(@RequestBody Bo bo) {
    try {
        step1(bo);
        step2(bo);
        step3(bo);
        
        // ✅ 只在最后记录一次
        MmsLog.update("处理完成");
        return R.ok();
    } catch (Exception e) {
        MmsLog.logError("用户管理", OperationType.UPDATE, "处理失败", e);
        return R.fail(e.getMessage());
    }
}
// 结果: 成功时新增 1 条日志，失败时新增 1 条错误日志
```

#### 方式 2: 记录每个关键节点

```java
@PostMapping("/importData")
public R importData(@RequestParam MultipartFile file) {
    try {
        // 步骤 1: 解析文件
        List<User> users = parseFile(file);
        MmsLog.select("解析Excel文件: " + users.size() + " 条");
        
        // 步骤 2: 数据校验
        validateData(users);
        MmsLog.select("数据校验通过");
        
        // 步骤 3: 批量导入
        int success = batchImport(users);
        MmsLog.imports("导入用户: 成功 " + success + " 条");
        
        return R.ok();
    } catch (Exception e) {
        MmsLog.logError("用户管理", OperationType.IMPORT, "导入失败", e);
        return R.fail(e.getMessage());
    }
}
// 结果: 成功时新增 3 条日志，失败时新增 N 条 + 1 条错误日志
```

### 选择建议

| 场景 | 推荐方式 | 原因 | 日志条数 |
|-----|---------|------|--------|
| Controller 简单接口 | `@MmsLog` | 自动记录 HTTP 信息 | 1 条/请求 |
| Controller 复杂流程 | `MmsLog.xxx()` | 记录关键节点 | 按需记录 |
| Service 业务方法 | `MmsLog.xxx()` | 无 HTTP 上下文 | 按需记录 |
| 定时任务 | `MmsLog.xxx()` | 无 HTTP 上下文 | 按需记录 |
| 异步任务 | `MmsLog.xxx()` | 灵活调用 | 按需记录 |

**记录频率建议:**
- 简单流程: 只记录最终结果 (1 条日志)
- 复杂流程: 记录关键节点 (多条日志)
- 避免过度记录: 每个小步骤都记录会产生大量日志

---

---

## 快速参考

### 静态方法快速调用

```java
// 一个参数 - 自动识别模块 (推荐)
MmsLog.insert("新增用户");
MmsLog.update("修改用户");
MmsLog.delete("删除用户");
MmsLog.select("查询用户");
MmsLog.export("导出用户");
MmsLog.imports("导入用户");
MmsLog.clean("清空缓存");

// 两个参数 - 指定模块
MmsLog.insert("会员管理", "新增VIP会员");
MmsLog.update("订单管理", "修改订单状态");

// 记录错误
MmsLog.logError("用户管理", OperationType.UPDATE, "处理失败", e);
```

### 注解方式快速调用

```java
// 完全自动
@MmsLog
@PostMapping
public R add() { }

// 部分自定义
@MmsLog(description = "批量删除用户")
@DeleteMapping("/batch")
public R batchDelete() { }
```

---

**版本:** v2.0  
**更新日期:** 2026-01-03  
**新特性:** 支持静态方法全局调用,自动识别模块
