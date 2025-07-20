# Stage 1: Build the app
FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk-alpine

COPY --from=builder /app/target/*.jar app.jar

ARG WEATHER_API_KEY
ENV weather.api.key=${WEATHER_API_KEY}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]