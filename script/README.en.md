# mms/script (DB Scripts & Docker Assets)

English | [简体中文](README.md)

This folder contains runtime assets for the **MMS backend**, including:

- **Database initialization scripts** (`db/`)
- **Upgrade SQL scripts** (`upgrade/`)
- **Docker compose & config templates** (`docker/`)
- Helper scripts (e.g. `mms-tool.sh`)

---

## Layout

```text
script/
├── db/
├── upgrade/
├── docker/
├── docker-compose/     # offline docker-compose binaries (legacy)
└── mms-tool.sh
```

---

## Database

Init SQL:

- `db/mms.sql`

Upgrade SQLs:

- `upgrade/*.sql`

> Some plugins also ship their own `script/install.sql` / `schema.sql` inside plugin modules; those are applied via the host plugin installation mechanism.

---

## Docker

The `docker/docker-compose.yml` starts MySQL/Redis/Nginx and some MMS services (depending on the images you use).

1) Create `.env`:

```bash
cp docker/.env.example docker/.env
```

2) Start:

```bash
cd docker
docker compose up -d
```

> Note: the current compose uses `network_mode: "host"` and mounts volumes under `/docker/...` on the host machine.
