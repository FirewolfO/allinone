#!/bin/bash
#start docker redis 
#$1 -> 版本号；$2 -> 端口号; $3 -> 名称 $4 -> f 强制启动，会把已有的进程删除
#强制停止
if [ $4=="f" ]; then
       sudo docker stop redis$1-$3
       sudo docker rm redis$1-$3
fi
# start
sudo docker run \
    --name redis$1-$3 \
    --restart always \
        -d \
    -p $2:6379 redis:$1