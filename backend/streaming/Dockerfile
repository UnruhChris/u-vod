# STAGE 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 1. Copiamo il Parent POM (Ora visibile grazie al context ./backend)
COPY pom.xml .
# Lo installiamo localmente per risolvere le dipendenze
RUN mvn -B -N install

# 2. Copiamo il servizio specifico (Path relativi alla cartella backend)
# MODIFICA 'streaming' CON IL NOME DELLA CARTELLA DEL SERVIZIO!
COPY streaming/pom.xml ./streaming/
COPY streaming/src ./streaming/src

# 3. Compiliamo il servizio puntando al suo pom
RUN mvn -f streaming/pom.xml package -DskipTests

# STAGE 2: Runtime
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
WORKDIR /app

RUN groupadd -r uvod && useradd -r -g uvod uvod
USER uvod

# 4. Copiamo il JAR generato
# MODIFICA 'streaming' CON IL NOME DELLA CARTELLA DEL SERVIZIO!
COPY --from=builder /app/streaming/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]