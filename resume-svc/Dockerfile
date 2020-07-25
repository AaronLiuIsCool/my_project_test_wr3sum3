FROM java:8-jdk-alpine

COPY ./target/resume-svc-0.0.1.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch resume-svc-0.0.1.jar'

ENTRYPOINT ["java", "-jar", "resume-svc-0.0.1.jar"]
