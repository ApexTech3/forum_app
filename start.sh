#!/bin/bash
nohup ./gradlew bootRun > log.txt 2>&1 &
echo $! > ./pid.file
