#!/bin/bash

kubectl create -f acmeair-bm-mongo-pv.yaml
kubectl create -f acmeair-bm-mongo-pvc.yaml

kubectl expose -f acmeair-bm-mongo-service.yaml
kubectl create -f acmeair-bm-mongo-deployment.yaml

kubectl expose -f service-center-bm-service.yaml
kubectl create -f service-center-bm-deployment.yaml

kubectl create -f acmeair-bm-customerservice-service.yaml
kubectl create -f acmeair-bm-customerservice-deployment.yaml

kubectl create -f acmeair-bm-bookingservice-service.yaml
kubectl create -f acmeair-bm-bookingservice-deployment.yaml

kubectl create -f acmeair-bm-webapp-service.yaml
kubectl create -f acmeair-bm-webapp-deployment.yaml

