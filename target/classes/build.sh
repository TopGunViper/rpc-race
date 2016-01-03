#!/bin/bash

CUR=$(pwd)

## reset dest folder
OUTPUT_PATH=${CUR}/dest
rm -rf $OUTPUT_PATH
mkdir -p ${OUTPUT_PATH}/bin
mkdir -p ${OUTPUT_PATH}/libs

## 获取依赖库和需要编译的java文件
export libs=`find libs -name "*.jar" |xargs|sed "s/ /:/g"`
export jfiles=`find src -name "*.java" |xargs|sed "s/ / /g"`

## 编译
opt="-d $OUTPUT_PATH/bin -cp ${libs} -encoding utf-8"
javac $opt ${jfiles}

## 打包
cd ${OUTPUT_PATH}/bin
jar -cvf ${CUR}/dest/mylib.jar *
cd ${CUR}/src
jar -cvf ${CUR}/dest/mylib-source.jar *