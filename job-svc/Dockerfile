FROM java:8-jdk-alpine

COPY ./target/job-svc-1.0.0.jar /usr/app/

COPY ./src/main/resources/matching/keywords.xlsx /usr/app/matching/keywords.xlsx

WORKDIR /usr/app

RUN sh -c 'touch job-svc-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "job-svc-1.0.0.jar"]