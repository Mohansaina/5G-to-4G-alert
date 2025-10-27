package com.netalert.ai;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.netalert.ai.database.NetworkHistoryEntity;
import com.netalert.ai.database.NetworkHistoryDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NetworkMonitorService extends Service {
    private static final String TAG = "NetworkMonitorService";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "NetworkMonitorChannel";
    private static final int DOWNGRADE_NOTIFICATION_ID = 2;
    
    private ConnectivityManager.NetworkCallback networkCallback;
    private String currentNetworkType = "Unknown";
    private String previousNetworkType = "Unknown";
    private NotificationManager notificationManager;
    private NetworkHistoryDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        database = NetworkHistoryDatabase.getDatabase(this);
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createForegroundNotification());
        registerNetworkCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Restart service if killed
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        NetworkMonitorService getService() {
            return NetworkMonitorService.this;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Network Monitor";
            String description = "Monitors network changes";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createForegroundNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("NetAlert AI")
                .setContentText("Monitoring network changes")
                .setSmallIcon(R.drawable.ic_notification)
                .build();
    }

    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d(TAG, "Network available: " + network.toString());
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d(TAG, "Network lost: " + network.toString());
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                String newNetworkType = getNetworkType(networkCapabilities);
                Log.d(TAG, "Network capabilities changed. New type: " + newNetworkType);
                
                // Store previous network type before updating
                previousNetworkType = currentNetworkType;
                currentNetworkType = newNetworkType;
                
                // Check if it's a downgrade from 5G to 4G or lower
                if (isNetworkDowngrade(previousNetworkType, currentNetworkType)) {
                    sendDowngradeNotification(previousNetworkType, currentNetworkType);
                    saveNetworkChange(previousNetworkType, currentNetworkType);
                }
                
                // Notify MainActivity about the change
                Intent intent = new Intent("NETWORK_CHANGED");
                intent.putExtra("network_type", newNetworkType);
                sendBroadcast(intent);
            }
        };

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private boolean isNetworkDowngrade(String oldNetwork, String newNetwork) {
        // Check if we're downgrading from 5G to 4G or lower
        return "5G".equals(oldNetwork) && 
               ("4G".equals(newNetwork) || "3G".equals(newNetwork) || "2G".equals(newNetwork) || "Unknown".equals(newNetwork));
    }

    private String getNetworkType(NetworkCapabilities capabilities) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return "WiFi";
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NR_SA)
                    || capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NR_SA_MMWAVE)) {
                return "5G";
            } else if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_LTE)) {
                return "4G";
            } else if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_IMS)) {
                // This is a rough approximation
                return "3G";
            } else {
                return "2G";
            }
        }
        return "Unknown";
    }

    private void sendDowngradeNotification(String oldNetwork, String newNetwork) {
        String title = getString(R.string.network_downgrade_notification_title);
        String text = getString(R.string.network_downgrade_notification_text, oldNetwork, newNetwork);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(DOWNGRADE_NOTIFICATION_ID, builder.build());
    }

    private void saveNetworkChange(String oldNetwork, String newNetwork) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d(TAG, "Network changed at " + timestamp + " from " + oldNetwork + " to " + newNetwork);
        
        // Save to Room database
        NetworkHistoryEntity networkHistory = new NetworkHistoryEntity(timestamp, oldNetwork, newNetwork);
        new Thread(() -> {
            database.networkHistoryDao().insert(networkHistory);
        }).start();
    }

    public String getCurrentNetworkType() {
        return currentNetworkType;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}