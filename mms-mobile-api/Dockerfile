# 使用 Eclipse Temurin JDK 21 基础镜像，性能更好且社区支持更活跃
FROM eclipse-temurin:21-jre-alpine

MAINTAINER SXPCWLKJ

# 创建必要的目录结构
RUN mkdir -p /sxpcwlkj/mms-mobile-api/logs \
    && mkdir -p /sxpcwlkj/mms-mobile-api/files \
    && mkdir -p /sxpcwlkj/mms-mobile-api/temp \
    && mkdir -p /sxpcwlkj/mms-mobile-api/skywalking/agent

WORKDIR /sxpcwlkj/mms-mobile-api

# 设置环境变量
ENV SERVER_PORT=8060 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

# 添加应用 JAR 文件
ADD ./target/mms-mobile-api.jar ./app.jar

# 启动命令，使用 JDK 21 的最佳实践 JVM 参数
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom \
           -Dserver.port=${SERVER_PORT} \
           # SkyWalking APM 监控配置（可选） \
           # -Dskywalking.agent.service_name=sxpcwlkj-mms \
           # -javaagent:/sxpcwlkj/mms/skywalking/agent/skywalking-agent.jar \
           -jar app.jar \
           -XX:+HeapDumpOnOutOfMemoryError \
           -Xlog:gc*:stdout:time,tags \
           -XX:+UseG1GC \
           -XX:+UseStringDeduplication \
           -Dspring.profiles.active=docker ${JAVA_OPTS}

