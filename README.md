

# KuaiDaoResume
Kuaidao resume source control Mono-repo.

## System Requirements
* Maven 3.5
* Java 8
* Git version
* Swagger 2.x
* Docker engine/cli v19.03.8 or up
* Kubernetes v1.16.5 or up
* Node v10.21.0 (lts/dubnium)
* Yarn v1.22.4 or above

## First time setup
We are using git fork-process development.
### Step 1 Fork.
Fork the repo under your namespace by click on the "Fork" button on right/top coner.
After forking, you will get url like ```https://github.com/AaronLiuIsCool/kuaidao-svc.git```
### Step 2 Clone.
Clone your namespace repo by below's commands (or using UI tool like source tree)
```
git clone https://github.com/<your_name_space>/kuaidao-svc.git
```
(Note: we should always user ssh rather than https clone for development time, however, proxy manangement is currently not under ets development control.)
### Step 3 Setup Remote.
Setup your origin to your account namespace repo, and upstream to the project namespace repo by below's commands. (or using UI tool)
a. Check your remote repo status
```
git remote -v
```
It should show your origin point to your account namesapce remote url. If it's not please remove it and re-add by step b.
b. Add your remote url.
```
git remote add upstream https://github.com/AaronLiuIsCool/kuaidao-svc.git
```
(Same to add your remote url for origin.)
```
git remote add origin https://github.com/<your_name_space>/kuaidao-svc.git
```
c.
After it done. You can check by using step a. It should show as below.
```
origin  https://github.com/<your_name_space>/kuaidao-svc.git (fetch)
origin  https://github.com/<your_name_space>/kuaidao-svc.git (push)
upstream   https://github.com/AaronLiuIsCool/kuaidao-svc.git (fetch)
upstream   https://github.com/AaronLiuIsCool/kuaidao-svc.git (push)
```
d. Checkout your develop branch on your local
```
git checkout develop
```
(If there is no develop branch please use -b)
e. Pull the latest version from remote upstream. (Not your remote origin.)
```
git pull upstream develop
```
Now, you are getting the latest sync with upstream develop.

## How to contribute to a robust git repo
Since we are using fork-process development.
### Step 1 Sync with Develop branch.
```
git checkout develop
```
e. Pull the latest version from remote upstream. (Not your remote origin.)
```
git pull upstream develop
```
### Step 2 Check a new feature/branch branch.
As an example TAIL-123 is your local feature/issue branch. TAIL-123 is a sample jira ticket number.
```
git checkout -b TAIL-123
```
(Normally I use jira task name as feature/issue branch name.)
### Step 3. Push to Your Origin.
As an example TAIL-123 is your local feature/issue branch.
```
git push origin TAIL-123
```

Note: `webapp` has prepush hooks enabled to run unit tests. Please make sure you run `yarn` in `webapp` to have the dependencies install. If you have not made any changes to `webapp`, please run `git push origin TAIL-123 --no-verify`

### Step 4. Start a Pull Request Review.
a. Github create a how to pull request in Github UI https://help.github.com/en/desktop/contributing-to-projects/creating-a-pull-request
Target to upsteam remote. In this case is https://github.com/AaronLiuIsCool/kuaidao-svc.git develop branch.
b. Add specific reviewers.
c. Once pull request approved, solve potential conflicts and merge it.

Thanks for your contributions.

More details please go to ask Aaron Liu.

## KDR-svc backend

To get the services running locally, you need to build the application first. Please run `mvn install`. Then follow the steps below

## Docker cli
Build all docker images locally
```docker-compose build```
Run Dockers as below
```docker-compose up```
Check docker container services running for all.
```docker-compose ps```
Clean up
```docker-compose down```

## Using SwitchsHost to point services to local ips account as below
```
127.0.0.1 account.kuaidaoresume-v2.local
127.0.0.1 kdr.kuaidaoresume-v2.local
```

## Spring health check
Gateway health check
```http://kdr.kuaidaoresume-v2.local/health```
Each services' itself swagger ui (account services as example.)
```account.kuaidaoresume-v2.local/swagger-ui.html```
For actuator
```account.kuaidaoresume-v2.local/actuator```

Also, you can run ```mvn spring-boot:run``` for each specific micro-service.

(Before you run k8s ensure your local .env and local config/application.yml had been setup)
## K8S
Run test local (as account-svc example) initial your services yaml
1. Go to k8s/test folder
Run ```kubectl apply -f config```
2. Go to k8s/ folder
Run ```kubectl apply -f test```
3. Go to localhost:30001

### 安装Kubernetes Dashboard
```https://github.com/kubernetes/dashboard```
```kubectl get pods —namespace=kube-system```
### 启动 kube proxy
```kubectl proxy```
### 生成访问令牌
```kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')```
### 访问 Dashboard
```http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/```

### 本地部署
查询kdr pod名:
```kubectl get pods```
### Port forwarding
```kubectl port-forward kdr-svc-deployment-8584d9c74d-v92wt 80:80```
sudo as you needed
### start switch host
rest of them are same as docker-compose

### Check local k8s status (either you can go to dashboard or using below's commend)
```kubectl get pods -o wide```
```kubectl get services```
```kubectl get deployments```

### Clean up
```
kubectl delete deployments --all
kubectl delete services --all
kubectl delete configmaps --all
```
All in one
```
kubectl delete deployments --all && kubectl delete services --all && kubectl delete configmaps --all
```

## Run uat (It's on AWS EKS CA central region)
1. Go to k8s/uat folder
Run ```kubectl apply -f config```
2. Go to k8s/ folder
Run ```kubectl apply -f uat```

