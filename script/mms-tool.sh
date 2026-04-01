#!/usr/bin/env bash

##
## MMS 一键工具脚本
##
## 功能：
##  1. 依赖全部下载 / 指定模块依赖下载
##  2. 启动 / 重启 后端模块（支持多环境：local / dev / prod）
##  3. 一键打包
##  4. 按模块构建 Docker 镜像并推送到远程仓库
##
## 用法：
##  cd 项目根目录（包含 pom.xml 那层）
##  chmod +x script/mms-tool.sh   # 第一次
##  script/mms-tool.sh
##

set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_DIR="${ROOT_DIR}/script/pids"
mkdir -p "${PID_DIR}"

color_echo() {
  local color="$1"; shift
  local msg="$*"
  case "$color" in
    red)    printf '\033[31m%s\033[0m\n' "$msg" ;;
    green)  printf '\033[32m%s\033[0m\n' "$msg" ;;
    yellow) printf '\033[33m%s\033[0m\n' "$msg" ;;
    blue)   printf '\033[34m%s\033[0m\n' "$msg" ;;
    *)      echo "$msg" ;;
  esac
}

press_enter() {
  echo
  read -rp "按回车键继续..." _
}

select_profile() {
  local profile
  echo
  echo "选择运行环境 profile（默认：local）："
  echo "  1) local"
  echo "  2) dev"
  echo "  3) prod"
  read -rp "请输入编号 [1-3] 或直接输入 profile 名称： " profile
  case "$profile" in
    ""|1) echo "local" ;;
    2) echo "dev" ;;
    3) echo "prod" ;;
    *) echo "$profile" ;;
  esac
}

select_module() {
  local module
  echo
  echo "选择模块："
  echo "  1) mms-admin           (系统管理后台)"
  echo "  2) mms-unix-api        (移动端 API)"
  echo "  3) mms-zoom/mms-monitor(监控服务)"
  echo "  4) mms-zoom/mms-powerjob(定时任务服务)"
  echo "  5) 自己手动输入模块路径（例如 mms-xxx 或 mms-zoom/mms-xxx）"
  read -rp "请输入编号 [1-5]： " module
  case "$module" in
    1) echo "mms-admin" ;;
    2) echo "mms-unix-api" ;;
    3) echo "mms-zoom/mms-monitor" ;;
    4) echo "mms-zoom/mms-powerjob" ;;
    5)
      read -rp "请输入模块路径（相对于项目根，例如 mms-admin 或 mms-zoom/mms-monitor）： " custom
      echo "$custom"
      ;;
    *)
      color_echo red "无效选择。"
      echo ""
      ;;
  esac
}

module_artifact_id() {
  # 根据模块路径返回 artifactId，默认使用最后一段目录名
  local module_path="$1"
  basename "$module_path"
}

ensure_jar_built() {
  local module_path="$1"
  local profile="$2"
  local artifact
  artifact="$(module_artifact_id "$module_path")"
  local jar_path="${ROOT_DIR}/${module_path}/target/${artifact}.jar"

  if [[ -f "$jar_path" ]]; then
    echo "$jar_path"
    return 0
  fi

  color_echo yellow "未找到 jar：${jar_path}，开始使用 Maven 打包（profile: ${profile}）..."
  (cd "${ROOT_DIR}" && mvn -pl "${module_path}" -am -P"${profile}" clean package -DskipTests)

  if [[ -f "$jar_path" ]]; then
    echo "$jar_path"
    return 0
  else
    color_echo red "打包失败：未找到 ${jar_path}"
    return 1
  fi
}

start_module_background() {
  local module_path="$1"
  local profile="$2"
  local artifact
  artifact="$(module_artifact_id "$module_path")"
  local pid_file="${PID_DIR}/${artifact}.pid"

  local jar_path
  jar_path="$(ensure_jar_built "$module_path" "$profile")" || return 1

  color_echo green "后台启动模块 ${module_path}（profile=${profile}）..."
  nohup java -jar "${jar_path}" --spring.profiles.active="${profile}" > "${ROOT_DIR}/logs/${artifact}.log" 2>&1 &
  local pid=$!
  echo "$pid" > "${pid_file}"
  color_echo green "已启动，PID=${pid}，日志：logs/${artifact}.log"
}

stop_module() {
  local module_path="$1"
  local artifact
  artifact="$(module_artifact_id "$module_path")"
  local pid_file="${PID_DIR}/${artifact}.pid"

  if [[ ! -f "$pid_file" ]]; then
    color_echo yellow "未找到 ${artifact} 的 PID 文件（${pid_file}），可能尚未通过本脚本启动。"
    return 0
  fi

  local pid
  pid="$(cat "$pid_file" 2>/dev/null || true)"
  if [[ -z "$pid" ]]; then
    color_echo yellow "PID 文件为空，跳过。"
    rm -f "$pid_file"
    return 0
  fi

  if kill -0 "$pid" 2>/dev/null; then
    color_echo yellow "正在停止 ${artifact} (PID=${pid}) ..."
    kill "$pid" || true
    sleep 3
    if kill -0 "$pid" 2>/dev/null; then
      color_echo yellow "进程仍在，尝试强制结束..."
      kill -9 "$pid" || true
    fi
  else
    color_echo yellow "进程 ${pid} 不存在，清理 PID 文件。"
  fi

  rm -f "$pid_file"
  color_echo green "已停止 ${artifact}。"
}

