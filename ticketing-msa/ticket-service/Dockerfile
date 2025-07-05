# 1단계: Gradle 빌드 단계
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# 필요한 파일만 먼저 복사 (종속성 캐시 활용)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle build --no-daemon || return 0

# 전체 프로젝트 복사 후 빌드
COPY src/main/java/com/ticketing_msa/ticket_service .
RUN gradle clean build -x test --no-daemon

# 2단계: 실제 실행용 경량 이미지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]