# Build stage
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY lombok.config .
COPY src ./src
RUN mvn clean package assembly:single -Dmaven.test.skip=true

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chmod 777 /app/logs

ENV SPRING_PROFILES_ACTIVE=default
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
