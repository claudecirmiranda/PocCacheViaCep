# Utilize a imagem base do OpenJDK 11
FROM gru.ocir.io/grboiw7hzvhq/openjdk:11.0.6-jre-slim

# Instale o Redis
RUN apt-get update && apt-get install -y redis-server

# Copie o arquivo JAR da aplicação para o diretório do container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Comando de entrada para iniciar a aplicação Java
ENTRYPOINT ["java","-jar","/app.jar"]
