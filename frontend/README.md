# Subscription Manager Frontend

A modern desktop application built with Kotlin and Jetpack Compose for managing subscriptions. This frontend application provides a beautiful and intuitive user interface for interacting with the Subscription Manager backend API.

## Features

- **Subscription Management**: View, add, edit, and delete subscriptions
- **Real-time Statistics**: View monthly and yearly spending totals
- **Modern UI**: Beautiful Material Design 3 interface
- **Form Validation**: Comprehensive input validation with user feedback
- **Error Handling**: Graceful error handling and user notifications
- **Responsive Design**: Adapts to different window sizes

## Screens

### 1. Subscription List
- Displays all subscriptions in a card-based layout
- Shows subscription details including name, amount, frequency, and status
- Action buttons for edit, delete, and toggle active status
- Empty state with helpful messaging

### 2. Add/Edit Subscription Form
- Form for creating new subscriptions or editing existing ones
- Real-time form validation
- Preview of monthly and yearly costs
- Support for both monthly and yearly billing frequencies

### 3. Statistics Dashboard
- Monthly and yearly spending totals
- Financial insights (daily, weekly, monthly costs)
- Helpful tips for subscription management

## Technology Stack

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for desktop applications
- **Ktor Client**: HTTP client for API communication
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Coroutines**: Asynchronous programming
- **Material Design 3**: Design system and components

## Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- Backend application running on port 8081 (default)

## Building and Running

### 1. Build the Application

```bash
# From the frontend directory
./gradlew build
```

### 2. Run the Application

```bash
# Run the application
./gradlew run
```

### 3. Create Distribution

```bash
# Create native distribution (executable)
./gradlew createDistributable
```

The distribution will be created in `build/compose/binaries/` directory.

## Configuration

### Backend URL Configuration

The frontend connects to the backend API by default at `http://localhost:8081/api/subscriptions`. To change this:

1. Modify the `baseUrl` parameter in `SubscriptionApiClient.kt`
2. Or set an environment variable (requires code modification)

### Logging Configuration

Logging is configured using Kotlin Logging and Logback. The default configuration outputs logs to the console.

## API Integration

The frontend communicates with the backend through the following endpoints:

- `GET /api/subscriptions` - Get all subscriptions
- `GET /api/subscriptions/active` - Get active subscriptions
- `GET /api/subscriptions/{id}` - Get subscription by ID
- `GET /api/subscriptions/search?name={name}` - Search subscriptions
- `GET /api/subscriptions/by-frequency/{frequency}` - Get subscriptions by frequency
- `GET /api/subscriptions/totals` - Get subscription totals
- `POST /api/subscriptions` - Create new subscription
- `PUT /api/subscriptions/{id}` - Update subscription
- `PATCH /api/subscriptions/{id}/toggle-active` - Toggle subscription active status
- `DELETE /api/subscriptions/{id}` - Delete subscription

## Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests FrontendApplicationTest
```

### Test Coverage

The test suite includes:

- API client connection tests
- CRUD operation tests
- Error handling tests
- Data validation tests

## Project Structure

```
frontend/
├── src/
│   ├── main/
│   │   └── kotlin/
│   │       └── com/
│   │           └── example/
│   │               └── frontend/
│   │                   ├── Main.kt                    # Application entry point
│   │                   ├── api/
│   │                   │   └── SubscriptionApiClient.kt # API client
│   │                   ├── model/
│   │                   │   └── Subscription.kt        # Data models
│   │                   ├── ui/
│   │                   │   ├── SubscriptionManagerApp.kt    # Main UI app
│   │                   │   ├── SubscriptionListScreen.kt    # Subscription list
│   │                   │   ├── SubscriptionFormScreen.kt    # Add/edit form
│   │                   │   └── StatisticsScreen.kt          # Statistics dashboard
│   │                   └── util/
│   │                       └── Serializers.kt         # Custom serializers
│   └── test/
│       └── kotlin/
│           └── com/
│               └── example/
│                   └── frontend/
│                       └── FrontendApplicationTest.kt # Test suite
├── build.gradle.kts                                   # Build configuration
└── README.md                                         # This file
```

## Development

### Adding New Features

1. Create new UI components in the `ui` package
2. Add corresponding API methods in `SubscriptionApiClient.kt`
3. Update the main app navigation in `SubscriptionManagerApp.kt`
4. Add tests for new functionality

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add documentation for public APIs
- Write unit tests for new functionality

## Troubleshooting

### Common Issues

1. **Connection Refused**: Ensure the backend application is running on port 8081
2. **Build Failures**: Check that Java 17+ is installed and JAVA_HOME is set correctly
3. **UI Not Rendering**: Verify that the backend API is responding correctly

### Debug Mode

To enable debug logging, modify the logback configuration or add debug statements in the code.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is part of the Subscription Manager application. See the main project README for license information. 