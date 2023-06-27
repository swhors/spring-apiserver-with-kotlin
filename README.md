# spring-kotlin-server

--------------------------------------

## 1. Compile / Run / Packaging / Dockerizing

### 1.1 Complie
```
   mvn compile
   mvn package
```

### 1.2 Run
+ Dev
```
   java -jar spring-kotlin-server-[version].jar --spring.profiles.active=dev 
```
+ Prod
```
   java -jar spring-kotlin-server-[version].jar --spring.profiles.active=dev 
```

### 1.3 Docker
```
    docker build -t spring-kotlin-server:[version] .
```

## 2. Automatic CI/CD Using Kubernetes
### 2.1 GitHub Setting
 - Create application account in azure active directory
 - Set ACR_ACCESS_PWD and ACR_ACCESS_ID using above information
![alt text](docs/image/image001.png "github setting")

### 2.2 ArgoCD Setting
 #### 2.2.1 Create Application Project
 - Create Porject
![alt text](docs/image/image002-1.png "argocd step 1-0")
 - Set project properties to access kubernetes url.

 #### 2.2.2 Create GitHub access Information
![alt text](docs/image/image003-1.png "argocd step 2-0")
![alt text](docs/image/image003-2.png "argocd step 2-1")
 #### 2.2.3 Create Project
![alt_text](docs/image/image004-1.png "argocd step 3-0")
![alt text](docs/image/image004-2.png "argocd step 3-1")
 #### 2.2.4 Finally Completed
![alt text](docs/image/image005-1.png "argocd step 4-0")

### 2.3 Kubernetes Check
```agsl
aip-0164@aip-0164ui-MacBookPro ~ % kubectl get all -n api
NAME                                       READY   STATUS    RESTARTS   AGE
pod/cm-acme-http-solver-jbcvf              1/1     Running   0          30m
pod/spring-kotlin-server-d9888c57b-qhrnh   1/1     Running   0          3m20s

NAME                                TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
service/cm-acme-http-solver-5krmg   NodePort   10.0.79.85     <none>        8089:30951/TCP   30m
service/spring-kotlin-server        NodePort   10.0.190.133   <none>        8080:31068/TCP   30m

NAME                                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/spring-kotlin-server   1/1     1            1           30m

NAME                                              DESIRED   CURRENT   READY   AGE
replicaset.apps/spring-kotlin-server-68f5957b46   0         0         0       30m
replicaset.apps/spring-kotlin-server-d9888c57b    1         1         1       3m21s
aip-0164@aip-0164ui-MacBookPro ~ %
```