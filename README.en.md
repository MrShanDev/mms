<div align="center">
   <br/>
   <a href="https://mmsadmin.cn">
     <img width="150" src="https://mmsadmin.cn/logo.png" alt="MMS logo">
   </a>
   <h1>Modular Management System (MMS)</h1>
   <p><strong>mms · Main Backend / Core Framework</strong></p>
   <p><a href="https://mmsadmin.cn/">📘 Online Docs · mmsadmin.cn</a> · <a href="https://gitee.com/LumeCode/mms">Gitee</a> · <a href="https://github.com/MrShanDev/mms">GitHub</a></p>
   <br/>
</div>

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

