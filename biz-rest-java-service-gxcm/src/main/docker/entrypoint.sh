#!/bin/sh
cd /biz-java-service
nohup java -jar biz-java-service.jar --spring.config.location=application.properties && tail -f ~/unios/var/log/biz-java-service/logs/log_info.log