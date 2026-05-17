# MMS (Main Backend)

English | [简体中文](README.md)

This folder contains the **main MMS backend** (a Git submodule inside the `mms-plus` workspace).

If you are working from the `mms-plus` root, start here for the full workspace overview:

- `../README.md`

---

## Typical dev flow (high-level)

1. Initialize submodules from `mms-plus` root:

```bash
git submodule update --init --recursive
```

2. Use the repo launcher from `mms-plus` root (recommended):

```bash
bash ../mms.sh
```

---

## Related

- Plugins aggregator: `../mms-plugins/`
- Admin UI: `../mms-ui/`
- Docs site: `../mms-doc/`

