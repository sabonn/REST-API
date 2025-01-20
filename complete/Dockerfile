# Use an official Gradle image for the build stage
FROM gradle:8.3-jdk17 AS build
WORKDIR /app
COPY . .
# Build the application
RUN gradle clean bootJar

# Use a lightweight JDK image for the final stage
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar
# Expose port 8080
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
