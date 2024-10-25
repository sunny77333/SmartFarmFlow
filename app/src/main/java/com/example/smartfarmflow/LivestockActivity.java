package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarmflow.adapters.LivestockAdapter;
import com.example.smartfarmflow.models.Animal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LivestockActivity extends AppCompatActivity {

    private RecyclerView livestockRecyclerView;
    private LivestockAdapter livestockAdapter;
    private List<Animal> animalList = new ArrayList<>();
    private DatabaseReference farmersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_tags);
        overridePendingTransition(0, 0);

        farmersRef = FirebaseDatabase.getInstance().getReference("users/user1/livestock");

        livestockRecyclerView = findViewById(R.id.livestock_recycler_view);
        livestockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchLivestockData();

        setupBottomNavigationView();
    }

    private void fetchLivestockData() {
        farmersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                for (DataSnapshot livestockSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot animalSnapshot : livestockSnapshot.getChildren()) {
                        Animal animal = animalSnapshot.getValue(Animal.class);
                        if (animal != null) {
                            animalList.add(animal);
                        }
                    }
                }
                livestockAdapter = new LivestockAdapter(LivestockActivity.this, animalList);
                livestockRecyclerView.setAdapter(livestockAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LivestockActivity", "Failed to fetch data.", databaseError.toException());
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_tags);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {

                    startActivity(new Intent(LivestockActivity.this, MainActivity.class));
                    return true;

                } else if (itemId == R.id.nav_map) {

                    startActivity(new Intent(LivestockActivity.this, farmMapsActivity.class));
                    return true;

                } else if (itemId == R.id.nav_tags) {
                    return true;

                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(LivestockActivity.this, profileActivity.class));
                    return true;

                } else {
                    return false;
                }
            }
        });

    }
}
