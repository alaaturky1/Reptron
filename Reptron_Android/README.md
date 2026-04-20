# Supplement Store - Android App

A native Android application built with Kotlin and Jetpack Compose, converted from the iOS Swift/SwiftUI app.

## Project Structure

```
android-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/supplementstore/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── models/
│   │   │   │   ├── viewmodels/
│   │   │   │   ├── navigation/
│   │   │   │   ├── services/
│   │   │   │   └── ui/
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   ├── xml/
│   │   │   │   └── mipmap/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── .gitignore
```

## Setup Instructions

1. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android-app` folder

2. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

3. **Configure API Base URL**
   - Update `APIService.kt` with your actual API base URL
   - Currently set to `http://localhost:3000` (update for production)

4. **Add App Icons**
   - Replace placeholder icons in `res/mipmap/` folders
   - Generate icons using Android Studio's Image Asset Studio

5. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10

## Dependencies

The project uses:
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Design system
- **Navigation Compose** - Navigation
- **ViewModel** - State management
- **Coroutines** - Asynchronous programming
- **Coil** - Image loading
- **Retrofit** - HTTP client (configured but needs implementation)
- **Kotlinx Serialization** - JSON parsing

## API Configuration

Update the base URL in `APIService.kt`:
```kotlin
private val baseURL = "https://your-api-url.com" // Update this
```

## Features

- ✅ Complete UI conversion from iOS
- ✅ Navigation with route protection
- ✅ Shopping cart functionality
- ✅ User authentication
- ✅ Product browsing and details
- ✅ Checkout and purchase history
- ✅ Coaches and equipment listings
- ✅ Dark theme with cyan accent

## Next Steps

1. Implement actual API calls in `AuthService.kt` and `APIService.kt`
2. Add Dependency Injection (Hilt recommended)
3. Replace image placeholders with actual image loading
4. Complete NavHost with all routes
5. Add error handling and loading states
6. Write unit tests for ViewModels
7. Add UI tests

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 24+ (Android 7.0+)
- Gradle 8.2+

## License

Same as the original iOS project.
