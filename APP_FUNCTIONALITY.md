# NetAlert AI - App Functionality

## Core Features

The NetAlert AI Android app has the following key features:

### 1. Real-time Network Monitoring
- Continuously monitors device's current network type (5G, 4G, 3G, etc.)
- Uses Android's ConnectivityManager.NetworkCallback API for efficient monitoring
- Runs as a foreground service to ensure continuous operation

### 2. Instant Notifications
- Sends notification when network downgrades from 5G to 4G or lower
- Sends notification when device is currently using a 4G network
- Notifications are non-intrusive but informative

### 3. History Logging
- Maintains local history log with timestamps
- Stores old network type and new network type for each change
- Uses Room Database for efficient local storage

### 4. Dashboard UI
- Clean dashboard showing current network status
- Displays network change history in a scrollable list
- Shows network statistics (today's downgrades, weekly downgrades)

### 5. Data Export
- Option to export network logs to CSV format
- Exported files are saved to device storage

## How It Works

### Network Detection
The app uses Android's ConnectivityManager.NetworkCallback to monitor network changes in real-time. When a network change is detected, it analyzes the network capabilities to determine the network type:

- 5G: Detected through NET_CAPABILITY_NR_SA or NET_CAPABILITY_NR_SA_MMWAVE
- 4G: Detected through NET_CAPABILITY_LTE
- 3G: Detected through NET_CAPABILITY_IMS
- 2G: Detected through NETWORK_TYPE_GPRS or NETWORK_TYPE_EDGE

### Notification System
The app sends two types of notifications:

1. **Downgrade Notification**: When network downgrades from 5G to 4G or lower
   - Title: "Network Downgrade Detected"
   - Message: "Your network downgraded from 5G to 4G"

2. **4G Notification**: When device is currently using 4G network
   - Title: "Using 4G Network"
   - Message: "Your device is currently connected to a 4G network"
   - Only appears once per 4G session to avoid spam

### Background Service
The NetworkMonitorService runs as a foreground service to ensure continuous monitoring even when the app is not actively open. This service:

- Registers a NetworkCallback to listen for network changes
- Maintains a persistent notification to keep the service running
- Stores network changes in the Room database
- Sends notifications when appropriate

## Building the App

### Prerequisites
- Android Studio Flamingo (2022.2.1) or later
- Android SDK API Level 33 (Android 13) or later
- Java Development Kit (JDK) 11 or later

### Steps to Build
1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the NetAlertAI directory
4. Wait for Gradle to sync the project
5. Select "Build" → "Make Project" from the menu
6. Connect an Android device or start an emulator
7. Select "Run" → "Run 'app'" from the menu

## Key Components

### NetworkMonitorService
The core service that monitors network changes and sends notifications.

### MainActivity
The main user interface that displays current network status and history.

### Room Database
Local storage for network history using Android Room persistence library.

### Notification System
Handles all notifications sent to the user.

## Permissions Required
- ACCESS_NETWORK_STATE: To monitor network state changes
- POST_NOTIFICATIONS: To send notifications (Android 13+)
- FOREGROUND_SERVICE: To run the monitoring service
- FOREGROUND_SERVICE_SPECIAL_USE: Special use foreground service

## Instant Notification Feature
The app provides instant notifications when:
1. Network downgrades from 5G to 4G or lower
2. Device is currently using a 4G network

These notifications appear immediately when the network condition is detected, providing real-time feedback to the user.