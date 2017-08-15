## 2-scaling-cluster

In this tutorial, we are scaling app the authentication application. By increasing the replicas to 4 from 2, we request kubernetes api controller to schedule 2 more copies of auth-pod in the cluster.

The service is unimpacted during the whole process. On successful pod start, they are picked up by the `auth-service` . Now this service will load balance traffic to 4 pods.


```bash
$ kubectl apply -f auth-app.yml -n seeker
```