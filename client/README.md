# Subscription Manager Client

This is the Android client application for the Subscription Manager, built using Kotlin Multiplatform (KMP) with Compose Multiplatform.

## Features

- View all subscriptions in a clean, card-based interface
- Add new subscriptions with detailed information
- Edit existing subscriptions
- Delete subscriptions
- Toggle subscription active/inactive status
- View subscription statistics and financial insights
- Material Design 3 UI with dark/light theme support

## Architecture

- **Kotlin Multiplatform**: Shared code between platforms
- **Compose Multiplatform**: Modern declarative UI framework
- **Ktor Client**: HTTP client for API communication
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Coroutines**: Asynchronous programming

## Project Structure

```
client/
├── src/
│   ├── commonMain/kotlin/com/example/client/
│   │   ├── api/          # API client for backend communication
│   │   ├── model/        # Data models
│   │   ├── ui/           # Compose UI components
│   │   └── util/         # Utility classes
│   └── androidMain/
│       ├── kotlin/       # Android-specific code
│       └── res/          # Android resources
└── build.gradle.kts      # Build configuration
```

## Setup

1. Make sure you have Android Studio with the latest Android SDK
2. The client module is already included in the project
3. Build the project using Gradle

## Configuration

The client is configured to connect to the backend server at `http://10.0.2.2:3000` (Android emulator localhost). For physical devices, you may need to update the base URL in `SubscriptionApiClient.kt`.

## Building

To build the Android APK:

```bash
./gradlew :client:assembleDebug
```

To run on an emulator or device:

```bash
./gradlew :client:installDebug
```

## Dependencies

- **Compose Multiplatform**: UI framework
- **Ktor Client**: HTTP client
- **Kotlinx Serialization**: JSON serialization
- **Kotlinx Coroutines**: Asynchronous programming
- **Material Icons**: Icon library

## API Integration

The client communicates with the backend server using RESTful APIs:

- `GET /api/subscriptions` - Get all subscriptions
- `POST /api/subscriptions` - Create new subscription
- `PUT /api/subscriptions/{id}` - Update subscription
- `DELETE /api/subscriptions/{id}` - Delete subscription
- `PATCH /api/subscriptions/{id}/toggle-active` - Toggle active status
- `GET /api/subscriptions/totals` - Get subscription totals

## Permissions

The app requires the following permissions:
- `INTERNET` - For API communication
- `ACCESS_NETWORK_STATE` - For network state monitoring 