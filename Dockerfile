# Use an official OpenJDK runtime as a parent image
FROM FROM eclipse-temurin:8-jre

# Set the working directory in the container
WORKDIR /app

# Create a directory for the externalized configuration file
VOLUME /app/config

# Create a directory for logs
VOLUME /app/logs

# Argument to specify the JAR file name (useful if it changes)
ARG JAR_FILE=target/team_oa-0.0.2-SNAPSHOT.jar

# Copy the packaged JAR file into the container
COPY ${JAR_FILE} app.jar

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Run the JAR file when the container launches
# Also, configure Spring Boot to look for the configuration file in the mounted volume
# and set the log path to the mounted logs volume.
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=file:/app/config/application.yaml", "--logging.file.path=/app/logs"]
