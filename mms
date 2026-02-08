#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

function print_menu() {
  echo ""
  echo "MMS 启动菜单"
  echo "1) 启动 mms-admin (profile=local)"
  echo "2) 启动 mms-admin (profile=dev)"
  echo "3) 启动 mms-admin (profile=prod)"
  echo "4) 启动 mms-monitor"
  echo "5) 启动 mms-powerjob (profile=local)"
  echo "6) 退出"
  echo ""
}

function run_admin() {
  local profile="$1"
  echo ">>> 启动 mms-admin (profile=${profile})"
  cd "${ROOT_DIR}/mms-admin"
  mvn spring-boot:run -Dspring-boot.run.profiles="${profile}"
}

function run_monitor() {
  echo ">>> 启动 mms-monitor"
  cd "${ROOT_DIR}/mms-zoom/mms-monitor"
  mvn spring-boot:run
}

function run_powerjob() {
  local profile="$1"
  echo ">>> 启动 mms-powerjob (profile=${profile})"
  cd "${ROOT_DIR}/mms-zoom/mms-powerjob"
  mvn spring-boot:run -Dspring-boot.run.profiles="${profile}"
}

while true; do
  print_menu
  read -r -p "请选择 [1-6]: " choice
  case "${choice}" in
    1) run_admin "local" ;;
    2) run_admin "dev" ;;
    3) run_admin "prod" ;;
    4) run_monitor ;;
    5) run_powerjob "local" ;;
    6) echo "已退出"; exit 0 ;;
    *) echo "无效选择，请重试" ;;
  esac
done
