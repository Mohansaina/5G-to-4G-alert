package com.netalert.ai;

public class NetworkHistory {
    private String timestamp;
    private String oldNetwork;
    private String newNetwork;

    public NetworkHistory(String timestamp, String oldNetwork, String newNetwork) {
        this.timestamp = timestamp;
        this.oldNetwork = oldNetwork;
        this.newNetwork = newNetwork;
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