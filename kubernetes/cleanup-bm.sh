#!/bin/bash

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
