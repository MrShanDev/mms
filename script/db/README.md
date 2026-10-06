# 开源数据库初始化

四份初始化 SQL 面向**全新空库**。文件采用 UTF-8 无 BOM、LF；连接、表及字符串列统一 `utf8mb4` / `utf8mb4_unicode_ci`，存储引擎为 InnoDB，行格式为 DYNAMIC。

| 文件 | 职责 | 初始化数据 |
| --- | --- | --- |
| `mms.sql` | 18 张系统核心表，含用户外观偏好表 | 最小管理员、默认租户/部门/岗位、系统角色、菜单/权限、核心字典、空值服务配置 |
| `mms-gen.sql` | 6 张生成器表 | 当前项目 BaseEntity 基类、31 条通用字段类型映射 |
| `mms-plugin.sql` | 插件市场、安装版本两张表 | 插件市场一级菜单及两类角色授权；安装记录为空 |
| `mms-job.sql` | PowerJob **4.3.9** 的 10 张 `pj_*` 表 | 不携带账号、应用、任务、实例、工作流和服务器记录 |

## 兼容条件

- MySQL **5.7.8+** 与 **8.x**。用户外观偏好表使用原生 JSON，不支持 5.7.8 之前版本。
- MySQL 5.7 使用 `innodb_file_per_table=ON`、`innodb_file_format=Barracuda`、`innodb_large_prefix=ON`，通常为默认配置；8.x 不再设置后两项。采用默认 16 KiB 页大小，索引上限 3072 字节。不以截断唯一索引绕过旧环境限制。
- 移除 `utf8mb4_0900_ai_ci`、不可见列、历史 dump 的版本条件指令。所有种子 INSERT 明确列名。
- 不指定库名、执行账户、宿主地址或 DEFINER，使用者自行选择目标库。
- 普通 CREATE TABLE 遇到已有表会报错；不会 DROP/TRUNCATE 已有表。脚本不保证重复执行，也不是已有库升级脚本。导入失败后检查空库状态，避免带着部分导入结果继续执行。

## 导入顺序

使用者先创建空库，选择 `utf8mb4` / `utf8mb4_unicode_ci`。以下命令从**母仓 mms-plus 根目录**执行，账号及库名按实际环境替换，`-p` 交互输入密码：

```bash
mysql --default-character-set=utf8mb4 -u root -p mms < mms/script/db/mms.sql
mysql --default-character-set=utf8mb4 -u root -p mms < mms/script/db/mms-gen.sql
mysql --default-character-set=utf8mb4 -u root -p mms < mms/script/db/mms-plugin.sql
mysql --default-character-set=utf8mb4 -u root -p mms < mms/script/db/mms-job.sql
```

生成器表结构独立，管理端使用生成器需要系统库。插件脚本依赖系统库的菜单、角色及授权表。调度脚本只包含 PowerJob 表，可导入独立库，并配置 `POWERJOB_DB_URL` 指向该库。

`patch/`、`upgrade/` 和 `mms/script/upgrade/` 是历史增量入口，保留用于已有库升级。**不要**在全新初始化后批量执行历史增量：外观偏好表、插件市场一级菜单等已纳入新基线。

## 首次登录与配置

- 默认租户 `000000`；管理员 `admin`；公开示例密码 `Mms@123456`。角色为 `super_admin`。初始化后先更改密码，再配置外部服务。
- 密码散列按当前 `SignUtil.pressWord` 算法和公开示例盐值重新生成，未沿用历史账号散列、AES 密钥或 RSA 私钥。邮箱、手机、微信标识、登录 IP 和登录时间均为空。
- 默认部门和岗位仅用于最小账户关联；真实组织、地址、联系人已移除。管理员角色 `admin` 保留原有有效权限，未创建额外用户。
- 公告、日志、签名、OSS 文件/配置、租户套餐为空。短信、邮件、微信凭据、签名、回调及第三方地址为空；邮件/微信及多租户状态配置默认关闭。品牌图片自行配置。
- 生成器不预存数据库连接，主数据源使用后端现有配置，额外数据源按需添加。项目改名路径、生成历史、导入表字段为空。基类字段与当前 BaseEntity 一致。
- 插件市场没有预登记插件或虚构激活版本，安装后由正常流程登记。业务插件的 `mms-plugins/<模块>/script/` 由插件独立管理。
- PowerJob 启动后在界面注册应用 `mms`，自行设置应用密码、通知用户，再开启管理端 `power-job.worker.enabled`。旧脚本混入的 5.x 专用 `pj_namespace`、`pj_pwjb_user_info`、`pj_sundry`、`pj_user_role` 及扩展列已移除；`ownerip`、`pedag` 按当前命名策略保留。
- PowerJob 4.3.9 的 RemoteJpaConfig 默认使用 Hibernate `ddl-auto=update`，预建表不能替代版本升级管理。切换依赖版本后重新核对实体和迁移过程。

## 验证记录

已检查编码、表归属、种子列数、主键/唯一键、字符串长度、菜单父级、角色授权、字典引用及敏感数据残留；逐表核对本地 PowerJob 4.3.9 persistence JAR 的全部实体字段，验证 Hibernate 对 `ownerIP` / `peDAG` 的命名结果。公开示例密码已用 Hutool MD5 按项目参数验证。

当前 Docker daemon 未运行，**尚未完成 MySQL 5.7 / 8.x 实际导入及应用登录联调**。本次只整理文件，未对数据库执行写操作。清洗范围仅覆盖这四份初始化 SQL，历史增量及模型文件未同步清洗。

兼容依据：[MySQL 5.7 手册](https://downloads.mysql.com/docs/refman-5.7-en.a4.pdf)、[MySQL 8.0 不可见列与 INSERT 规则](https://dev.mysql.com/doc/refman/8.0/en/invisible-columns.html)。
