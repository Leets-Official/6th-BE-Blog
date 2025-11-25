# 기존 openjdk:17-jdk 대신 아래 amazoncorretto:17을 사용합니다.
FROM amazoncorretto:17

# JAR 파일 위치 지정
ARG JAR_FILE=build/libs/*.jar

# JAR 파일을 컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]