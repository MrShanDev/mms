<div align="center">
   <br/>
   <a href="https://mmsadmin.cn">
     <img width="150" src="https://mmsadmin.cn/logo.png" alt="MMS logo">
   </a>
   <h1>Modular Management System (MMS)</h1>
   <p><strong>mms · Main Backend / Core Framework</strong></p>
   <p><a href="https://mmsadmin.cn/">📘 Online Docs · mmsadmin.cn</a> · <a href="https://gitee.com/MrShanDev/mms">Gitee</a> · <a href="https://github.com/MrShanDev/mms">GitHub</a></p>
   <br/>
</div>

English | [简体中文](README.md)

This folder contains the **main MMS backend** — the core framework of **MMS (Modular Management System)**. It serves as the backend for all other MMS frontend / client projects.

For the overall architecture, plugin system, and deployment guides, see the online documentation at [mmsadmin.cn](https://mmsadmin.cn).

---

## Typical dev flow (high-level)

1. Clone and enter the project:

```bash
git clone https://gitee.com/MrShanDev/mms.git
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


## Development tool installation

Node.js usually includes npm; pnpm must be installed separately. nvm manages Node.js versions. Maven is a separate Java build tool whose command is `mvn`.

**macOS / Linux / WSL: nvm, Node.js and pnpm**

Follow the [official nvm instructions](https://github.com/nvm-sh/nvm#installing-and-updating), then reopen your terminal. Skip nvm if you already have a suitable Node.js version and do not need version switching.

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.8/install.sh | bash
```

In a new terminal:

```bash
command -v nvm
nvm install 22
nvm use 22
nvm alias default 22
node -v
npm -v
npm install -g pnpm@10
pnpm -v
```

Reinstall pnpm for the active Node.js version if it is missing after switching versions. Node.js 25+ does not bundle Corepack. On native Windows, use [nvm-windows](https://github.com/coreybutler/nvm-windows) or the official Node.js installer instead of the Shell script above.

**Install Maven for the backend**

Install JDK 21 and follow the [Maven installation guide](https://maven.apache.org/install.html). On macOS with Homebrew:

```bash
brew install maven
```

On Linux / Windows, extract the Maven binary distribution and add its `bin` directory to `PATH`. Set `JAVA_HOME` to your JDK 21 installation, reopen the terminal, and check:

```bash
java -version
mvn -v
```

Verify the Java version reported by Maven. On macOS with JDK 21 installed:

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
export PATH="$JAVA_HOME/bin:$PATH"
mvn -v
```

For `mvn: command not found`, check installation and the current terminal's `PATH`. Maven is required for the Java backend, not for running `mms-ui` alone.
