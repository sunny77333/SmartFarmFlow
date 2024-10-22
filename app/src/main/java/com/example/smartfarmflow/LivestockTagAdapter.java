package com.example.smartfarmflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LivestockTagAdapter extends RecyclerView.Adapter<LivestockTagAdapter.ViewHolder> {

    private List<String> livestockTags;

    public LivestockTagAdapter(List<String> livestockTags) {
        this.livestockTags = livestockTags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livestock_tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tagLabel = livestockTags.get(position);
        holder.livestockTagLabel.setText(tagLabel);

    }

    @Override
    public int getItemCount() {
        return livestockTags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView livestockTagIcon;
        TextView livestockTagLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            livestockTagIcon = itemView.findViewById(R.id.livestock_tag_icon);
            livestockTagLabel = itemView.findViewById(R.id.livestock_tag_label);
        }
    }
}
