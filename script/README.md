# mms/script（数据库脚本 & Docker 部署资源）

[English](README.en.md) | 简体中文

本目录存放 **MMS 主后端**配套的脚本与部署资源，主要包括：

- **数据库初始化脚本**（`db/`）
- **升级脚本**（`upgrade/`）
- **Docker 运行所需配置**（`docker/`）
- 辅助脚本（如 `mms-tool.sh`）

---

## 目录结构

```text
script/
├── db/                      # 数据库初始化（mms.sql）、PDMan 模型等
├── upgrade/                 # 版本升级 SQL（按日期/版本递增）
├── docker/                  # docker-compose 与 nginx/mysql/redis 配置模板
├── docker-compose/          # 历史 docker-compose 二进制（仅供离线环境）
└── mms-tool.sh              # 工具脚本（按脚本内容使用）
```

---

## 数据库（初始化与升级）

### 初始化

初始化脚本位于：

- `db/mms.sql`

通常流程：

1. 创建数据库（例如 `sxpcwlkj_mms`）
2. 执行 `db/mms.sql`
3. 再按需执行 `upgrade/` 下的增量脚本

> 注意：插件也可能带自己的 `script/install.sql` / `schema.sql`（在插件模块内），安装插件时按宿主的插件安装机制执行/导入。

### 升级脚本

升级脚本位于 `upgrade/`，文件名使用日期/版本前缀递增，建议在每次发布时形成“升级说明 + SQL 脚本”对。

---

## Docker（本地/服务器部署依赖）

`docker/` 下提供 `docker-compose.yml` 以及 nginx/mysql/redis 的配置模板。

### 1) 准备环境变量

复制并填写 `.env`（当前主要用于 MySQL root 密码）：

```bash
cp docker/.env.example docker/.env
```

### 2) 启动基础依赖（MySQL/Redis/Nginx）

> 当前 compose 使用 `network_mode: "host"`，并把配置/数据卷映射到宿主机 `/docker/...`。这对服务器部署方便，但对本地开发/非 Linux 环境不够友好；如需跨平台，建议额外提供一份 bridge 网络的 compose。

```bash
cd docker
docker compose up -d
```

---

## 常见问题（新用户最容易踩的坑）

1. **compose 使用 host 网络 + 固定 `/docker` 路径**  
   - 需要提前在宿主机准备目录与权限，否则容器会启动失败或配置不生效。
2. **数据库初始化脚本在哪里**  
   - 统一在 `db/mms.sql`；升级脚本在 `upgrade/`。
