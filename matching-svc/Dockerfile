FROM java:8-jdk-alpine

COPY ./target/matching-svc-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch matching-svc-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "matching-svc-1.0.0.jar"]