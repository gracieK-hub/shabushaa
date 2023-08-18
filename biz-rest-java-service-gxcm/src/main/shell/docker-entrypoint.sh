#!/bin/bash

is_docker_container=`cat /etc/issue|grep Alpine`
if [ -z "$is_docker_container" ];
then
    echo "请注意，这个shell脚本只用来在docker环境下使用。"
    echo "非docker环境启停服务请使用startup.sh和shutdown.sh或者service.sh脚本。"
    exit 0
fi

#获取shell脚本所在的绝对路径
SOURCE="$0"
while [ -h "$SOURCE"  ];
do
    DIR="$( cd -P "$( dirname "$SOURCE"  )" && pwd  )"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /*  ]] && SOURCE="$DIR/$SOURCE"
done
APP_BIN="$(cd -P "$( dirname "$SOURCE"  )" && pwd)"
APP_HOME="$(dirname $APP_BIN)"
APP_CONF="$APP_HOME/conf"
APP_LOGS="$APP_HOME/logs"
APP_JAR="`find $APP_HOME/*.jar`"
APP_NAME="`basename $APP_JAR`"

#自动查找配置文件
APPLICATION_FILE=""
for file in $APP_CONF/*;
do
    if [[ $file =~ ^$APP_CONF/application.*.yml ]];
    then
        APPLICATION_FILE=$file,$APPLICATION_FILE
    elif [[ $file =~ ^$APP_CONF/application.*.properties ]];
    then
        APPLICATION_FILE=$file,$APPLICATION_FILE
    elif [[ $file =~ ^$APP_CONF/bootstrap.*.yml ]];
    then
        APPLICATION_FILE=$file,$APPLICATION_FILE
    elif [[ $file =~ ^$APP_CONF/bootstrap.*.properties ]];
    then
        APPLICATION_FILE=$file,$APPLICATION_FILE
    fi
done
APPLICATION_FILE=${APPLICATION_FILE%,}

JAVA_OPTS="-server -Xms1g -Xmx1g -Xmn512m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:-UseLargePages -XX:+CMSParallelRemarkEnabled -XX:ParallelGCThreads=4 -Djava.awt.headless=true"
#容器内部启动服务必须作为前台进程，否则容器会直接停掉
java -jar $JAVA_AGENT $JAVA_OPTS $APP_JAR --xjar.password=${JAR_PASSWORD} --spring.config.location=$APPLICATION_FILE
