#!/bin/sh
#
# publish - publish the contents of the website
#
#
#

rsync -avz --exclude=.* ../webContent/ jnestor@www.jnestor.supermanhamuerto.com:~/public_html/





