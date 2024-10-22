package com.example.smartfarmflow.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarmflow.AnimalDetailsActivity;
import com.example.smartfarmflow.R;
import com.example.smartfarmflow.models.Animal;

import java.util.List;

public class LivestockAdapter extends RecyclerView.Adapter<LivestockAdapter.ViewHolder> {

    private List<Animal> animalList;
    private Context context;

    public LivestockAdapter(Context context, List<Animal> animalList) {
        this.context = context;
        this.animalList = animalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livestock_tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.animalTag.setText(animal.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnimalDetailsActivity.class);
            intent.putExtra("animalData", animal);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView animalTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            animalTag = itemView.findViewById(R.id.livestock_tag_label);
        }
    }
}
