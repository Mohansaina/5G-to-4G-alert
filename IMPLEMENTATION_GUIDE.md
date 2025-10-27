# NetAlert AI - Complete Implementation Guide

## Overview

NetAlert AI is an Android application that monitors mobile network changes and notifies users when their connection downgrades from 5G to 4G or lower. This document provides a complete guide to implementing all features of the application.

## Project Structure

The application follows the standard Android project structure:

```
NetAlertAI/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/netalert/ai/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── NetworkHistory.java
│   │   │   │   ├── NetworkHistoryAdapter.java
│   │   │   │   ├── NetworkChangeReceiver.java
│   │   │   │   └── NetworkMonitorService.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   └── build.gradle
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Core Components Implementation

### 1. Network Monitoring Service

The [NetworkMonitorService](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkMonitorService.java#L15-L167) is a foreground service that continuously monitors network changes using the modern ConnectivityManager.NetworkCallback API.

Key features:
- Runs in the foreground to ensure continuous operation
- Uses NetworkCallback for real-time network change detection
- Identifies network types (5G, 4G, 3G, 2G)
- Detects downgrade events and triggers notifications
- Maintains low battery usage through event-based monitoring

### 2. Network Change Receiver

The [NetworkChangeReceiver](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkChangeReceiver.java#L10-L56) provides an alternative approach using broadcast receivers for network change detection, which can be useful for older Android versions.

### 3. Data Model

The [NetworkHistory](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkHistory.java#L3-L40) class represents a network change event with:
- Timestamp of the event
- Previous network type
- New network type

### 4. UI Components

The [MainActivity](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/MainActivity.java#L5-L15) provides the main dashboard with:
- Current network status display
- Network statistics (daily/weekly downgrades)
- History of network changes
- Export functionality

The [NetworkHistoryAdapter](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkHistoryAdapter.java#L9-L52) manages the RecyclerView for displaying network change history.

## Implementation Steps

### Step 1: Set up Permissions

Add the following permissions to [AndroidManifest.xml](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/AndroidManifest.xml):

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
```

### Step 2: Register Components

Register the service and receiver in [AndroidManifest.xml](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/AndroidManifest.xml):

```xml
<service android:name=".NetworkMonitorService"
    android:foregroundServiceType="specialUse"
    android:exported="false">
    <property android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE" 
        android:value="network_monitoring" />
</service>

<receiver android:name=".NetworkChangeReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
    </intent-filter>
</receiver>
```

### Step 3: Implement Network Monitoring Service

The [NetworkMonitorService](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/NetworkMonitorService.java#L15-L167) should:

1. Create a notification channel for Android 8.0+
2. Start as a foreground service
3. Register a NetworkCallback to monitor network changes
4. Identify network types using NetworkCapabilities
5. Detect downgrade events (5G → 4G/3G/2G)
6. Send notifications using NotificationManager
7. Save events to local storage

### Step 4: Implement UI Components

The [MainActivity](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/src/main/java/com/netalert/ai/MainActivity.java#L5-L15) should:

1. Bind to the NetworkMonitorService
2. Display current network status
3. Show network change history using RecyclerView
4. Provide export functionality

### Step 5: Add Notification Support

Create notification channels and send notifications when downgrades are detected:

```java
private void sendDowngradeNotification(String oldNetwork, String newNetwork) {
    String title = getString(R.string.network_downgrade_notification_title);
    String text = getString(R.string.network_downgrade_notification_text, oldNetwork, newNetwork);
    
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    notificationManager.notify((int) System.currentTimeMillis(), builder.build());
}
```

## Advanced Features

### Firebase Integration

To add Firebase analytics and cloud logging:

1. Add Firebase SDK to [app/build.gradle](file:///c%3A/Users/svssw/Downloads/5g4galert/NetAlertAI/app/build.gradle):
```gradle
implementation 'com.google.firebase:firebase-analytics:21.2.0'
implementation 'com.google.firebase:firebase-firestore:24.4.1'
```

2. Initialize Firebase in Application class:
```java
FirebaseApp.initializeApp(this);
```

3. Log events to Firebase:
```java
Bundle bundle = new Bundle();
bundle.putString("old_network", oldNetwork);
bundle.putString("new_network", newNetwork);
FirebaseAnalytics.getInstance(context).logEvent("network_downgrade", bundle);
```

### Export Functionality

Implement CSV export for network history:

```java
private void exportToCSV(List<NetworkHistory> historyList) {
    // Create CSV content
    StringBuilder csvContent = new StringBuilder();
    csvContent.append("Timestamp,Old Network,New Network\n");
    
    for (NetworkHistory history : historyList) {
        csvContent.append(history.getTimestamp())
                  .append(",")
                  .append(history.getOldNetwork())
                  .append(",")
                  .append(history.getNewNetwork())
                  .append("\n");
    }
    
    // Save to file or share
    // Implementation depends on storage permissions and target Android version
}
```

## Testing

### Unit Tests

Create unit tests for network type detection:

```java
@Test
public void testNetworkTypeDetection() {
    // Test different NetworkCapabilities scenarios
    // Verify correct network type identification
}
```

### Instrumentation Tests

Create UI tests for the main activity:

```java
@Test
public void testMainActivityDisplay() {
    // Launch activity
    // Verify UI elements are displayed correctly
    // Check network status updates
}
```

## Optimization Tips

1. Use NetworkCallback instead of polling for better battery life
2. Implement efficient data storage (Room database recommended)
3. Minimize UI updates when the app is in the background
4. Handle different Android versions appropriately
5. Request only necessary permissions

## Troubleshooting

Common issues and solutions:

1. **Notifications not appearing**: Check notification channel setup and permissions
2. **Network changes not detected**: Verify NetworkCallback registration and permissions
3. **Service not starting**: Check foreground service permissions and implementation
4. **Battery drain**: Optimize NetworkCallback filters to reduce events

## Future Enhancements

1. Add detailed statistics and charts
2. Implement geolocation tracking for network quality mapping
3. Add machine learning for predictive network quality analysis
4. Create a widget for quick status access
5. Add sharing capabilities for network quality reports