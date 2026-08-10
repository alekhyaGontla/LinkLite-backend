<<<<<<< HEAD
# Stage 1: Build stage
=======
# Build Stage
>>>>>>> 5ca3fb3 (Fix database configuration and update controllers)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

<<<<<<< HEAD
# 1. Copy pom.xml first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
=======
# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build executable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
>>>>>>> 5ca3fb3 (Fix database configuration and update controllers)
FROM eclipse-temurin:21-jre
WORKDIR /app
<<<<<<< HEAD

# Copy the built jar from stage 1
=======
>>>>>>> 5ca3fb3 (Fix database configuration and update controllers)
COPY --from=build /app/target/*.jar app.jar

# Render assigns a dynamic port via the PORT env variable
EXPOSE 8080
<<<<<<< HEAD

# Configure JVM flags for container resource constraints and optimized startup
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
=======
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> 5ca3fb3 (Fix database configuration and update controllers)
