#!/bin/bash

# Set H2 profile
export SPRING_PROFILES_ACTIVE=h2

# Check if Maven wrapper exists
if [ ! -f "./mvnw" ]; then
  echo "Maven wrapper not found. Creating it..."
  mvn -N io.takari:maven:wrapper
fi

echo "Building and running application with H2 profile..."
./mvnw clean package -DskipTests

echo "Starting application..."
java -jar target/training-application-1.0-SNAPSHOT-jar-with-dependencies.jar

# Note: This script runs the application in the foreground.
# Press Ctrl+C to stop the application.
