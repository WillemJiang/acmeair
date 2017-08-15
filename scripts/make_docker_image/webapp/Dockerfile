FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/tank/

COPY ./acmeair-webapp-exec.jar  /opt/tank


CMD ["java", "-jar","-Dspring.profiles.active=sc","/opt/tank/acmeair-webapp-exec.jar"]
