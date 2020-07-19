
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
### Step 4. Start a Pull Request Review.
a. Github create a how to pull request in Github UI https://help.github.com/en/desktop/contributing-to-projects/creating-a-pull-request
Target to upsteam remote. In this case is https://github.com/AaronLiuIsCool/kuaidao-svc.git develop branch.
b. Add specific reviewers.
c. Once pull request approved, solve potential conflicts and merge it.

Thanks for your contributions.

More details please go to ask Aaron Liu.

## Spring health check
Health check
http://localhost:8081/actuator if you run spring boot without docker compose.

Health check
http://localhost:8333/actuator if you run docker-compose up.

## Docker cli
Run Dockers as below
```docker-compose up```
Check docker container services
```docker-compose ps```
Clean up
```docker-compose down```

## K8S
Run test local (as account-svc example) initial your services yaml
1. Go to k8s/test folder
Run ```kubectl apply -f config```
2. Go to k8s/ folder
Run ```kubectl apply -f test```
3. Go to localhost:30001

Run uat (It's on AWS EKS CA central region)
TBD
Run Prod
TBD

## Web UI
1. `cd webapp`
2. `yarn`
3. For develop build use `yarn start`

Please see [guidelines for webapp](webapp/README.md) for more details
