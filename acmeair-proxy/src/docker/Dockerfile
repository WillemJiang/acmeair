FROM tomcat:8.5-jre8

RUN rm -r /usr/local/tomcat/webapps/ROOT

COPY maven/ /usr/local/tomcat/webapps/

EXPOSE 7001
EXPOSE 8080

CMD ["catalina.sh", "run"]