# 1. Java 17 JDK 이미지 기반
FROM eclipse-temurin:17-jdk-alpine as builder

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. Gradle 빌드에 필요한 파일 복사
COPY build/libs/discovery-service-0.0.1-SNAPSHOT.jar app.jar

# 4. 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]