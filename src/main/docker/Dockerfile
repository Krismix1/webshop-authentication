FROM tomcat:8.0-jre8
MAINTAINER Cristian Betivu cbetivu@gmail.com
RUN apt update -y && apt upgrade -y
RUN rm -rf /usr/local/tomcat/webapps
RUN mkdir /usr/local/tomcat/webapps
ADD @project.build.finalName@.war /usr/local/tomcat/webapps/ROOT.war
RUN rm -f /usr/local/tomcat/conf/server.xml
ADD server.xml /usr/local/tomcat/conf/server.xml
ENV SPRING_PROFILES_ACTIVE="@activeProfiles@"