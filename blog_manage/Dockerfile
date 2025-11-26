# Stage 1: 빌드 단계
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 캐시를 활용하기 위해 build.gradle과 settings.gradle 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (캐시 활용)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN gradle build -x test --no-daemon

# Stage 2: 실행 단계
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출 (Spring Boot 기본 포트)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]

