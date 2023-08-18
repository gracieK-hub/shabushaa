#!/bin/bash

# 检查Java基础环境,如果环境上没有把Jdk8设置变量JAVA_HOME,那么需要自己在这里进行设置
JAVA_HOME=`echo $JAVA_HOME`
if [ "$JAVA_HOME" != "" ];
then
    JAVA_VERSION=`$JAVA_HOME/bin/java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`
    IS_Jdk8=`echo $JAVA_VERSION | grep "1.8"`
    if [ "$IS_Jdk8" != "" ];
    then
        JAVA="$JAVA_HOME/bin/java"
    else
        echo "This env not install Jdk8,please install Jdk8"
        exit 0
    fi
else
    JAVA_VERSION=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`
    if [ $? = 0 ];
    then
        IS_Jdk8=`echo $JAVA_VERSION | grep "1.8"`
        if [ "$IS_Jdk8" == "" ];
        then
            echo -e "This env install Jdk,but not Jdk8,please install Jdk8"
            exit 0
        else
            JAVA=`which java 2>&1 |awk 'NR==1{ gsub(/"/,""); print $0 }'`
        fi
    else
        echo "This env not install Jdk8,please install Jdk8"
        exit 0
    fi
fi

# 获取shell脚本所在的绝对路径
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
echo "APP_BIN="$APP_BIN
echo "APP_HOME="$APP_HOME
echo "APP_CONF="$APP_CONF
echo "APP_LOGS="$APP_LOGS

####
# JDK启动参数配置
JAVA_OPTS=" -server -Xms1g -Xmx2g -Xmn512m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=1024m -XX:+UseG1GC -XX:+ScavengeBeforeFullGC"
JAVA_AGENT=""

# 自动查找配置文件
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
    elif [[ $file =~ ^$APP_CONF/logback-test.xml ]];
    then
        str=`cat $APP_CONF/logback-test.xml |grep '<property name="log.home" value=".*"'|awk '{print $3}'`
        str=${str/value=\"/}
        str=${str/\"/}
        sed -i "s#$str#${APP_LOGS//\//\\/}#g" $APP_CONF/logback-test.xml
    elif [[ $file =~ ^$APP_CONF/logback-prod.xml ]];
    then
        str=`cat $APP_CONF/logback-prod.xml |grep '<property name="log.home" value=".*"'|awk '{print $3}'`
        str=${str/value=\"/}
        str=${str/\"/}
        sed -i "s#$str#${APP_LOGS//\//\\/}#g" $APP_CONF/logback-prod.xml
    elif [[ $file =~ ^$APP_CONF/kafka_client_jaas.conf ]];
    then
        JAVA_OPTS="-Djava.security.auth.login.config=$file"$JAVA_OPTS
    fi
done
APPLICATION_FILE=${APPLICATION_FILE%,}

PID=$(ps aux | grep ${APP_JAR} | grep -v grep | awk '{print $2}' )
function check_if_process_is_running
{
    if [ "$PID" = "" ];
    then
        return 1
    fi
    ps -p $PID | grep "java"
    return $?
}

case "$1" in
    status)
        if check_if_process_is_running
        then
            echo -e "$APP_NAME is running"
        else
            echo -e "$APP_NAME not running"
        fi
        ;;
    stop)
        if ! check_if_process_is_running
        then
            echo -e "$APP_NAME already stopped"
            exit 0
        fi
        kill $PID
        echo -e "Waiting for process to stop"
        NOT_KILLED=1
        for i in {1..20}; do
            if check_if_process_is_running
            then
                echo -ne "."
                sleep 1
            else
                NOT_KILLED=0
            fi
        done
        if [ $NOT_KILLED = 1 ]
        then
            echo -e "Cannot kill process"
            exit 1
        fi
        echo -e "$APP_NAME already stopped"
        ;;
    start)
        if [ "$PID" != "" ] && check_if_process_is_running
        then
            echo -e "$APP_NAME already running"
            exit 1
        fi
        # 后台服务方式启动程序
        nohup $JAVA -jar $JAVA_AGENT $JAVA_OPTS $APP_JAR --spring.config.location=$APPLICATION_FILE > /dev/null 2>&1 &
        echo $JAVA -jar $JAVA_AGENT $JAVA_OPTS $APP_JAR --spring.config.location=$APPLICATION_FILE
        echo -e "$APP_NAME started, you can use ./service.sh loginfo see logs"
        ;;
    restart)
        $0 stop
        if [ $? = 1 ]
        then
            exit 1
        fi
        $0 start
        ;;
    loginfo)
        tail -f $APP_LOGS/info.log
        exit 1
        ;;
    logerror)
        tail -f $APP_LOGS/error.log
        exit 1
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|loginfo|logerror}"
        exit 1
esac

exit 0