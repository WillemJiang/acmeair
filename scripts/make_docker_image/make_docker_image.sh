CUR_PATH=$(cd "$(dirname "$0")"; pwd)
ROOT_PATH="${CUR_PATH}/../../"
cd "${ROOT_PATH}"
mvn clean  install -DskipTests  -PHuaweiCloud
cd "${CUR_PATH}"
cp ./customer/Dockerfile  ${ROOT_PATH}/acmeair-customer-service/target/acmeair/
cp ./booking/Dockerfile  ${ROOT_PATH}/acmeair-booking-service/target/acmeair/
cp ./webapp/Dockerfile   ${ROOT_PATH}/acmeair-webapp/target/
cd ${ROOT_PATH}/acmeair-customer-service/target/acmeair/
docker build -t acmeair-customer-service:3.0.0-SNAPSHOT  . 
cd  ${ROOT_PATH}/acmeair/acmeair-booking-service/target/acmeair/
docker build -t acmeair-booking-service:3.0.0-SNAPSHOT .
cd ${ROOT_PATH}/acmeair/acmeair-webapp/target/
docker build -t acmeair-webapp:3.0.0-SNAPSHOT  .
