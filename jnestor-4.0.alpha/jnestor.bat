rem
rem jnestor.bat
rem 


set JAVA_HOME=C:\Program Files\Java\jre7
set JAVA="%JAVA_HOME%\bin\java.exe"

set CLASSPATH=./;./jnestor.jar;./lib/*;scripts/*

:loop

%JAVA% -cp "%CLASSPATH%" ^
-Dspring.context=applicationContext.xml ^
com.supermanhamuerto.nestor.Nestor

sleep 1600
goto loop

