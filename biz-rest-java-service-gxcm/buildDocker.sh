#!/bin/bash

WORKDIR="$(cd `dirname $0`; pwd)"
cd $WORKDIR

docker build -f Dockerfile -t 192.168.6.11:8082/itesc/cqyd-biz-java-service:latest .
if [ $? != 0 ]; then
    echo "docker build failed!"
    exit 1
fi
docker push 192.168.6.11:8082/itesc/cqyd-biz-java-service
if [ $? != 0 ]; then
    echo "docker push failed!"
    exit 1
fi
exit 0