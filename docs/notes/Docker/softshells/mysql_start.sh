#!/bin/bash
#$1 -> 版本号；$2 -> 端口号; $3 -> 服务名 $4 -> f 强制启动，会把已有的进程删除
#强制停止
if [ $4=="f" ]; then
       sudo docker stop mysql$1-$3
       sudo docker rm  mysql$1-$3
fi
# 启动
sudo docker run \
    --name mysql$1-$3 \
    --restart always \
    -d \
    -v $PWD/datadir:/var/lib/mysql \
    -v $PWD/conf.d:/etc/mysql/conf.d \
    -e MYSQL_ROOT_PASSWORD="1" \
    -p $2:3306 mysql:$1 \
    --general-log=ON \
    --general-log-file=/var/log/mysql/general-log.log