download_all_deps() {
  color_echo blue "开始下载/更新所有模块依赖并编译（不执行测试）..."
  (cd "${ROOT_DIR}" && mvn -U -T 1C clean install -DskipTests)
  color_echo green "依赖下载完成。"
}

download_module_deps() {
  echo
  echo "依赖下载（指定模块）"
  echo "提示：模块示例：mms-admin、mms-modules、mms-zoom/mms-monitor 等"
  read -rp "请输入模块路径（留空返回菜单）： " module_path
  [[ -z "$module_path" ]] && return

  color_echo blue "为模块 ${module_path} 下载依赖并编译（不执行测试）..."
  (cd "${ROOT_DIR}" && mvn -pl "${module_path}" -am clean install -DskipTests)
  color_echo green "模块 ${module_path} 依赖下载完成。"
}

start_module_menu() {
  local module_path profile
  module_path="$(select_module)"
  [[ -z "$module_path" ]] && return
  profile="$(select_profile)"
  [[ -z "$profile" ]] && profile="local"

  color_echo blue "前台启动模块 ${module_path}（使用 Maven spring-boot:run，profile=${profile}）..."
  echo "提示：按 Ctrl+C 可停止当前服务。"
  (cd "${ROOT_DIR}" && mvn -pl "${module_path}" -am -P"${profile}" spring-boot:run)
}

restart_module_menu() {
  local module_path profile
  module_path="$(select_module)"
  [[ -z "$module_path" ]] && return
  profile="$(select_profile)"
  [[ -z "$profile" ]] && profile="local"

  stop_module "$module_path"
  start_module_background "$module_path" "$profile"
}

package_menu() {
  local module_path profile
  echo
  echo "打包选项："
  echo "  1) 整个项目（所有模块）"
  echo "  2) 指定模块"
  read -rp "请选择 [1-2]： " choice

  profile="$(select_profile)"
  [[ -z "$profile" ]] && profile="local"

  case "$choice" in
    1)
      color_echo blue "打包整个项目（profile=${profile}）..."
      (cd "${ROOT_DIR}" && mvn -P"${profile}" clean package -DskipTests)
      ;;
    2)
      module_path="$(select_module)"
      [[ -z "$module_path" ]] && return
      color_echo blue "打包模块 ${module_path}（profile=${profile}）..."
      (cd "${ROOT_DIR}" && mvn -pl "${module_path}" -am -P"${profile}" clean package -DskipTests)
      ;;
    *)
      color_echo red "无效选择。"
      ;;
  esac

  color_echo green "打包完成。"
}

docker_build_and_push_menu() {
  local module_choice module_path dockerfile image repo tag full_image

  echo
  echo "Docker 构建 / 推送"
  echo "提示：项目中已有的 Dockerfile 主要示例："
  echo "  - mms-admin/Dockerfile"
  echo "  - mms-zoom/mms-monitor/Dockerfile"
  echo "  - mms-zoom/mms-powerjob/Dockerfile"
  echo
  module_path="$(select_module)"
  [[ -z "$module_path" ]] && return

  dockerfile="${ROOT_DIR}/${module_path}/Dockerfile"
  if [[ ! -f "$dockerfile" ]]; then
    color_echo red "未在 ${module_path} 下找到 Dockerfile：${dockerfile}"
    return
  fi

  read -rp "请输入镜像仓库前缀（例如：registry.example.com/mms 或留空使用本地 mms）： " repo
  read -rp "请输入镜像名称（默认使用模块名，如 mms-admin）： " image
  read -rp "请输入镜像 tag（默认：latest）： " tag

  if [[ -z "$image" ]]; then
    image="$(module_artifact_id "$module_path")"
  fi
  [[ -z "$tag" ]] && tag="latest"

  if [[ -z "$repo" ]]; then
    full_image="mms/${image}:${tag}"
  else
    full_image="${repo}/${image}:${tag}"
  fi

  color_echo blue "开始构建镜像：${full_image}"
  (cd "${ROOT_DIR}/${module_path}" && docker build -t "${full_image}" -f "${dockerfile}" .)
  color_echo green "镜像构建完成：${full_image}"

  read -rp "是否推送到远程仓库？[y/N]： " push_choice
  case "$push_choice" in
    y|Y)
      color_echo blue "推送镜像到远程仓库：${full_image}"
      docker push "${full_image}"
      color_echo green "推送完成。"
      ;;
    *)
      color_echo yellow "已跳过推送，仅在本地保留镜像：${full_image}"
      ;;
  esac
}

main_menu() {
  while true; do
    echo
    color_echo blue "=========== MMS 工具脚本 ==========="
    echo "项目根目录：${ROOT_DIR}"
    echo
    echo "  1) 依赖全部下载（所有模块）"
    echo "  2) 指定模块依赖下载"
    echo "  3) 启动模块（前台，便于调试）"
    echo "  4) 重启模块（后台：先停再起，记录 PID）"
    echo "  5) 打包（整项目 / 指定模块）"
    echo "  6) 构建并推送 Docker 镜像"
    echo "  0) 退出"
    echo "------------------------------------"
    read -rp "请选择操作 [0-6]： " choice
    case "$choice" in
      1) download_all_deps; press_enter ;;
      2) download_module_deps; press_enter ;;
      3) start_module_menu ;;
      4) restart_module_menu; press_enter ;;
      5) package_menu; press_enter ;;
      6) docker_build_and_push_menu; press_enter ;;
      0) exit 0 ;;
      *) color_echo red "无效选择，请重新输入。"; press_enter ;;
    esac
  done
}

main_menu

