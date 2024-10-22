package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LivestockTagsActivity extends AppCompatActivity {

    private RecyclerView livestockRecyclerView;
    private LivestockTagAdapter livestockTagAdapter;
    private List<String> livestockTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_tags);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_profile) {

                    return true;
                } else if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(LivestockTagsActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_map) {

                    return true;
                } else if (itemId == R.id.nav_tags) {

                    return true;
                } else {
                    return false;
                }
            }
        });

        livestockRecyclerView = findViewById(R.id.livestock_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        livestockRecyclerView.setLayoutManager(gridLayoutManager);

        livestockTags = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            livestockTags.add("#" + i);
        }

        livestockTagAdapter = new LivestockTagAdapter(livestockTags);
        livestockRecyclerView.setAdapter(livestockTagAdapter);
    }
}
