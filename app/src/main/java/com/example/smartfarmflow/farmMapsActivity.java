package com.example.smartfarmflow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.smartfarmflow.models.Animal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class farmMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Declaring Google Map object for displaying the map
    private DatabaseReference animalRef; // Reference to the Firebase database
    private List<Animal> animalList = new ArrayList<>(); // List to store animal data
    private String userId;  // Declaring a string variable to store the User ID to dynamically fetch data from firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        overridePendingTransition(0, 0);

        // Retrieving the user id from userSession
        userId = userSession.getInstance().getUserId();

        if (userId != null) {
            // Set the Firebase reference to the logged-in user's livestock data based on user id
            animalRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("livestock");
        } else {
            Toast.makeText(this, "User ID not found, cannot load livestock data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Log userId for debugging
        Log.d("farmMapsActivity", "User ID: " + userId);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupBottomNavigationView();
    }

    /**
     * Setting the Google Map object and loading animal markers on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // Set map to satellite mode
            loadAnimalMarkers();
        } else {
            Log.e("farmMapsActivity", "GoogleMap is null in onMapReady");
        }
    }
    /**
     * Loads markers on the map for each animal's location based on latitude and longitude values.
     */
    private void loadAnimalMarkers() {
        animalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFirstAnimal = true;

                //Loop through each animal type and add markers for each animal
                for (DataSnapshot animalTypeSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot animalSnapshot : animalTypeSnapshot.getChildren()) {
                        Animal animal = animalSnapshot.getValue(Animal.class);
                        if (animal != null) {
                            LatLng animalLocation = new LatLng(
                                    Double.parseDouble(animal.getLatitude()),
                                    Double.parseDouble(animal.getLongitude())
                            );

                            //Add marker for each animal with it name attached
                            mMap.addMarker(new MarkerOptions()
                                    .position(animalLocation)
                                    .title(animal.getName()));

                            //Center camera on the first animal
                            if (isFirstAnimal) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(animalLocation, 16));
                                isFirstAnimal = false;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("farmMapsActivity", "Failed to load animal markers", error.toException());
            }
        });
    }

    /**
     * Sets up the bottom navigation view with listeners for each menu item to navigate to different activities.
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(farmMapsActivity.this, MainActivity.class).putExtra("USER_ID", userId));
                    return true;

                } else if (itemId == R.id.nav_map) {
                    return true;

                } else if (itemId == R.id.nav_tags) {
                    startActivity(new Intent(farmMapsActivity.this, LivestockActivity.class).putExtra("USER_ID", userId));
                    return true;

                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(farmMapsActivity.this, profileActivity.class).putExtra("USER_ID", userId));
                    return true;

                } else {
                    return false;
                }
            }
        });
    }
}
