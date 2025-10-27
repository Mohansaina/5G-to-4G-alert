package com.netalert.ai.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NetworkHistoryEntity.class}, version = 1, exportSchema = false)
public abstract class NetworkHistoryDatabase extends RoomDatabase {
    public abstract NetworkHistoryDao networkHistoryDao();

    private static volatile NetworkHistoryDatabase INSTANCE;

    public static NetworkHistoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NetworkHistoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NetworkHistoryDatabase.class, "network_history_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}