#!/usr/bin/env bash

mkdir -p /root/.embedmongo/linux/
cp /maven/mongodb/* /root/.embedmongo/linux/

java -Dspring.data.mongodb.host=localhost -Dserver.port=8082 -jar /maven/acmeair/acmeair-customer-service-exec.jar
