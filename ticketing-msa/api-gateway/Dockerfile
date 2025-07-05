# 1단계: Gradle 빌드 단계
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# 종속성 캐시 최적화를 위해 필요한 파일만 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle build --no-daemon || return 0

# 전체 프로젝트 복사 후 최종 빌드
COPY . .
RUN gradle clean build -x test --no-daemon

# 2단계: 실제 실행 이미지 (경량화)
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Gateway는 보통 8080 포트 사용 (필요시 변경)
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]