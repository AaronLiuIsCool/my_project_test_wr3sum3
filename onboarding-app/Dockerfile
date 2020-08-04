FROM java:8-jdk-alpine

COPY ./target/onboarding-app-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch onboarding-app-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "onboarding-app-1.0.0.jar"]