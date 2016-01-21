#!/usr/bin/env bash
source setEnvironment.sh

# bash terminateCluster.sh false -> run without rebuilding
build=true
if [ -n "$1" ];then build=$1;fi
if ${build};then mvn clean -Dmaven.test.skip=true package;fi

java -jar ${rootPath}/${jarPath} --conf ${confFile} --clusterTermination
