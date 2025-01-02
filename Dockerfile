FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/employeemanagement-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]