# Subscription Manager Application

A Spring Boot application for managing subscriptions, with support for both PostgreSQL and H2 databases.

## Current Status

- PostgreSQL is not installed or not running on the system
- The application is configured to use PostgreSQL in production mode
- A development profile with H2 database is available for development and testing

## Features

- Add, update, and delete subscriptions
- Track monthly and yearly subscriptions
- Calculate total monthly and yearly costs
- REST API for integration with other systems

## Running the Application

### With H2 Database (Development Mode)

For development and testing, you can use the H2 in-memory database:

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

This will start the application with the H2 database, which doesn't require any additional setup.

### With PostgreSQL (Production Mode)

For production use, the application is configured to use PostgreSQL:

1. Install and set up PostgreSQL by following the instructions in [POSTGRESQL_SETUP.md](POSTGRESQL_SETUP.md)
2. Run the application:
   ```
   ./gradlew bootRun
   ```

## Checking PostgreSQL Connection

To verify if PostgreSQL is installed and accessible:

```
./gradlew checkPostgreSQL
```

This will attempt to connect to PostgreSQL and report any issues.

## API Endpoints

- `GET /api/subscriptions`: Get all subscriptions
- `GET /api/subscriptions/active`: Get active subscriptions
- `GET /api/subscriptions/{id}`: Get a subscription by ID
- `GET /api/subscriptions/search?name=...`: Search subscriptions by name
- `GET /api/subscriptions/by-frequency/{frequency}`: Get subscriptions by frequency (MONTHLY or YEARLY)
- `GET /api/subscriptions/totals`: Get total monthly and yearly costs
- `POST /api/subscriptions`: Create a new subscription
- `PUT /api/subscriptions/{id}`: Update a subscription
- `PATCH /api/subscriptions/{id}/toggle-active`: Toggle a subscription's active status
- `DELETE /api/subscriptions/{id}`: Delete a subscription

## Development

### Running Tests

```
./gradlew test
```

### Building the Application

```
./gradlew build
```

## Dependencies

- Spring Boot 3.5.3
- Spring Data JPA
- PostgreSQL Driver
- H2 Database (for development)
- JUnit 5 (for testing)