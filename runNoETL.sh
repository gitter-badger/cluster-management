#!/usr/bin/env bash
source setEnvironment.sh

if [ -z "$1" ]
then echo "Using default log file ${logFile}" >> ${logFile}
else logFile=$1
fi

for f in ${rootPath}/ClusterStarted_JFId*
do
	echo "Parsing ${f}..." >> ${logFile}
	jobflow=`echo ${f} | cut -d@ -f2`
	echo "Job flow id: ${jobflow}" >> ${logFile}
	dns=`sed -n '1p' ${f} | cut -d: -f2`
	echo "DNS name: ${dns}" >> ${logFile}
	pipelineFiles=`sed -n '2p' ${f} | cut -d: -f2`
	echo "Files for pipeline: ${pipelineFiles}" >> ${logFile}
	echo "Printing split files:" >> ${logFile}
	IFS=',' read -r -a fileArray <<< "$pipelineFiles"
	for element in "${fileArray[@]}"
    do
        echo "moving $element to tmp directory" >> ${logFile}
        aws s3 mv ${noetlStage}/${element} ${noetlProcessing}/${element} --profile default
    done
done

#execute NoETL
#remove the ClusterStarted file at the last step of NoETL
