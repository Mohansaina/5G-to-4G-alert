package com.netalert.ai;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netalert.ai.database.NetworkHistoryDatabase;
import com.netalert.ai.database.NetworkHistoryEntity;
import com.netalert.ai.utils.ExportUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NetworkHistoryDatabase database;
    private NetworkMonitorService networkMonitorService;
    private boolean isServiceBound = false;
    private TextView currentNetworkText;
    private ImageView networkIcon;
    private TextView downgradesToday;
    private TextView downgradesWeek;
    private RecyclerView historyRecyclerView;
    private NetworkHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = NetworkHistoryDatabase.getDatabase(this);
        
        // Initialize UI components
        initUI();
        
        // Request necessary permissions
        requestPermissions();
        
        // Start the network monitoring service
        startNetworkMonitoringService();
        
        // Load network history
        loadNetworkHistory();
        
        // Set up export button
        Button exportLogsButton = findViewById(R.id.exportLogsButton);
        exportLogsButton.setOnClickListener(v -> exportLogs());
    }
    
    private void initUI() {
        currentNetworkText = findViewById(R.id.currentNetworkText);
        networkIcon = findViewById(R.id.networkIcon);
        downgradesToday = findViewById(R.id.downgradesToday);
        downgradesWeek = findViewById(R.id.downgradesWeek);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        
        // Set up RecyclerView
        historyAdapter = new NetworkHistoryAdapter();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
    }
    
    private void requestPermissions() {
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }
    
    private void startNetworkMonitoringService() {
        Intent serviceIntent = new Intent(this, NetworkMonitorService.class);
        // Start service in foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NetworkMonitorService.LocalBinder binder = (NetworkMonitorService.LocalBinder) service;
            networkMonitorService = binder.getService();
            isServiceBound = true;
            // Update UI with current network type
            if (networkMonitorService != null) {
                updateNetworkUI(networkMonitorService.getCurrentNetworkType());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };
    
    private void loadNetworkHistory() {
        new Thread(() -> {
            List<NetworkHistoryEntity> historyList = database.networkHistoryDao().getAllNetworkHistory();
            runOnUiThread(() -> {
                historyAdapter.updateHistory(historyList);
            });
        }).start();
    }
    
    private void updateNetworkUI(String networkType) {
        if (currentNetworkText != null) {
            currentNetworkText.setText(networkType != null ? networkType : "Unknown");
        }
        
        if (networkIcon != null) {
            // Update icon based on network type
            int iconResource = R.drawable.ic_network_4g; // Default to 4G
            if (networkType != null) {
                switch (networkType) {
                    case "5G":
                        iconResource = R.drawable.ic_network_5g;
                        break;
                    case "4G":
                        iconResource = R.drawable.ic_network_4g;
                        break;
                    case "WiFi":
                        iconResource = R.drawable.ic_network_5g; // Using 5G icon for WiFi as placeholder
                        break;
                    default:
                        iconResource = R.drawable.ic_network_4g; // Default to 4G for other types
                        break;
                }
            }
            networkIcon.setImageResource(iconResource);
        }
    }
    
    private void exportLogs() {
        new Thread(() -> {
            List<NetworkHistoryEntity> historyList = database.networkHistoryDao().getAllNetworkHistory();
            String fileName = "network_history_" + System.currentTimeMillis() + ".csv";
            boolean success = ExportUtils.exportToCSV(this, historyList, fileName);
            
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Export successful: " + fileName, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload history when returning to the app
        loadNetworkHistory();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }
}