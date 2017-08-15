## 1-deploy-all

In this tutorial, we will deploy pods and services for all 3 microservices of the seeker example app.

We will create a new namespace called `seeker`

```bash
@ kubectl create namespace "seeker"
```

Deploy all pods and services.
```bash
$ kubectl apply -f auth-app.yml -n seeker
$ kubectl apply -f web-app.yml -n seeker
$ kubectl apply -f so-app.yml -n seeker
```

Check our deployments.
```bash
$ kubectl get deployments -n seeker
$ kubectl get services -n seeker
$ kubectl get pods -n seeker
```