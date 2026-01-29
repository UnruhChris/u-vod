FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B -N install

COPY common/pom.xml ./common/
COPY common/src ./common/src
RUN mvn -f common/pom.xml install -DskipTests

COPY streaming/pom.xml ./streaming/
COPY streaming/src ./streaming/src
RUN mvn -f streaming/pom.xml package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

RUN groupadd -r uvod && useradd -r -g uvod uvod
USER uvod

COPY --from=builder /app/streaming/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]