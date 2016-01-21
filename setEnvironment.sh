#!/usr/bin/env bash
echo "Environmental Variables:"

confFileName=conf.json

rootPath=`awk -F : '/rootPath/ { print $2 }' ${confFileName} | tr -d [:space:],\" | head -1`
echo "rootPath: ${rootPath}"

jarPath=target/cluster-management-1.0-SNAPSHOT-jar-with-dependencies.jar
echo "jarPath: ${jarPath}"

confFile=${rootPath}/${confFileName}
echo "confFile: ${confFile}"

logFile=`awk -F : '/logFile/ { print $2 }' ${confFileName} | tr -d [:space:],\" | head -1`
echo "logFile: ${logFile}"

noetlStage=s3://noetl-chen
echo "noetlStage: ${noetlStage}"

noetlProcessing=s3://noetl-chen/tmp
echo "noetlProcessing: ${noetlProcessing}"

echo
echo
