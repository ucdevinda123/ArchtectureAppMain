# Modular SDK Architecture

This project demonstrates a modular SDK architecture for Android applications with separate HTTP, Session, and UI modules that can be easily integrated into other applications.

## Architecture Overview

The SDK is divided into three independent modules:

1. **sdk-http**: HTTP client module with Retrofit
2. **sdk-session**: User session management module with Room database
3. **sdk-ui**: UI module with Fragment and Compose support

## Module Dependencies

```
app (Main Application)
├── sdk-http (HTTP Client)
├── sdk-session (Session Management)
└── sdk-ui (UI Components)
    ├── sdk-http (for HTTP operations)
    └── sdk-session (for session management)
```

## Features

### HTTP Module (`sdk-http`)
- Retrofit-based HTTP client
- Generic API result handling with Flow support
- Configurable timeout and logging
- Common data models for API responses

**Key Classes:**
- `ApiClient`: Generic interface for HTTP operations
- `RetrofitApiClient`: Retrofit implementation
- `HttpModule`: Factory for creating HTTP clients
- `ApiResult`: Sealed class for handling API results

### Session Module (`sdk-session`)
- Room database for session persistence
- User session and profile management
- Flow-based state observation
- Token management and refresh

**Key Classes:**
- `SessionManager`: Interface for session operations
- `SessionManagerImpl`: Room-based implementation
- `SessionState`: Sealed class for session states
- `UserSession` & `UserProfile`: Data models

### UI Module (`sdk-ui`)
- Fragment wrapper for easy integration
- Jetpack Compose UI components
- Flow-based state management
- Configurable UI themes

**Key Classes:**
- `SdkFragment`: Fragment wrapper for SDK UI
- `SdkViewModel`: ViewModel with session integration
- `SdkComposeContent`: Main Compose UI
- `SdkModule`: Factory for creating SDK components

## Integration Guide

### 1. Add Dependencies

In your `build.gradle`:

```kotlin
dependencies {
    implementation project(':sdk-http')
    implementation project(':sdk-session')
    implementation project(':sdk-ui')
    
    // Required dependencies
    implementation libs.dagger.hilt.android
    kapt libs.dagger.hilt.compiler
}
```

### 2. Initialize Hilt

In your `Application` class:

```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### 3. Use SDK Fragment

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize session
        val profile = UserProfile(
            id = "user123",
            email = "user@example.com",
            name = "John Doe"
        )
        
        sessionManager.login(
            userId = "user123",
            accessToken = "access_token",
            refreshToken = "refresh_token",
            expiresAt = System.currentTimeMillis() + 86400000, // 24 hours
            profile = profile
        )
    }
}
```

### 4. Integrate SDK UI

#### Option A: Fragment Integration

```kotlin
val sdkFragment = SdkModule.createSdkFragment(
    config = SdkUiConfig(
        primaryColor = "#6200EE",
        enableLogging = true,
        showDebugInfo = true
    ),
    onResult = { result ->
        // Handle SDK result
        println("SDK Result: $result")
    }
)

supportFragmentManager.commit {
    replace(R.id.container, sdkFragment)
}
```

#### Option B: Compose Integration

```kotlin
@Composable
fun MyScreen() {
    val sdkViewModel: SdkViewModel = hiltViewModel()
    val uiState by sdkViewModel.uiState.collectAsStateWithLifecycle()
    
    SdkComposeContent(
        uiState = uiState,
        config = SdkUiConfig(),
        onEvent = { event ->
            when (event) {
                is SdkEvent.UserInputChanged -> {
                    sdkViewModel.updateUserInput(event.input)
                }
                is SdkEvent.SubmitPressed -> {
                    sdkViewModel.submitInput(event.input)
                }
                // Handle other events...
            }
        }
    )
}
```

## Flow Communication

The SDK uses Kotlin Flows for reactive communication between modules:

### Session State Flow
```kotlin
sessionManager.sessionState.collect { state ->
    when (state) {
        is SessionState.Authenticated -> {
            // User is logged in
        }
        is SessionState.Unauthenticated -> {
            // User needs to login
        }
        is SessionState.Error -> {
            // Handle error
        }
    }
}
```

### SDK Result Flow
```kotlin
sdkViewModel.resultFlow.collect { result ->
    when {
        result.success -> {
            // Handle success
            result.data?.let { data ->
                // Process data
            }
        }
        else -> {
            // Handle error
            result.error?.let { error ->
                // Show error message
            }
        }
    }
}
```

## Configuration

### HTTP Configuration
```kotlin
val httpConfig = HttpConfig(
    baseUrl = "https://api.example.com",
    timeoutSeconds = 30,
    enableLogging = true
)

val retrofit = HttpModule.createRetrofit(httpConfig)
```

### UI Configuration
```kotlin
val uiConfig = SdkUiConfig(
    primaryColor = "#6200EE",
    secondaryColor = "#03DAC6",
    enableLogging = true,
    showDebugInfo = false
)
```

## Sample Implementation

The main app demonstrates:
- Session initialization with demo user
- SDK UI integration with Compose
- Flow-based state management
- Error handling and loading states

## Benefits

1. **Modularity**: Each module can be used independently
2. **Testability**: Clear separation of concerns
3. **Reusability**: Easy to integrate into other projects
4. **Maintainability**: Well-defined interfaces and dependencies
5. **Scalability**: Easy to extend with new features

## Building

```bash
./gradlew build
```

## Testing

```bash
./gradlew test
```

## ProGuard Rules

Each module includes appropriate ProGuard rules to ensure proper obfuscation while maintaining SDK functionality.