2.5 check deployed services
Run ```kubectl get services```
2.6 check pods status
Run ```kubectl get pods -o wide```
2.7 check deployment status
Run ```kubectl get deployments```

NOTE: Each time apply UAT k8s, please double check your k8s/uat/config folder's config.yaml.
If you have questions, please ask in Slack group[https://kuaidaoresume.slack.com/archives/C01EQ32RSG5] before apply -f config.

## Run Prod On (AWS West 2)
1. So far (Jan 19, 2020) UAT action pipeline is not isolated with PROD environment. A few steps need to manually run as local below.
a. Check the release branch from master.
b. Run ```mvn clean install -DskipTests=true```.
c. Run ```export REACT_APP_ENV=production``` for web frontend env update to prod.
d. Run ```docker-compose build && docker-compose push``` build docker image and push as usual (same as UAT)
e. Rests are similiar as UAT env (either delete and apply, or re-apply all k8s/prod)

## Flyway
Flyway is integrated with Spring Boot. To run migrations, follow these steps
1. Add flyway dependency to the sub-module pom
2. Create folder src/main/resources/db/migration
3. Add sql migration script with V{migration_version}__{brief description}.sql to the folder
4. Make sure spring.jpa.hibernate.ddl-auto is set to either "validate" or "none"
5. Start the application

To run migration from maven cli:
`mvn flyway:migrate`

## Web UI

#### One time setup

Add `127.0.0.1 app.kuaidaoresume-v2.local` to `/etc/hosts` or use smarthost

#### Run app locally
1. `cd webapp`
2. `yarn`
3. For develop build use `yarn start`

Note:
- run yarn start will try start the application on port 80 which if you have docker or k8s running for backend, you will not be able to get port 80 unless you change the backend or webapp to use a different port
- You might need to run `sudo` to run on port 80

#### Run app via Docker
- Follow the docker steps above, it will automatically create an image based on the code on your local
- To update with changes, you do not need to restart all containers. Just need to run `docker-compose up --force-recreate --build -d app-service`

#### Bypass login
Login is enabled, to bypass that, please go to `/404` first and run `localStorage.setItem('kdr-login-bypass',true);` in your browser console.

Please see [guidelines for webapp](webapp/README.md) for more details

#### Connect to backend services on UAT.
- Modify your `/etc/hosts` file or use https://github.com/oldj/SwitchHosts and add in
```shell script
127.0.0.1 app.uat.smartresume.careers
```
- Under `webapp/` add in `.env` file with content
```shell script
HTTPS=false
PORT=80 # You will need admin or sudo to allow port 80, or use default 3000
HOST='app.uat.smartresume.careers'
```
- Run `sudo REACT_APP_ENV=UAT yarn start`
- Go to `http://app.uat.smartresume.careers/` or `http://app.uat.smartresume.careers:3000/` if your port is 3000

## AWS Production environments
#### Create EKS cluster:
1. `cd k8s/prod/config`

1. create EKS cluster using cluster-us-west-2.yaml
   ```
   eksctl create cluster -f cluster-us-west-2.yaml
   ```

Note: This is an one time process and has been done in AWS us-west-2 region.

#### Create EKS and RDS security groups:
1. assign eks cluster VPC id to local var
   ```shell
   VPCID=$(aws eks describe-cluster --name kdr-prod \
   --region us-west-2 \
   --query "cluster.resourcesVpcConfig.vpcId" \
   --output text)
   ```

1. create the RDS access security group
   ```shell
   RDSSG=$(aws ec2 create-security-group --group-name RDSAccessSG \
   --description "Security group to apply to apps that need access to RDS" \
   --vpc-id $VPCID \
   --query "GroupId" \
   --output text)
   ```

1. add outbound rule to the RDS access security group
   ```shell
   aws ec2 authorize-security-group-egress --group-id $RDSSG \
   --protocol tcp \
   --cidr 0.0.0.0/0
   ```

1. follow the [instructions](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_SettingUp.html#CHAP_SettingUp.SecurityGroup) to provide access to your DB instance in VPC by creating another security group.

1. assign eks cluster security group id to local var
   ```shell
   CLUSTERSG=$(aws eks describe-cluster --name kdr-prod \
   --query "cluster.resourcesVpcConfig.clusterSecurityGroupId" \
   --output text)
   ```

1. enable pod ENIs
   ```
   kubectl set env daemonset -n kube-system aws-node ENABLE_POD_ENI=true
   ```

1. disable TCP early demux
   ```
   kubectl edit daemonset aws-node -n kube-system
   ```
   Under the initContainer section, change the value for DISABLE_TCP_EARLY_DEMUX from `false` to `true`.

1. follow the [instructions](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.IAMPolicy.html) to create an IAM policy for IAM database access

1. create service account config
   1.
      ```
      cd k8s/prod/config
      ```

   1.
      ```
      eksctl create iamserviceaccount --config-file=serviceaccount-us-west-2.yaml
      ```

1. apply SecurityGroupPolicy to eks cluster
   1. assign eks cluster security group id
      ```shell
      CLUSTERSG=$(aws eks describe-cluster --name kdr-prod \
      --query "cluster.resourcesVpcConfig.clusterSecurityGroupId" \
      --output text)
      ```

   1. print security group ids and add to security-group-policy-us-west-2.yaml
      ```shell
      echo $CLUSTERSG $RDSSG
      ```

   1.
      ```
      kubectl apply -f security-group-policy-us-west-2.yaml
      ```

Note: This is an one time process and has been done in AWS us-west-2 region.
