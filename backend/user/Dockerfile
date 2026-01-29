FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -B -N install
COPY common/pom.xml ./common/
COPY common/src ./common/src
RUN mvn -f common/pom.xml install -DskipTests
COPY user/pom.xml ./user/
COPY user/src ./user/src
RUN mvn -f user/pom.xml package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app


RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/user/target/*.jar app.jar

COPY user/entrypoint.sh ./entrypoint.sh
RUN chmod +x ./entrypoint.sh


EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]  