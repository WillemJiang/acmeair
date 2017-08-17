#!/bin/bash
# huawei service stage website: https://servicestage.hwclouds.com/
# How to use(Linux):
# 1. Uncomment the variables and set their values
# 2. Execute: bash publish_images_to_huaweicloud.sh

# config example
# TARGET_VERSION=0.1.0                                                  # ---------huawei cloud images repository target version.
# TENANT_NAME=xxxxxxxxxxx                                               # ---------huawei cloud tenant name.
# REPO_ADDRESS=registry.cn-north-1.hwclouds.com                         # ---------huawei cloud images repository address.
# USER_NAME=xxxxx                                                       # ---------username: login huawei cloud images repository.
# PW=xxxxxxx                                                            # ---------password: login huawei cloud images repository.

CUSTOMER_NAME=acmeair-customer-service						                      # ---------customer name, created by maven docker plugin.
BOOKING_NAME=acmeair-booking-service						                        # ---------booking name, created by maven docker plugin.
WEBAPP_NAME=acmeair-webapp						                                  # ---------webapp name, created by maven docker plugin.


which docker
if [ $? -ne 0 ]; then
    echo "no docker, please install docker 1.11.2."
    exit 1
fi

which mvn
if [ $? -ne 0 ]; then
    echo "no maven, please install maven."
    exit 1
fi

function isPropertySet () {
    if [ -z $2 ]; then
        echo "$1 is empty, please set it first"
        exit 1
    fi
}

properties=(TARGET_VERSION TENANT_NAME REPO_ADDRESS USER_NAME PW
            CUSTOMER_NAME BOOKING_NAME WEBAPP_NAME)
for property in ${properties[@]}; do
    isPropertySet $property ${!property}
done

CUR_PATH=$(cd "$(dirname $0)/"; pwd)
ROOT_PATH=$(cd "${CUR_PATH}/../"; pwd)
cd "${ROOT_PATH}"
ORIGIN_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep Building | awk '{print $4}')

modules=($CUSTOMER_NAME $BOOKING_NAME $WEBAPP_NAME)
echo "Removing old docker images"
for module in ${modules[@]}; do
    image_id=$(docker images| grep $module| grep $ORIGIN_VERSION| awk '{print $3}')
    if [ ! -z $image_id ]; then
       docker rmi -f $image_id
    fi
done

echo "Generating new docker images"

mvn clean package -DskipTests -DskipITs -PHuaweiCloud -Pdocker

echo "Tagging image versions"
for module in ${modules[@]}; do
    docker tag $module:$ORIGIN_VERSION ${REPO_ADDRESS}/${TENANT_NAME}/$module:$TARGET_VERSION
done

mongo_exists=$(docker images| grep "${REPO_ADDRESS}/${TENANT_NAME}/mongo"| awk '{print $2}'| grep "3.4.6")
if [ -z $mongo_exists ]; then
    docker pull mongo:3.4.6
    docker tag mongo:3.4.6 ${REPO_ADDRESS}/${TENANT_NAME}/mongo:3.4.6
fi

docker login -u ${USER_NAME} -p ${PW} ${REPO_ADDRESS}

echo "Pushing images to huawei docker repository"
for module in ${modules[@]}; do
    docker push ${REPO_ADDRESS}/${TENANT_NAME}/$module:$TARGET_VERSION
done
docker push ${REPO_ADDRESS}/${TENANT_NAME}/mongo:3.4.6

echo "Done"
