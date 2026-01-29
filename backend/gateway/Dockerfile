FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B -N install

COPY gateway/pom.xml ./gateway/
COPY gateway/src ./gateway/src
RUN mvn -f gateway/pom.xml package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

RUN groupadd -r uvod && useradd -r -g uvod uvod
USER uvod

COPY --from=builder /app/gateway/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]