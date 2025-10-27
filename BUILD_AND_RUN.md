# NetAlert AI - Build and Run Guide

## Prerequisites

Before building and running NetAlert AI, ensure you have the following installed:

1. Android Studio Flamingo (2022.2.1) or later
2. Android SDK API Level 33 (Android 13) or later
3. Java Development Kit (JDK) 11 or later
4. An Android device or emulator running Android 5.0 (API Level 21) or later

## Project Setup

1. Clone or download the NetAlert AI project
2. Open Android Studio
3. Select "Open an existing Android Studio project"
4. Navigate to the NetAlertAI directory and select it

## Project Structure

```
NetAlertAI/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/netalert/ai/
│   │   │   │   ├── database/              # Room database components
│   │   │   │   ├── utils/                 # Utility classes
│   │   │   │   ├── MainActivity.java      # Main activity
│   │   │   │   ├── NetworkHistory.java    # Data model
│   │   │   │   ├── NetworkHistoryAdapter.java # RecyclerView adapter
│   │   │   │   ├── NetworkChangeReceiver.java # Network change receiver
│   │   │   │   └── NetworkMonitorService.java # Network monitoring service
│   │   │   ├── res/
│   │   │   │   ├── layout/                # Layout files
│   │   │   │   ├── values/                # Resource files
│   │   │   │   └── drawable/              # Drawable resources
│   │   │   └── AndroidManifest.xml        # Application manifest
│   │   └── build.gradle                   # App module build configuration
│   └── build.gradle                       # Project level build configuration
├── gradle/
│   └── wrapper/                           # Gradle wrapper files
├── build.gradle                           # Top-level build configuration
├── settings.gradle                        # Project settings
├── gradle.properties                      # Gradle properties
└── README.md                              # Project documentation
```

## Building the Project

1. After opening the project in Android Studio, wait for Gradle to sync
2. If prompted, install any missing SDK components
3. Build the project by selecting "Build" > "Make Project" from the menu

## Running the Application

1. Connect an Android device via USB or start an emulator
2. Select "Run" > "Run 'app'" from the menu
3. Choose your target device when prompted

## Key Features

### Network Monitoring

The application uses a foreground service ([NetworkMonitorService](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkMonitorService.java#L15-L167)) to continuously monitor network changes:

- Uses ConnectivityManager.NetworkCallback for real-time monitoring
- Identifies network types (5G, 4G, 3G, 2G)
- Detects downgrade events (5G → 4G/3G/2G)
- Sends instant notifications when downgrades occur

### Data Storage

Network history is stored locally using Room database:

- [NetworkHistoryEntity](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/database/NetworkHistoryEntity.java#L7-L61) - Database entity for network events
- [NetworkHistoryDao](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/database/NetworkHistoryDao.java#L8-L21) - Data access object
- [NetworkHistoryDatabase](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/database/NetworkHistoryDatabase.java#L9-L26) - Database class
- [NetworkHistoryRepository](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/database/NetworkHistoryRepository.java#L9-L39) - Repository pattern implementation

### User Interface

The main dashboard ([MainActivity](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/MainActivity.java#L12-L42)) displays:

- Current network status
- Network change history in a RecyclerView
- Export functionality for network logs

### Export Functionality

Network history can be exported to CSV format using [ExportUtils](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/utils/ExportUtils.java#L9-L33):

- Exports all network history to a CSV file
- Saves files to the app's external storage directory

## Permissions

The application requires the following permissions:

- `ACCESS_NETWORK_STATE` - To monitor network state changes
- `POST_NOTIFICATIONS` - To send notifications (Android 13+)
- `FOREGROUND_SERVICE` - To run the monitoring service
- `WRITE_EXTERNAL_STORAGE` - To export logs (Android 9 and below)

## Testing

### Unit Tests

Run unit tests using:
```
./gradlew test
```

### Instrumentation Tests

Run instrumentation tests on a connected device/emulator:
```
./gradlew connectedAndroidTest
```

## Troubleshooting

### Common Issues

1. **Gradle sync fails**: Ensure you have the correct Android SDK installed
2. **Build fails**: Check that all dependencies are properly resolved
3. **App crashes on startup**: Verify all permissions are granted
4. **Network changes not detected**: Check that the service is running properly

### Debugging

1. Check Logcat for error messages
2. Verify that all required permissions are granted
3. Ensure the NetworkMonitorService is running
4. Confirm that the database is properly initialized

## Customization

### UI Customization

- Modify layout files in [res/layout/](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/res/layout/)
- Update colors in [res/values/colors.xml](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/res/values/colors.xml)
- Change themes in [res/values/styles.xml](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/res/values/styles.xml)

### Functionality Extension

- Add Firebase integration for cloud logging
- Implement geolocation tracking
- Add machine learning for predictive analysis
- Create a widget for quick status access

## Deployment

To generate a release build:

1. Create a signed APK or App Bundle
2. Select "Build" > "Generate Signed Bundle / APK"
3. Follow the wizard to create a release build
4. Upload to Google Play Store or distribute manually

## Support

For issues or feature requests, please create an issue on the project repository.