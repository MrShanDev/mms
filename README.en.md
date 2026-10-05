# MMS (Main Backend)

English | [简体中文](README.md)

This folder contains the **main MMS backend** — the core framework of **MMS (Modular Management System)**. It serves as the backend for all other MMS frontend / client projects.

For the overall architecture, plugin system, and deployment guides, see the online documentation at [mmsadmin.cn](https://mmsadmin.cn).

---

## Typical dev flow (high-level)

1. Clone and enter the project:

```bash
git clone https://gitee.com/LumeCode/mms.git
cd mms
```

2. Build:

```bash
mvn -DskipTests package
```

---

## Related

- Plugins aggregator: `../mms-plugins/`
- Admin UI: `../mms-ui/`
- Docs site: `../mms-doc/`

