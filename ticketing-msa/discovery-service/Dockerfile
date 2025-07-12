FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY discovery-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]