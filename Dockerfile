FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your project to the container
COPY target/*.jar app.jar

# Expose the port your application listens on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "SpringAOP-0.0.1-SNAPSHOT.jar"]
