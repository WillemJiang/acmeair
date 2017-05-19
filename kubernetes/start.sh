#!/bin/bash

kubectl label pods consul-0 "role=web"
kubectl create -f consul-web.yaml

kubectl create -f customer.yaml
kubectl create -f booking.yaml
kubectl create -f gateway.yaml

gcloud compute firewall-rules create allow-gateway --allow=tcp:31000
gcloud compute firewall-rules create allow-consul --allow=tcp:31005

gcloud compute instances list

kubectl autoscale deployment customer-service --cpu-percent=50 --min=1 --max=10
kubectl autoscale deployment booking-service --cpu-percent=50 --min=1 --max=10
kubectl autoscale deployment gateway-service --cpu-percent=50 --min=1 --max=3
