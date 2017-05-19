## Auto-Scaling on Kubernetes
steps to do stress testing on google compute engine:
* add a container cluster in google container engine with 3 nodes and auto-scaling on
![container cluster](https://servicecomb.atlassian.net/secure/attachment/10001/gce.PNG)
* start consul cluster
```
git clone git@github.com:seanyinx/consul-on-kubernetes.git
cd consul-on-kubernetes
bash start.sh
```
* start mongodb cluster
```
git clone git@github.com:seanyinx/mongo-k8s-sidecar.git
cd mongo-k8s-sidecar/examples
bash start.sh
```
* start acmeair
```
git clone git@github.com:WillemJiang/acmeair.git
cd acmeair
git checkout docker_image_upload
cd kubernetes 
bash start.sh
```
* run stress test with JMeter with test plan at https://github.com/seanyinx/acmeair-driver

* any external ip can be used to access gateway (port 31000) and consul web ui (port 31005)
```
seanyinx@clean-sequencer-163514:~/acmeair/deployments$ gcloud compute instances list
NAME                         ZONE            MACHINE_TYPE   PREEMPTIBLE  INTERNAL_IP  EXTERNAL_IP     STATUS
gke-k0-pool-1-1915feca-0706  europe-west1-d  n1-standard-1               10.132.0.5   35.187.80.62    RUNNING
gke-k0-pool-1-1915feca-l4pw  europe-west1-d  n1-standard-1               10.132.0.4   35.189.203.200  RUNNING
gke-k0-pool-1-1915feca-xqwl  europe-west1-d  n1-standard-1               10.132.0.3   35.187.112.87   RUNNING
gke-k0-pool-1-1915feca-zb8l  europe-west1-d  n1-standard-1               10.132.0.2   35.187.117.70   RUNNING
```
* to see cpu load and replicas
```
seanyinx@clean-sequencer-163514:~/acmeair/deployments$ kubectl get hpa
NAME               REFERENCE                     TARGETS     MINPODS   MAXPODS   REPLICAS   AGE
booking-service    Deployment/booking-service    24% / 50%   1         10        1          29m
customer-service   Deployment/customer-service   60% / 50%   1         10        2          29m
gateway-service    Deployment/gateway-service    99% / 50%   1         3         2          28m
```
* to get pod list
```
seanyinx@clean-sequencer-163514:~/acmeair/deployments$ kubectl get pods
NAME                                READY     STATUS    RESTARTS   AGE
booking-service-2973951487-nb34s    1/1       Running   0          25m
consul-0                            1/1       Running   0          1h
consul-1                            1/1       Running   0          1h
consul-2                            1/1       Running   0          1h
customer-service-4071286682-jxkrp   1/1       Running   0          25m
gateway-service-4200126056-9r72p    1/1       Running   0          25m
mongo-0                             2/2       Running   0          1h
mongo-1                             2/2       Running   0          1h
mongo-2                             2/2       Running   0          1h
```
* to see pod details
```
kubectl describe pod gateway-service-4200126056-9r72p
```
* to see pod logs
```
kubectl logs gateway-service-4200126056-9r72p
```

steps to cleanup
* cleanup acmeair
```
cd acmeair/kubernetes 
bash cleanup.sh
```
* cleanup mongodb
```
cd mongo-k8s-sidecar/examples
bash cleanup.sh
```
* cleanup consul
```
cd consul-on-kubernetes
bash cleanup
```

