rem
rem jnestor.bat
rem 


set JAVA_HOME=C:\Program Files\Java\jre7
set JAVA="%JAVA_HOME%\bin\java.exe"

set CLASSPATH=./;./jnestor.jar;^
lib/activation.jar;^
lib/ant.jar;^
lib/ant-launcher.jar;^
lib/ant-testutil.jar;^
lib/axis-ant.jar;^
lib/axis-REV609819.jar;^
lib/commons-discovery-0.2.jar;^
lib/commons-logging-1.0.4.jar;^
lib/commons-logging-1.1.1.jar;^
lib/commons-logging-adapters-1.1.1.jar;^
lib/commons-logging-api-1.1.1.jar;^
lib/jaxrpc.jar;^
lib/jmock-1.1.0.jar;^
lib/jsoup-1.6.1.jar;^
lib/junit.jar;^
lib/log4j-1.2.16.jar;^
lib/log4j-1.2.8.jar;^
lib/mail.jar;^
lib/mantisconnect-client-api-1.1.1.1.jar;^
lib/org.springframework.aop-3.0.2.RELEASE.jar;^
lib/org.springframework.asm-3.0.2.RELEASE.jar;^
lib/org.springframework.aspects-3.0.2.RELEASE.jar;^
lib/org.springframework.beans-3.0.2.RELEASE.jar;^
lib/org.springframework.context-3.0.2.RELEASE.jar;^
lib/org.springframework.context.support-3.0.2.RELEASE.jar;^
lib/org.springframework.core-3.0.2.RELEASE.jar;^
lib/org.springframework.expression-3.0.2.RELEASE.jar;^
lib/org.springframework.instrument-3.0.2.RELEASE.jar;^
lib/org.springframework.instrument.tomcat-3.0.2.RELEASE.jar;^
lib/org.springframework.jdbc-3.0.2.RELEASE.jar;^
lib/org.springframework.jms-3.0.2.RELEASE.jar;^
lib/org.springframework.orm-3.0.2.RELEASE.jar;^
lib/org.springframework.oxm-3.0.2.RELEASE.jar;^
lib/org.springframework.test-3.0.2.RELEASE.jar;^
lib/org.springframework.transaction-3.0.2.RELEASE.jar;^
lib/org.springframework.web-3.0.2.RELEASE.jar;^
lib/org.springframework.web.portlet-3.0.2.RELEASE.jar;^
lib/org.springframework.web.servlet-3.0.2.RELEASE.jar;^
lib/org.springframework.web.struts-3.0.2.RELEASE.jar;^
lib/saaj.jar;^
lib/wsdl4j-1.5.1.jar

%JAVA% -Dspring.context=applicationContext.xml ^
com.supermanhamuerto.nestor.Nestor 