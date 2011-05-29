#!/bin/bash

ACTION=$1
HOST=$2
START_ACTION="start"
STOP_ACTION="stop"

PID_FILE="core.pid"

set SUCCESS_STATUS=SUCCESS

export APP_JAR=mon-hub-app-0.1-SNAPSHOT.jar

function start {
  java -Djava.rmi.server.hostname=$HOST -jar lib/${APP_JAR} > /dev/null 2>&1 &
  STATUS=$?
  PID=$!
  echo $PID > $PID_FILE
  JAVA_STARTED=`ps aux | grep "$APP_JAR" | wc -l`
  if [ $JAVA_STARTED -eq 2 ]; then
        echo "SUCCESS"
  else
        echo "FAIL"
  fi
}


function stop {
  echo  "Stopping Monitoring Hub"
  kill `cat $PID_FILE`
}


DIRECTORY=$(cd `dirname $0` && pwd)
cd ${DIRECTORY}


if [ "$ACTION" = "$STOP_ACTION" ] ; then
  stop
elif [ "$ACTION" = "$START_ACTION" ] ; then
  start
else
  echo "Usage: $0 $START_ACTION | $STOP_ACTION"
  exit 1
fi


