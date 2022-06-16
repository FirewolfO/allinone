#!/bin/bash
#该脚本用于处理shell脚本编码问题
#需要被重新编码的文件所属目录
dir=$1
if [ -z $dir ]; then 
	dir=$PWD
fi
files=`ls -l $dir| grep -v '^d' | awk -F ' ' '{print $9}' | grep '.sh$'`
echo $files
for file in $files 
do
	echo "convert encoding for file: $dir/$file "
    sudo chmod a+x $dir/$file
	sed -i "s/\r//g" $dir/$file
done 