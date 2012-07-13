#!/bin/sh
#
# jnestor.sh - launch jnestor majordomo
#
#
#


CLASSPATH=./:./jnestor.jar:./lib:./scripts

java -Dspring.context=applicationContext.xml \
-classpath "$CLASSPATH" \
com.supermanhamuerto.nestor.Nestor 




