#!/bin/bash
#$1 -> 版本号；$2 -> 端口号; $3 -> f 强制启动，会把已有的进程删除
#start
if [ $3 == 'f' ] then  
sh stop_base.sh mysql$1
sudo docker run \
    --name mysql$1 \
    --restart always \
    -d \
    -v $PWD/datadir:/var/lib/mysql \
    -v $PWD/conf.d:/etc/mysql/conf.d \
    -e MYSQL_ROOT_PASSWORD="1" \
    -p $1:3306 mysql:$2 \
    --general-log=ON \
    --general-log-file=/var/log/mysql/general-log.log
