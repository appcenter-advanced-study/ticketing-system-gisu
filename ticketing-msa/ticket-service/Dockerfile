FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY ticket-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]