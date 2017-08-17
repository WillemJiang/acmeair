CUR_PATH=$(cd "$(dirname "$0")"; pwd)
ROOT_PATH="${CUR_PATH}/../../"
cd "${ROOT_PATH}"
mvn clean package -DskipTests -DskipITs -PHuaweiCloud
cd "${CUR_PATH}"
cp ./Dockerfile  ${ROOT_PATH}/acmeair-customer-service/target/acmeair/
cp ./Dockerfile  ${ROOT_PATH}/acmeair-booking-service/target/acmeair/
cp ./Dockerfile   ${ROOT_PATH}/acmeair-webapp/target/acmeair/
cd ${ROOT_PATH}/acmeair-customer-service/target/acmeair/
docker build -t acmeair-customer-service:0.0.1  . 
cd  ${ROOT_PATH}/acmeair-booking-service/target/acmeair/
docker build -t acmeair-booking-service:0.0.1 .
cd ${ROOT_PATH}/acmeair-webapp/target/acmeair
docker build -t acmeair-webapp:0.0.1  .
