# Usar JDK como base
FROM openjdk:17-jdk-slim

# Directorio de la app
WORKDIR /app

# Copiar el JAR al contenedor
COPY target/pqrs-*.jar app.jar

# Exponer el puerto si lo necesitas (opcional)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
