#!/bin/bash
dir=$1
outfile=$2
echo "protobuf dir si $1, targer file is $2"
function dealOneFile(){
  if [ -d $1 ]; then
    for f in `ls $1`
    do
      dealOneFile "$1/$f"
    done
  else
    if echo $1 | grep -q -E '\.proto$';then
      #拷贝service信息
      pkg=`grep "^[ \t]*package" $1 | awk -F " " "{print $2}" | sed -e 's/^[ ]*//g' | sed -e 's/[ ;]*$//g'`
      echo "current file is $1,package is $pkg"
    fi
  fi
}

dealOneFile $1
