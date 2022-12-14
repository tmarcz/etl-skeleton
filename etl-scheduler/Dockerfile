# Build
FROM maven:3-openjdk-11-slim AS builder

WORKDIR /build

COPY pom.xml pom.xml
COPY etl-commons/java-commons/src commons/src
COPY etl-commons/java-commons/pom.xml commons/pom.xml

COPY etl-scheduler/src scheduler/src
COPY etl-scheduler/pom.xml scheduler/pom.xml

RUN mvn clean install --projects commons,scheduler

# Execution
FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=builder /build/scheduler/target/etl-scheduler-*.jar /app/scheduler.jar

EXPOSE 9610

ENTRYPOINT ["java", "-cp", "scheduler.jar", "com.example.scheduler.SchedulerApplication"]
