# Etapa de construção: Maven com JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e baixar as dependências
COPY pom.xml . 
RUN mvn dependency:go-offline

# Copiar o código-fonte e compilar o JAR
COPY src /app/src
RUN mvn clean package -DskipTests

# Etapa de execução: Eclipse Temurin com JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da etapa de construção
COPY --from=build /app/target/zippydelivery-0.0.1-SNAPSHOT.jar /app/zippydelivery.jar

# Copiar o arquivo .env para a imagem (garantindo que ele seja usado pela aplicação)
COPY .env /app/.env

# Expor a porta 8080
EXPOSE 8080

# Iniciar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "/app/zippydelivery.jar"]
