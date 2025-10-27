package com.netalert.ai.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkHistoryRepository {
    private NetworkHistoryDao networkHistoryDao;
    private ExecutorService executorService;

    public NetworkHistoryRepository(Application application) {
        NetworkHistoryDatabase db = NetworkHistoryDatabase.getDatabase(application);
        networkHistoryDao = db.networkHistoryDao();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(NetworkHistoryEntity networkHistory) {
        executorService.execute(() -> {
            networkHistoryDao.insert(networkHistory);
        });
    }

    public List<NetworkHistoryEntity> getAllNetworkHistory() {
        return networkHistoryDao.getAllNetworkHistory();
    }

    public List<NetworkHistoryEntity> getNetworkHistoryForDay(String startOfDay) {
        return networkHistoryDao.getNetworkHistoryForDay(startOfDay);
    }

    public void deleteAll() {
        executorService.execute(() -> {
            networkHistoryDao.deleteAll();
        });
    }
}