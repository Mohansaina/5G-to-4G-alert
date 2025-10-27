package com.netalert.ai.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "network_history")
public class NetworkHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "old_network")
    private String oldNetwork;

    @ColumnInfo(name = "new_network")
    private String newNetwork;

    public NetworkHistoryEntity(String timestamp, String oldNetwork, String newNetwork) {
        this.timestamp = timestamp;
        this.oldNetwork = oldNetwork;
        this.newNetwork = newNetwork;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOldNetwork() {
        return oldNetwork;
    }

    public void setOldNetwork(String oldNetwork) {
        this.oldNetwork = oldNetwork;
    }

    public String getNewNetwork() {
        return newNetwork;
    }

    public void setNewNetwork(String newNetwork) {
        this.newNetwork = newNetwork;
    }

    public String getChangeDescription() {
        return oldNetwork + " → " + newNetwork;
    }
}