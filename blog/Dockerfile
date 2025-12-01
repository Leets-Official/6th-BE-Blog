FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ENV SERVER_PORT=8080

COPY blog/build/libs/blog-0.0.1-SNAPSHOT.jar app.jar

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]