# --- 1. Build Stage ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# --- 2. Run Stage ---
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# This is the crucial part:
# Render sets a $PORT environment variable. This command
# tells Spring Boot to use that port instead of the default 8080.
CMD ["java", "-jar", "-Dserver.port=${PORT}", "app.jar"]