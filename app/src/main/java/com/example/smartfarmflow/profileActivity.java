package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {

    // Declaring variables for UI elements and Firebase database
    private TextView profileName, profileUsername, profilePassword, profileEmail, profileLocation, profileFarmName;
    private DatabaseReference userRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);
        overridePendingTransition(0, 0);

        // Initialising the UI elements
        profileName = findViewById(R.id.profile_name);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);
        profileEmail = findViewById(R.id.profile_email);
        profileLocation = findViewById(R.id.profile_location);
        profileFarmName = findViewById(R.id.profile_farm_name);

        // Retrieving the user id from userSession
        userId = userSession.getInstance().getUserId();

        if (userId != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        } else {
            Toast.makeText(this, "User ID not found, cannot load profile data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchProfileInfo(); // Calling the method to fetch and display profile information
        setupBottomNavigationView();
    }

    /**
     * Fetches and displays the profile information from Firebase Realtime Database
     */
    private void fetchProfileInfo() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Extracting the profile information from the snapshot
                String name = snapshot.child("username").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                String farmName = snapshot.child("farmName").getValue(String.class);

                // Setting the profile information to the UI elements
                profileUsername.setText(name);
                profilePassword.setText(password != null ? "********" : "N/A");
                profileEmail.setText(email);
                profileLocation.setText(location);
                profileFarmName.setText(farmName);
                profileName.setText(name != null ? name : "N/A");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profileActivity.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                Log.e("profileActivity", "Error loading profile data", error.toException());
            }
        });
    }

    /**
     * Sets up the bottom navigation view with listeners for each menu item to navigate to different activities.
     */

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(profileActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_map) {
                    startActivity(new Intent(profileActivity.this, farmMapsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_tags) {
                    startActivity(new Intent(profileActivity.this, LivestockActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
