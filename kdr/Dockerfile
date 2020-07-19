FROM java:8-jdk-alpine

COPY ./target/kdr-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch kdr-1.0.0.jar'

ENTRYPOINT ["java", "-jar", "kdr-1.0.0.jar"]
