package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaring variables for the UI elements and Firebase database
    private TextView heatCount, sickCount, pregnantCount, medicatedCount;
    private TextView cowCount, sheepCount, heiferCount, bullCount, totalLivestock;
    private DatabaseReference livestockRef;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardpage);
        setupBottomNavigationView();

        // Retrieve the userId from UserSession
        userId = userSession.getInstance().getUserId();
        if (userId == null) {
            Toast.makeText(this, "User ID not found, cannot load dashboard.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set the Firebase reference to the logged-in user's livestock data based on user id
        livestockRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("livestock");

        // Initialising the UI elements
        heatCount = findViewById(R.id.heat_count);
        sickCount = findViewById(R.id.sick_count);
        pregnantCount = findViewById(R.id.pregnant_count);
        medicatedCount = findViewById(R.id.medicated_count);
        cowCount = findViewById(R.id.cow_count);
        sheepCount = findViewById(R.id.sheep_count);
        heiferCount = findViewById(R.id.heifer_count);
        bullCount = findViewById(R.id.bull_count);
        totalLivestock = findViewById(R.id.total_livestock);

        // Call the method to fetch and update the dashboard data
        fetchDashboardData();
    }

    /**
     * Fetches and displays the dashboard data from Firebase Realtime Database
     */
    private void fetchDashboardData() {
        livestockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int heatCounter = 0, sickCounter = 0, pregnantCounter = 0, medicatedCounter = 0;
                int cowCounter = 0, sheepCounter = 0, heiferCounter = 0, bullCounter = 0;

                // Looping through each type of livestock and updating counters based on the status
                for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                    String type = typeSnapshot.getKey();
                    for (DataSnapshot animalSnapshot : typeSnapshot.getChildren()) {

                        // Checking the status of each animal and updating the counters accordingly
                        Boolean isHeat = animalSnapshot.child("isHeat").getValue(Boolean.class);
                        Boolean isSick = animalSnapshot.child("isSick").getValue(Boolean.class);
                        Boolean isPregnant = animalSnapshot.child("isPregnant").getValue(Boolean.class);
                        Boolean isMedicated = animalSnapshot.child("isMedicated").getValue(Boolean.class);

                        // Updating the counters based on the status of each animal
                        if (Boolean.TRUE.equals(isHeat)) heatCounter++;
                        if (Boolean.TRUE.equals(isSick)) sickCounter++;
                        if (Boolean.TRUE.equals(isPregnant)) pregnantCounter++;
                        if (Boolean.TRUE.equals(isMedicated)) medicatedCounter++;

                        // Count the number of each type of livestock
                        switch (type) {
                            case "cows":
                                cowCounter++;
                                break;
                            case "sheeps":
                                sheepCounter++;
                                break;
                            case "heifers":
                                heiferCounter++;
                                break;
                            case "bulls":
                                bullCounter++;
                                break;
                        }
                    }
                }

                // Adding up the total number of livestock
                int totalCounter = cowCounter + sheepCounter + heiferCounter + bullCounter;

                // Setting the counters to the UI elements Textview
                heatCount.setText(String.valueOf(heatCounter));
                sickCount.setText(String.valueOf(sickCounter));
                pregnantCount.setText(String.valueOf(pregnantCounter));
                medicatedCount.setText(String.valueOf(medicatedCounter));
                cowCount.setText("Cows: " + cowCounter);
                sheepCount.setText("  Sheep: " + sheepCounter);
                heiferCount.setText("Heifers: " + heiferCounter);
                bullCount.setText("Bulls: " + bullCounter);
                totalLivestock.setText("Total Livestock: " + totalCounter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error loading dashboard data", databaseError.toException());
            }
        });
    }

    /**
     * Sets up the bottom navigation view with listeners for each menu item to navigate to different activities.
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {
                    return true;
                } else if (itemId == R.id.nav_map) {
                    startActivity(new Intent(MainActivity.this, farmMapsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_tags) {
                    startActivity(new Intent(MainActivity.this, LivestockActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, profileActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
