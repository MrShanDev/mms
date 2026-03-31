---
name: mms-doc-sync
description: 当用户要求更新/同步 mms-doc 在线文档时使用。解析同级 mms-doc 仓库、对比 mms 代码与文档差异；维护 docs/index、mms-api-admin、mms-ui 文章，并同步 docs/.vitepress/config.mts 中的顶部导航与侧栏菜单。
---

# mms-doc 文档同步（VitePress）

## 文档站位置

- 假定 **MMS 主工程** 仓库根目录为 `{mmsRoot}`（含 `mms-api-admin`、`mms-modules`、`mms-ui` 等）。
- **在线文档仓库**（二选一，**优先子模块**）：
  1. `{mmsRoot}/mms-doc`（主仓 **Git 子模块**，`clone --recurse-submodules` 或 `git submodule update --init` 后可用）
  2. 与主工程 **同级**：`{mmsRoot}/../mms-doc`（历史布局，仍兼容）
- 若上述路径均不存在，或缺少 `package.json`、或 `scripts` 中未包含 `vitepress dev docs`，则向用户说明未找到文档仓，勿编造路径。

内容根目录为 **`mms-doc/docs/`**（VitePress：`npm run dev` → `vitepress dev docs`）。重点栏目：

| 相对路径 | 含义 |
|---------|------|
| `docs/index.md` | 站点首页 |
| `docs/index/*.md` | Admin 导览区（introduction、mmsAdmin、mmsAdmin-ui、deploy 等） |
| `docs/mms-api-admin/*.md` | 后端 / 管理端能力说明 |
| `docs/mms-admin/modules-map.md`、`scaffold-evolution.md` | `mms-modules` 子模块速查、分阶段质量与插件路线 |
| `docs/mms-admin/saas-tenant-gap.md`、`plugin-jar-phases.md`、`plugin-overview.md`、`plugin-developer-guide.md` | SaaS 缺口；JAR 插件阶段（P0～P4）；插件体系介绍与开发指南 |
| `docs/index/scaffold-capability-matrix.md`、`docs/mms-ui/plugin-route-protocol.md` | 脚手架能力矩阵、插件与 mms-ui 动态路由协议 |
| 主仓 `mms-ui/src/views/system/pluginMarket/` | 插件市场页：与 `plugin-overview` / `plugin-developer-guide` / `plugin-jar-phases` 叙述一致时，同步文档中的**操作说明**（详情入口、安装/卸载/删除语义、运行中隐藏安装与删除、`pluginsRootReady` 与列表「磁盘」标记等） |
| `docs/mms-ui/**/*.md` | 管理端前端（MMS-UI）说明 |
| `docs/.vitepress/config.mts` | **站点菜单**：`themeConfig.nav`（顶栏）、`themeConfig.sidebar`（侧栏） |

### `config.mts` 菜单维护规则

- **`themeConfig.nav`**：顶栏链接。若新增**独立一级入口**（极少见），在此增加 `text` + `link` 或 `items` 下拉；否则多数新文档只需侧栏。
- **`themeConfig.sidebar`**：按 **路由前缀** 分桶。`docs/index/**`、`docs/mms-api-admin/**`、`docs/mms-ui/**` 当前共用默认桶 **`'/'`**（与 `/faq/`、`/cms/` 等分桶并存；VitePress 取最长匹配前缀）。
- **新增 `.md` 时**：在 `'/'` 下找到对应分组（如「入门指南」「项目功能 / 基础功能」「前端教程」），增加 `{ text: '…', link: '/path' }`。`link` 与文件路径一致：`docs/foo/bar.md` → `/foo/bar`（`index.md` 可写 `/foo/` 或 `/foo/index` —— 与同文件现有条目风格一致）。
- **仅改标题或文档定位时**：同步更新侧栏/顶栏 `text`，避免菜单与正文 H1 严重不一致。
- **不要**漏改侧栏：新增页面若只写 Markdown、不改 `config.mts`，读者在默认侧栏中**看不到入口**。

## 触发语与动作

当用户在对话中明确说 **「更新 mms-doc 文档」「同步 mms-doc」「完善在线文档」** 等时：

1. **定位仓库**：解析 `{mmsRoot}/../mms-doc`，确认 `docs/` 与 `.vitepress/config.mts` 存在。
2. **对比源**：以当前 `mms` 工作区已实现的代码、配置与 **`.cursor/skills/mms-kills/SKILL.md`**、**`.cursor/skills/mms-plugin/SKILL.md`**（插件宿主、市场 API/UI、`removeCatalog`、磁盘探测、`status` 字段等）为准，识别文档缺口或过时描述（接口路径、权限、`formLayout`、分页结构等）。
3. **版本与修订记录**：
   - **产品版本**：仍以 `docs/log/index.md` 中既有 `v1.0.x` 系列为准；勿随意改号，除非用户明确要求发布新版本说明。
   - **文档修订**：在 `docs/log/index.md` **顶部**（第一个 `# 日志` 标题之后）追加一节，标题格式：`## 文档修订 YYYY-MM-DD`，下列出本次相对上一版文档的变更要点（对应改了哪些 `.md`、对齐了哪些代码行为）。若同日多次同步，可用后缀 `(2)` 或补充小节区分。
   - VitePress 已开启 `lastUpdated: true`，单页会以 Git 最后提交时间显示页脚；**结构化「相对上一稿写了什么」仍以 `log/index.md` 为准**，便于人工对比。
4. **优先完善的目录**：按用户点名或按缺口——仅在 **`docs/index/`**、**`docs/mms-api-admin/`**、**`docs/mms-ui/`** 内补写或修订；其他目录（如 `mms-unix`）除非本轮变更相关否则不扩散范围。
5. **同步 `config.mts`**：与本轮修改的 Markdown **同一提交意图**内，更新 **`docs/.vitepress/config.mts`** 中 `nav` / `sidebar`（见上文规则）；若页面删除或路由重命名，**删除或替换**对应菜单项，避免死链。
6. **文风**：与现有文档一致（简体中文、必要时 tip 容器）、外链与图片保持可访问；不确定的路径不写死，可写「以当前仓库 `xxx` 为准」。

## 与 mms-kills 的关系

- **代码与接口事实**：以 `mms` 仓库与 `mms-kills` 为准。
- **对外叙述**：写入 `mms-doc`；两边冲突时以代码为准并更新文档。

## 自检清单（每次同步后）

- [ ] `docs/log/index.md` 已追加「文档修订」条目。  
- [ ] `docs/.vitepress/config.mts` 中 **`nav` / `sidebar`** 已与本轮页面增删改一致（含文案、路径、分组位置）。  
- [ ] 改动的页面在本地 `vitepress dev docs` 下路由可打开（路径与 `config.mts` 一致）。  
- [ ] `index` / `mms-api-admin` / `mms-ui` 中新增交叉链接若指向站内，使用 VitePress 路径风格（如 `/mms-api-admin/page`）。
