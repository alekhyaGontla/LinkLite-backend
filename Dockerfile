# Stage 1: Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# 1. Copy pom.xml first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from stage 1
COPY --from=build /app/target/*.jar app.jar

# Render assigns a dynamic port via the PORT env variable
EXPOSE 8080

# Configure JVM flags for container resource constraints and optimized startup
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
