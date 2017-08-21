# Run Acmeair on Kubernetes
These scripts show you how to run acmeair on kubernetes.

## Before you begin
* A bare metal kubernetes cluster
* Bigger than 1Gi size of /tmp dir with at least on of nodes in the cluster 
* Scripts are tested with kubernetes 1.7+ and docker 17.06.0-ce

## Start using
```
git clone https://github.com/WillemJiang/acmeair.git
cd acmeair/kubernetes/
bash start-bm.sh
```
## Check result

### Check result inside the cluster
* Get CLUSTER-IP and TARGET-PORT with api gateway
```
kubectl get svc |grep acmeair-webapp
```

* Visit website
```
curl http://<CLUSTER-IP>:<TARGET-PORT>/index.html
```

### Check result outside the cluster
* Get EXTERNAL-IP and NODE-PORT with api gateway
```
kubectl get svc acmeair-webapp -o yaml | grep ExternalIP -C 1
kubectl get svc acmeair-webapp -o yaml | grep nodePort -C 1
```

* Visit website
```
curl http://<EXTERNAL-IP>:<NODE-PORT>/index.html
```

***Notes:***
*If no free external-ip can be reserved by kubernetes, try to visit with the public-ip with the node.*
```
# get NODE-NAME of the node which api gateway is runing on
  kubectl get po -owide |grep acmeair-webapp
# get PUBLIC-IP which of the node
  kubectl describe  node <NODE-NAME> |grep public-ip
# visit website
  curl http://<PUBLIC-IP>:<NODE-PORT>/index.html
```

## Clean up
```
bash cleanup-bm.sh
```

