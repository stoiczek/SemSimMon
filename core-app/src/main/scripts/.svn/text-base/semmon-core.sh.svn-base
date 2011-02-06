#!/bin/bash

ACTION=$1
START_ACTION="start"
STOP_ACTION="stop"

PID_FILE="core.pid"

function start {
  echo "Starting semmon core service"
  java -cp resources/: -jar lib/core-app-0.1-SNAPSHOT.jar > /dev/null 2>&1 &
  STATUS=$?
  PID=$!
  echo $PID > $PID_FILE
  echo $STATUS
}

function stop {
  echo  "Stopping semmon core service"
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


