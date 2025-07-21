# ---- Stage 1: Build ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy only the JAR from the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Pass the API key securely at runtime
ENV WEATHER_API_KEY=unset

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]