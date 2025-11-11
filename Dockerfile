# --- 1. Build Stage ---
# Use an official Maven image to build the app
FROM maven:3.8-openjdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the app
COPY src ./src
RUN mvn clean package -DskipTests

# --- 2. Run Stage ---
# Use a lightweight, official Java image to run the app
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# This is the crucial part:
# Render sets a $PORT environment variable. This command
# tells Spring Boot to use that port instead of the default 8080.
CMD ["java", "-jar", "-Dserver.port=${PORT}", "app.jar"]