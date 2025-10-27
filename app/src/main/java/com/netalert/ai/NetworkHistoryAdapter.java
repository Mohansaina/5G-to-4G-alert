package com.netalert.ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.netalert.ai.database.NetworkHistoryEntity;
import java.util.List;

public class NetworkHistoryAdapter extends RecyclerView.Adapter<NetworkHistoryAdapter.ViewHolder> {
    private List<NetworkHistoryEntity> historyList;

    public NetworkHistoryAdapter() {
        this.historyList = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_network_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (historyList != null) {
            NetworkHistoryEntity history = historyList.get(position);
            holder.timestampText.setText(history.getTimestamp());
            holder.networkChangeText.setText(history.getChangeDescription());
        }
    }

    @Override
    public int getItemCount() {
        return historyList == null ? 0 : historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestampText;
        TextView networkChangeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timestampText = itemView.findViewById(R.id.timestampText);
            networkChangeText = itemView.findViewById(R.id.networkChangeText);
        }
    }

    public void updateHistory(List<NetworkHistoryEntity> newHistoryList) {
        this.historyList = newHistoryList;
        notifyDataSetChanged();
    }
}