package com.netalert.ai.utils;

import android.content.Context;
import android.os.Environment;

import com.netalert.ai.database.NetworkHistoryEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportUtils {
    
    public static boolean exportToCSV(Context context, List<NetworkHistoryEntity> historyList, String fileName) {
        try {
            // Create CSV content
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Timestamp,Old Network,New Network\n");
            
            for (NetworkHistoryEntity history : historyList) {
                csvContent.append(history.getTimestamp())
                        .append(",")
                        .append(history.getOldNetwork())
                        .append(",")
                        .append(history.getNewNetwork())
                        .append("\n");
            }
            
            // Write to file
            File exportDir = new File(context.getExternalFilesDir(null), "exports");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            File file = new File(exportDir, fileName);
            FileWriter writer = new FileWriter(file);
            writer.write(csvContent.toString());
            writer.close();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}