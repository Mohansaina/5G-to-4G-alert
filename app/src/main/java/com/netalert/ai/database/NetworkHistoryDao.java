package com.netalert.ai.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NetworkHistoryDao {
    @Query("SELECT * FROM network_history ORDER BY id DESC")
    List<NetworkHistoryEntity> getAllNetworkHistory();

    @Query("SELECT * FROM network_history WHERE timestamp >= :startOfDay ORDER BY id DESC")
    List<NetworkHistoryEntity> getNetworkHistoryForDay(String startOfDay);

    @Insert
    void insert(NetworkHistoryEntity networkHistory);

    @Query("DELETE FROM network_history")
    void deleteAll();
}