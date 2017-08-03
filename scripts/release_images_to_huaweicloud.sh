#how to use(linux): 1. Set the following variables 2.  ./release_images_to_huaweicloud.sh
#huawei cloud website :  https://servicestage.hwclouds.com/

#config example
#TARGET_VERSION=0.0.1                                                    ---------huawei cloud images repository target version.
#ORIGIN_VERSION=2.0.0-SNAPSHOT                                           ---------local images version.
#TENANT_NAME=xxxxxxxxxxx                                                 ---------huawei cloud tenant name.
#REPO_ADDRESS=registry.cn-north-1.hwclouds.com                           ---------huawei cloud images repository address.
#USER_NAME=xxxxx                                                         ---------user name: login huawei cloud images repository.
#PW=xxxxxxx                                                              ---------paasword: login huawei cloud images repository.
#CUSTOMER_REPO_NAME=acmeair-customer                                     ---------customer repository name ,created by huawei cloud. 
#BOOKING_REPO_NAME=acmeair-booking                                       ---------booking repository name ,created by huawei cloud. 
#WEBSITE_REPO_NAME=acmeair-website                                       ---------website repository name ,created by huawei cloud. 
#MONGO_REPO_NAME=mongo                                                   ---------mongo repository name ,created by huawei cloud. 

which docker
if [ $? -ne 0 ];
then
echo "no docker, please install docker."
exit 1
fi

which mvn
if [ $? -ne 0 ];
then
echo "no maven, please install maven."
exit 1
fi

if [ -z $TARGET_VERSION ];
then
echo "TARGET_VERSION is empty,please set it"
exit 1
fi

if [ -z $ORIGIN_VERSION ];
then
echo "ORIGIN_VERSION is empty,please set it"
exit 1
fi

if [ -z $TENANT_NAME ];
then
echo "TENANT_NAME is empty,please set it"
exit 1
fi

if [ -z $REPO_ADDRESS ];
then
echo "REPO_ADDRESS is empty,please set it"
exit 1
fi

if [ -z $USER_NAME ];
then
echo "USER_NAME is empty,please set it"
exit 1
fi

if [ -z $PW ];
then
echo "PW is empty,please set it"
exit 1
fi

if [ -z $CUSTOMER_REPO_NAME ];
then
echo "CUSTOMER_REPO_NAME is empty,please set it"
exit 1
fi

if [ -z $BOOKING_REPO_NAME ];
then
echo "BOOKING_REPO_NAME is empty,please set it"
exit 1
fi

if [ -z $WEBSITE_REPO_NAME ];
then
echo "WEBSITE_REPO_NAME is empty,please set it"
exit 1
fi

if [ -z $MONGO_REPO_NAME ];
then
echo "MONGO_REPO_NAME is empty,please set it"
exit 1
fi


CUR_PATH=$(cd "$(dirname "$0")"; pwd)
ROOT_PATH="${CUR_PATH}/../"
cd "${ROOT_PATH}"
docker rmi -f $(docker images|grep acmeair-customer-service|grep $ORIGIN_VERSION |awk '{print $3}')
docker rmi -f $(docker images|grep acmeair-booking-service|grep $ORIGIN_VERSION |awk '{print $3}')
docker rmi -f $(docker images|grep acmeair-website|grep $ORIGIN_VERSION |awk '{print $3}')
mvn clean  install -DskipTests  -Phuaweicloud -Pdocker
docker pull mongo:3.4.6
docker tag  mongo:3.4.6  ${REPO_ADDRESS}/${TENANT_NAME}/${MONGO_REPO_NAME}:$TARGET_VERSION
docker tag acmeair-customer-service:$ORIGIN_VERSION  ${REPO_ADDRESS}/${TENANT_NAME}/${CUSTOMER_REPO_NAME}:$TARGET_VERSION
docker tag acmeair-booking-service:$ORIGIN_VERSION   ${REPO_ADDRESS}/${TENANT_NAME}/${BOOKING_REPO_NAME}:$TARGET_VERSION
docker tag acmeair-website:$ORIGIN_VERSION           ${REPO_ADDRESS}/${TENANT_NAME}/${WEBSITE_REPO_NAME}:$TARGET_VERSION
docker login -u ${USER_NAME} -p ${PW} ${REPO_ADDRESS}
docker push ${REPO_ADDRESS}/${TENANT_NAME}/${MONGO_REPO_NAME}:$TARGET_VERSION
docker push ${REPO_ADDRESS}/${TENANT_NAME}/${CUSTOMER_REPO_NAME}:$TARGET_VERSION
docker push ${REPO_ADDRESS}/${TENANT_NAME}/${BOOKING_REPO_NAME}:$TARGET_VERSION
docker push ${REPO_ADDRESS}/${TENANT_NAME}/${WEBSITE_REPO_NAME}:$TARGET_VERSION