#FROM findepi/graalvm:java17-native
FROM openjdk:17.0.2-oraclelinux8

MAINTAINER SXPCWLKJ

RUN mkdir -p /sxpcwlkj \
    /sxpcwlkj/mms-powerjob \
    /sxpcwlkj/mms-powerjob/logs \
    /sxpcwlkj/mms-powerjob/files \
    /sxpcwlkj/mms-powerjob/temp \
    /sxpcwlkj/mms-powerjob/skywalking/agent

WORKDIR /sxpcwlkj/mms-powerjob

ENV SERVER_PORT=7700 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/mms-powerjob.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           # 应用名称 如果想区分集群节点监控 改成不同的名称即可
           #-Dskywalking.agent.service_name=sxpcwlkj-mms \
           #-javaagent:/sxpcwlkj/mms/skywalking/agent/skywalking-agent.jar \
           -jar app.jar \
           -XX:+HeapDumpOnOutOfMemoryError -Xlog:gc*,:time,tags,level -XX:+UseZGC ${JAVA_OPTS}

