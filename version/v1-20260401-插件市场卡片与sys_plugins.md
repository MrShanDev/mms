# v1-20260401 插件市场卡片与 sys_plugins

## 目标

- 管理端「插件市场」以**卡片**呈现：**名称、封面图 URL、版本、运行状态**，并支持「安装（上传 JAR）」「卸载（删磁盘目录 + 全量重载）」「全量重载」。
- 点击卡片或「详情」弹出**功能介绍**及 **Manifest / 健康检查**（若有）。
- 新增表 **`sys_plugins`**：市场展示元数据（名称、icon_url、description、上架 status、排序 sort），与磁盘 `mms.plugin.root-dir` 安装、内存加载状态由接口合并。

## 运行态含义

| 状态            | 含义 | 界面文案（安装） |
|-----------------|------|------------------|
| `NOT_INSTALLED` | 磁盘无有效 lib/jar | 未安装 |
| `ON_DISK`       | 磁盘已安装但未在当前 JVM 加载（或宿主未启用） | 已安装（未加载） |
| `LOADED`        | 宿主已启用且 SPI 已加载 | 运行中 |

**健康**（仅运行中有意义）：`NORMAL` 正常 · `ABNORMAL` 异常 · `NO_SPI` 无探测 · `NONE` —。未运行时卡片不混合展示「正常/异常」。

**操作**：安装仅在页顶「上传插件包」；卡片仅 **详情**、**卸载**（已安装时）；**全量重载**仅在页顶。

## 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET  | `/system/pluginMarket/cards` | 卡片列表（`super_admin`） |
| POST | `/system/pluginHost/uninstall` | Body: `{ pluginId, version? }`，version 空则删该插件全部版本目录；随后全量 reload |

安装仍为 `POST /system/pluginHost/install`（multipart `file`）。

## 数据库

- 执行 `script/db/increment_20260401_sys_plugins.sql`（含示例健康插件一条上架数据）。
- 可在库内维护 `icon_url`、富文本级说明放在 `description`。

## 前端

- `mms-ui`：`views/system/pluginMarket/index.vue` 卡片布局 + `el-dialog` 详情。
