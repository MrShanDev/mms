#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

function print_menu() {
  echo ""
  echo "MMS 启动菜单"
  printf "%-38s %s\n" "1) 启动 mms-admin (profile=local)" "2) 启动 mms-admin (profile=dev)"
  printf "%-38s %s\n" "3) 启动 mms-admin (profile=prod)" "4) 启动 mms-monitor"
  printf "%-38s %s\n" "5) 启动 mms-powerjob (profile=local)" "6) 重启 8789 代理"
  printf "%-38s %s\n" "7) 重启 Codex" "8) 重启 CC Switch"
  printf "%-38s %s\n" "9) 退出"
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

# —— 服务重启函数 ——
MIMO2CODEX_OPTS="${MIMO2CODEX_OPTS:---model ds --port 8789 --api-key sk-78eb5d7dc1764adab7415460ef355015}"

function restart_8789() {
  echo ">>> 检查 8789 代理状态..."
  if lsof -ti :8789 > /dev/null 2>&1; then
    echo ">>> 8789 已运行，正在停止..."
    lsof -ti :8789 | xargs kill
    sleep 2
  fi
  echo ">>> 启动 8789 代理 (mimo2codex)..."
  mimo2codex ${MIMO2CODEX_OPTS} &
  echo ">>> 8789 代理已在后台启动"
}

function restart_codex() {
  echo ">>> 检查 Codex 状态..."
  if pgrep -f "Codex.app" > /dev/null 2>&1; then
    echo ">>> Codex 正在运行，正在停止..."
    pkill -f "Codex.app"
    sleep 3
  fi
  echo ">>> 启动 Codex..."
  open /Applications/Codex.app
  echo ">>> Codex 已启动"
}

function restart_cc_switch() {
  echo ">>> 检查 CC Switch 状态..."
  if pgrep -f "CC Switch.app" > /dev/null 2>&1; then
    echo ">>> CC Switch 正在运行，正在停止..."
    pkill -f "CC Switch.app"
    sleep 3
  fi
  echo ">>> 启动 CC Switch..."
  open /Applications/CC\ Switch.app
  echo ">>> CC Switch 已启动"
}

while true; do
  print_menu
  read -r -p "请选择 [1-9]: " choice
  case "${choice}" in
    1) run_admin "local" ;;
    2) run_admin "dev" ;;
    3) run_admin "prod" ;;
    4) run_monitor ;;
    5) run_powerjob "local" ;;
    6) restart_8789 ;;
    7) restart_codex ;;
    8) restart_cc_switch ;;
    9) echo "已退出"; exit 0 ;;
    *) echo "无效选择，请重试" ;;
  esac
done
