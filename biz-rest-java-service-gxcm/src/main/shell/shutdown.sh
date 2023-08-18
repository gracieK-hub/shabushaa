#!/bin/bash

JAVA_OPTS=""
# 获取shell脚本所在的绝对路径
SOURCE="$0"
while [ -h "$SOURCE"  ]; do
    DIR="$( cd -P "$( dirname "$SOURCE"  )" && pwd  )"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /*  ]] && SOURCE="$DIR/$SOURCE"
done

APP_BIN="$(cd -P "$( dirname "$SOURCE"  )" && pwd)"
APP_HOME="$(dirname $APP_BIN)"
APP_LOGS="$APP_HOME/logs"
APP_JAR=`find $APP_HOME/*.jar`
JAR_KEYWORDS="$APP_JAR"
# 设置程序名称
APP_NAME=$APP_JAR
# 查找服务进程号
PID=$(ps aux | grep ${JAR_KEYWORDS} | grep -v grep | awk '{print $2}' )

function check_if_process_is_running 
{
    if [ "$PID" = "" ]; then
        return 1
    fi
    ps -p $PID | grep "java"
    return $?
}

if ! check_if_process_is_running
then
    echo -e "\033[32m $APP_NAME already stopped \033[0m"
    exit 0
fi
kill $PID
echo -e "\033[32m Waiting for process to stop \033[0m"
NOT_KILLED=1
for i in {1..20}; do
    if check_if_process_is_running
    then
        echo -ne "\033[32m . \033[0m"
        sleep 1
    else
        NOT_KILLED=0
    fi
done
if [ $NOT_KILLED = 1 ]
then
    echo -e "\033[32m Cannot kill process \033[0m"
    exit 1
fi
echo -e "\033[32m $APP_NAME already stopped \033[0m"

exit 0