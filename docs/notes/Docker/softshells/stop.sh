#!/bin/bash
# $1 --> 容器名
sudo docker stop $1
sudo docker rm $1
