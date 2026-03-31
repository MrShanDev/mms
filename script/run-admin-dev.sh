#!/usr/bin/env bash
set -e

# 从当前脚本位置切回项目根目录
cd "$(dirname "$0")/.."

echo "使用 dev 环境启动 mms-api-admin ..."
echo "等价命令：mvn -pl mms-api-admin -am -Pdev spring-boot:run"

mvn -pl mms-api-admin -am -Pdev spring-boot:run

