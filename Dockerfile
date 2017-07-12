FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/tank/

COPY ./acmeair-customer-service/target/acmeair/acmeair-customer-service-2.0.0-SNAPSHOT-exec.jar  /opt/tank


CMD ["java", "-jar","-Dspring.profiles.active=cse","-Dspring.data.mongodb.database=acmeair","-Dspring.data.mongodb.host=mongodb","-Dspring.data.mongodb.port=32701","/opt/tank/acmeair-customer-servie-2.0.0-SNAPSHOT-exec.jar"]
