#!/bin/bash
#start docker redis 
#$1 -> 版本号；$2 -> 端口号; $3 -> f 强制启动，会把已有的进程删除
#强制停止
if [ $3 == "f" ]; then
       sudo docker stop redis$1
       sudo docker rm redis$1
fi
# start 
sudo docker run \
	--name redis$1 \
	--restart always \
       	-d \
	-p $2:6379 redis:$1