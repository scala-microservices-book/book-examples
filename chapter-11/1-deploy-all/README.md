## 1-deploy-all

In this tutorial, we will deploy pods and services for all 3 microservices of the seeker example app.

We will create a new namespace called `sm-seeker`

```bash
@ kubectl create namespace "sm-seeker"
```

Deploy all pods and services.
```bash
$ cd auth-app/
$ kubectl apply -f 10-cmaps.yml -n sm-seeker
$ kubectl apply -f 10-secrets.yml -n sm-seeker
$ kubectl apply -f deployment.yml -n sm-seeker
$ kubectl apply -f service.yml -n sm-seeker

Repeat this for so-app and web-app.
```

Check our deployments.
```bash
$ kubectl get deployments -n seeker
$ kubectl get services -n seeker
$ kubectl get pods -n seeker
```

Now visit http://127.0.0.1:8001/api/v1/proxy/namespaces/sm-seeker/services/auth-service:2000/

You should see play framework error page.