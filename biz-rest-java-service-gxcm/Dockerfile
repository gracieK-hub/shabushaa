FROM 192.168.6.11:8082/jdk1.8.0_162

MAINTAINER unic <unic@unitechs.com>

ADD target/biz-java-service.tar.gz /

#RUN chmod 755 /biz-java-service/docker-entrypoint.sh

# Set the environment variables
ENV BUILD_NUMBER biz-java-service
ENV JAVA_OPTS="-server -Xms1024M -Xmx2048M -XX:+CMSParallelRemarkEnabled -XX:ParallelGCThreads=4 -Djava.awt.headless=true"

WORKDIR /biz-java-service

EXPOSE 7072

# Get ready to run command
ENTRYPOINT exec java $JAVA_OPTS -jar biz-java-service.jar --spring.config.location=conf/bootstrap.properties,conf/application.properties
