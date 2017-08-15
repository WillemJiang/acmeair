FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/tank/

COPY ./acmeair-booking-service-exec.jar  /opt/tank

CMD ["java", "-jar","-Dspring.profiles.active=mongodb","-Dspring.data.mongodb.database=acmeair","-Dspring.data.mongodb.host=mongodb","-Dspring.data.mongodb.port=32717","/opt/tank/acmeair-booking-service-exec.jar"]
