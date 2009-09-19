#!/bin/bash

# 標準入力から値を取得
(
tmp="";

echo "start. ";

# 区切り文字の変更 tabが無視されるのを回避している
IFS=$'\n';
while read line
do
 echo "${line}";
done

echo "end. ";
)
