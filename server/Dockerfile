FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

ENV TZ=Asia/Seoul

ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]