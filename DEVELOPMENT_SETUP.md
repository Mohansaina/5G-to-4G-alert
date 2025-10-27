# NetAlert AI - Development Setup Guide

This guide will help you set up your development environment for NetAlert AI.

## Prerequisites

1. **Android Studio**
   - Download from: https://developer.android.com/studio
   - Install with default settings

2. **Android SDK**
   - Android Studio will install this automatically
   - Required SDK: API Level 33 (Android 13)

3. **JDK 11 or later**
   - Included with Android Studio
   - Or download from: https://adoptium.net/

## Setup Steps

1. **Clone or Download the Project**
   ```
   git clone <repository-url>
   ```
   Or download the ZIP file and extract it

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the NetAlertAI directory
   - Select the project folder

3. **Initial Build**
   - Android Studio will automatically sync Gradle files
   - Wait for the sync to complete
   - If prompted, install any missing SDK components

4. **Run the Application**
   - Connect an Android device via USB
   - Or create an emulator through AVD Manager
   - Click the "Run" button (green play icon)
   - Select your target device

## Project Structure

```
NetAlertAI/
├── app/                 # Main application module
│   ├── src/            # Source code
│   │   └── main/       # Main source set
│   │       ├── java/   # Java source files
│   │       └── res/    # Resource files
│   └── build.gradle    # Module build configuration
├── build.gradle        # Project build configuration
└── settings.gradle     # Project settings
```

## Key Components

1. **NetworkMonitorService**
   - Foreground service for network monitoring
   - Uses ConnectivityManager.NetworkCallback

2. **Room Database**
   - Local storage for network history
   - Entities, DAOs, and Database classes

3. **MainActivity**
   - Main dashboard UI
   - Displays current network status and history

## Development Tips

1. **Debugging**
   - Use Logcat to view application logs
   - Filter by "NetAlert" tag for relevant messages

2. **Testing**
   - Unit tests in `src/test/`
   - Instrumentation tests in `src/androidTest/`

3. **Adding Features**
   - Follow the existing code structure
   - Use MVVM pattern where applicable
   - Add documentation for new features

## Troubleshooting

1. **Gradle Sync Issues**
   - Check internet connection
   - Verify SDK paths in local.properties
   - Try "File" > "Sync Project with Gradle Files"

2. **Build Failures**
   - Clean project: "Build" > "Clean Project"
   - Rebuild project: "Build" > "Rebuild Project"

3. **Runtime Issues**
   - Check Logcat for error messages
   - Verify permissions in AndroidManifest.xml
   - Ensure device meets minimum requirements

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a pull request

For major changes, please open an issue first to discuss what you would like to change.