# Training Application

This is a training application that supports both PostgreSQL and H2 databases.

## Running with H2 Database

The application can be run with an in-memory H2 database for development and testing purposes.

### Prerequisites

- Java 17 or higher
- Maven (or use the included Maven wrapper)

### Running the Application with H2

1. On Windows, run the following command:
   ```
   run-h2.bat
   ```

2. On Linux/Mac, run the following command:
   ```
   ./run-h2.sh
   ```

This will:
- Set the active profile to "h2"
- Build the application with Maven
- Start the application with the H2 database

### Verifying H2 Database

Once the application is running with the H2 profile:

1. The H2 console is available at: http://localhost:8080/h2-console
2. Use the following connection details:
   - JDBC URL: `jdbc:h2:mem:training;DB_CLOSE_DELAY=-1`
   - Username: `sa`
   - Password: (leave empty)
   - Click "Connect"

3. You can now browse the database tables and execute SQL queries.

### H2 Database Configuration

The H2 database is configured with the following settings:
- In-memory database (data is lost when the application is stopped)
- Hibernate automatically creates the schema (create-drop mode)
- H2 console is enabled for easy database inspection

## Running with PostgreSQL Database

For production use, the application can be run with a PostgreSQL database.

1. On Windows, run the following command:
   ```
   run-postgres.bat
   ```

2. On Linux/Mac, run the following command:
   ```
   ./run-postgres.sh
   ```

For more details on PostgreSQL configuration, refer to the application properties files.