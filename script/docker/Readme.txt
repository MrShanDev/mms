#基础服务
docker-compose up -d mysql nginx-web redis minio
#业务服务
docker-compose up -d admin
#进入容器
docker exec -it admin  /bin/bash
#退出
exit
#日志
docker-compose logs -f admin