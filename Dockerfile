FROM eclipse-temurin:21-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ARG WEATHER_API_KEY
ENV weather.api.key=${WEATHER_API_KEY}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]