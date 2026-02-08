#基础服务
docker-compose up -d mysql nginx-web redis minio
#先设置环境变量（示例）
#cp .env.example .env
#编辑 .env 填写 MYSQL_ROOT_PASSWORD
#业务服务
docker-compose up -d admin
#进入容器
docker exec -it admin  /bin/bash
#退出
exit
#日志
docker-compose logs -f admin