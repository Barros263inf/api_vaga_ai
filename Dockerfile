# 1. Usa uma imagem base com Java 17
FROM eclipse-temurin:17-jdk-alpine

# 2. Define o diretório de trabalho dentro do container
WORKDIR /app

# 3. Copia o arquivo .jar gerado pelo Maven para dentro do container
# Nota: O nome do jar pode variar, ajuste conforme o seu target/
COPY target/gs-0.0.1-SNAPSHOT.jar app.jar

# 4. Comando para rodar a aplicação quando o container subir
ENTRYPOINT ["java", "-jar", "app.jar"]