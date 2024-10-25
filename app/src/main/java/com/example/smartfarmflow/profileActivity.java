package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
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

    private TextView profileName, profileUsername, profilePassword, profileEmail, profileLocation, profileFarmName;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);
        overridePendingTransition(0, 0);

        profileName = findViewById(R.id.profile_name);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);
        profileEmail = findViewById(R.id.profile_email);
        profileLocation = findViewById(R.id.profile_location);
        profileFarmName = findViewById(R.id.profile_farm_name);

        userRef = FirebaseDatabase.getInstance().getReference("users/user1");

        fetchProfileInfo();

        setupBottomNavigationView();
    }

    private void fetchProfileInfo() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("username").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                String farmName = snapshot.child("farmName").getValue(String.class);

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
            }
        });
    }

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
