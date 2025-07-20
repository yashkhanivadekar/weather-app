# Use official Java base image
FROM eclipse-temurin:21-jdk-alpine

# Set environment variable for API key
ARG WEATHER_API_KEY
ENV weather.api.key=${WEATHER_API_KEY}

# Copy the built JAR file
COPY target/forecast-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "/app.jar"]