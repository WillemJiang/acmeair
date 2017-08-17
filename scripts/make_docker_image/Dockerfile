FROM openjdk:8-jre-alpine

RUN mkdir -p /maven/acmeair/

COPY ./*-exec.jar  /maven/acmeair/

CMD ["java","$JAVA_OPTS", "-jar","/maven/acmeair/$JAR_NAME-exec.jar"]
