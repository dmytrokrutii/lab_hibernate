#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

# Variables
CONTAINER_NAME="training-postgres-standalone"
DB_NAME="${POSTGRES_DB:-training}"
DB_USER="${POSTGRES_USER:-postgres}"
DB_PASSWORD="${POSTGRES_PASSWORD:-postgres}"
DB_PORT="${POSTGRES_PORT:-5432}"
POSTGRES_IMAGE="postgres:17.4"

# Check if container already exists
if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
  echo "Container $CONTAINER_NAME already exists. Stopping and removing..."
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

# Run PostgreSQL container
echo "Starting PostgreSQL container..."
docker run --name "$CONTAINER_NAME" \
  -e POSTGRES_DB="$DB_NAME" \
  -e POSTGRES_USER="$DB_USER" \
  -e POSTGRES_PASSWORD="$DB_PASSWORD" \
  -p $DB_PORT:5432 \
  -v postgres-data-standalone:/var/lib/postgresql/data \
  -d "$POSTGRES_IMAGE"

# Check if container started successfully
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
  echo "PostgreSQL container started successfully."
  echo "Database: $DB_NAME"
  echo "Username: $DB_USER"
  echo "Password: $DB_PASSWORD"
  echo "Port: $DB_PORT"

  echo "Waiting for PostgreSQL to be ready..."
  sleep 5

  echo "Running Flyway migrations..."
  mvn flyway:migrate -DskipFlyway=false
else
  echo "Failed to start PostgreSQL container."
  exit 1
fi
