#!/bin/bash
#
# Usage: start.sh [debug]
#
if test -n "${JAVA_HOME}"; then
  if test -z "${JAVA_EXE}"; then
    JAVA_EXE=$JAVA_HOME/bin/java
  fi
fi

if test -z "${JAVA_EXE}"; then
  JAVA_EXE=java
fi

${JAVA_EXE} -version >/dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "ERROR: Not Found java installed!"
  exit 1
fi


cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf
LOGS_DIR=`grep -v '^\s*#' conf/log4j.properties | sed '/^config.log.basedir/!d;s/.*=//' | tr -d '\r'`
if [ ! -d $LOGS_DIR ]; then
    mkdir -p $LOGS_DIR
fi
if [ ! -d $LOGS_DIR ]; then
  echo "ERROR: Please check LOGS_DIR=$LOGS_DIR is ok?"
  exit 1
fi
STDOUT_FILE=$LOGS_DIR/console.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

java  -classpath $CONF_DIR:$LIB_JARS  com.pwc.ehm.job.EhmJobLauncher > $STDOUT_FILE 2>&1 &

echo 'OK'

PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"
