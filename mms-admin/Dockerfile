# 使用 Eclipse Temurin JDK 21 基础镜像，性能更好且社区支持更活跃
FROM eclipse-temurin:21-jre-alpine

MAINTAINER SXPCWLKJ

# 创建必要的目录结构（plugins：JAR 插件安装根目录，与 mms.plugin.root-dir / 环境变量 MMS_PLUGIN_ROOT_DIR 一致）
RUN mkdir -p /sxpcwlkj/mms-admin/logs \
    && mkdir -p /sxpcwlkj/mms-admin/files \
    && mkdir -p /sxpcwlkj/mms-admin/temp \
    && mkdir -p /sxpcwlkj/mms-admin/plugins \
    && mkdir -p /sxpcwlkj/mms-admin/skywalking/agent

WORKDIR /sxpcwlkj/mms-admin

# 设置环境变量（插件目录可挂卷覆盖；需其它 profile 时运行加 -e SPRING_PROFILES_ACTIVE=xxx）
# 文档站微信 payNotify 完整 URL，须为外网 HTTPS 且与网关 path 一致；生产请 -e MMS_DOC_PAY_NOTIFY_URL=实际地址
ENV SERVER_PORT=8080 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS="" \
    MMS_PLUGIN_ROOT_DIR=/sxpcwlkj/mms-admin/plugins \
    SPRING_PROFILES_ACTIVE=prod \
    MMS_DOC_PAY_NOTIFY_URL=https://demo.mmsadmin.cn/prod-api/doc/v1/payNotify

EXPOSE ${SERVER_PORT}

# 添加应用 JAR 文件
ADD ./target/mms-admin.jar ./app.jar

# JVM 选项须在 -jar 之前；Spring Profile 可放在 jar 之后（由 Spring Boot 解析）
# SkyWalking 示例：在 JAVA_OPTS 中加入 -javaagent:... 与 -Dskywalking.agent.service_name=...
# GC 日志：仅 warning 及以上打到 stdout（避免启动期 Young GC 刷屏）；需详细可改为 gc*=info 或输出到文件
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom \
           -Dserver.port=${SERVER_PORT} \
           -XX:+HeapDumpOnOutOfMemoryError \
           -Xlog:gc*=warning:stdout:time,tags \
           -XX:+UseG1GC \
           -XX:+UseStringDeduplication \
           ${JAVA_OPTS} \
           -jar app.jar \
           --spring.profiles.active=${SPRING_PROFILES_ACTIVE}

