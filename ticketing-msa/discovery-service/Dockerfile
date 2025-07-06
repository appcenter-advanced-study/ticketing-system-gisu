# 1단계: Gradle 빌드
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# 2단계: 실행 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]