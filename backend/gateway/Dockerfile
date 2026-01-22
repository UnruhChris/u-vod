# STAGE 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 1. Copiamo il Parent POM (Ora visibile grazie al context ./backend)
COPY pom.xml .
# Lo installiamo localmente per risolvere le dipendenze
RUN mvn -B -N install

# 2. Copiamo il servizio specifico (Path relativi alla cartella backend)
# MODIFICA 'gateway' CON IL NOME DELLA CARTELLA DEL SERVIZIO!
COPY gateway/pom.xml ./gateway/
COPY gateway/src ./gateway/src

# 3. Compiliamo il servizio puntando al suo pom
RUN mvn -f gateway/pom.xml package -DskipTests

# STAGE 2: Runtime
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

RUN groupadd -r uvod && useradd -r -g uvod uvod
USER uvod

# 4. Copiamo il JAR generato
# MODIFICA 'gateway' CON IL NOME DELLA CARTELLA DEL SERVIZIO!
COPY --from=builder /app/gateway/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]