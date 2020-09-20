kubectl config use-context docker-desktop

kubectl delete deployments --all
kubectl delete services --all
kubectl delete configmaps --all

mvn clean install -DskipTests=true

export REACT_APP_ENV=development 
docker-compose build

kubectl apply -f k8s/test/local
kubectl apply -f k8s/test
