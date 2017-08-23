#!/bin/bash

# Copyright 2017 Huawei Technologies Co., Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

kubectl delete hpa acmeair-bm-webapp
kubectl delete hpa acmeair-bm-bookingservice
kubectl delete hpa acmeair-bm-customerservice
kubectl delete hpa service-center-bm
kubectl delete hpa acmeair-bm-mongo


kubectl delete -f acmeair-bm-webapp-service.yaml
kubectl delete -f acmeair-bm-webapp-deployment.yaml

kubectl delete -f acmeair-bm-bookingservice-deployment.yaml
kubectl delete -f acmeair-bm-bookingservice-service.yaml

kubectl delete -f acmeair-bm-customerservice-deployment.yaml
kubectl delete -f acmeair-bm-customerservice-service.yaml

kubectl delete -f service-center-bm-deployment.yaml
kubectl delete -f service-center-bm-service.yaml

kubectl delete -f acmeair-bm-mongo-deployment.yaml
kubectl delete -f acmeair-bm-mongo-service.yaml

kubectl delete -f acmeair-bm-mongo-pvc.yaml
kubectl delete -f acmeair-bm-mongo-pv.yaml
