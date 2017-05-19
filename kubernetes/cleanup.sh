#!/bin/bash

gcloud compute firewall-rules delete allow-gateway allow-consul

kubectl delete hpa booking-service customer-service gateway-service
kubectl delete deployments customer-service booking-service gateway-service
kubectl delete svc consul-web gateway

