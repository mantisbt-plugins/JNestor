#!/bin/sh
#
# jnestor.sh - launch jnestor majordomo
#
#
#


CLASSPATH=./:./jnestor-4.0.jar:./lib/*:./scripts

java -Dspring.context=applicationContext.xml \
-classpath "$CLASSPATH" \
com.supermanhamuerto.nestor.Nestor $1





