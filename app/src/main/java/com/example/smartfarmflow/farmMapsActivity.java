package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

    private GoogleMap mMap;
    private DatabaseReference animalRef;
    private List<Animal> animalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        animalRef = FirebaseDatabase.getInstance().getReference("users/user1/livestock");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupBottomNavigationView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadAnimalMarkers();
    }

    private void loadAnimalMarkers() {
        animalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot animalTypeSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot animalSnapshot : animalTypeSnapshot.getChildren()) {
                        Animal animal = animalSnapshot.getValue(Animal.class);
                        if (animal != null) {
                            LatLng animalLocation = new LatLng(
                                    Double.parseDouble(animal.getLatitude()),
                                    Double.parseDouble(animal.getLongitude())
                            );
                            mMap.addMarker(new MarkerOptions()
                                    .position(animalLocation)
                                    .title(animal.getName()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(farmMapsActivity.this, MainActivity.class));
                    return true;

                } else if (itemId == R.id.nav_map) {
                    return true;

                } else if (itemId == R.id.nav_tags) {
                    startActivity(new Intent(farmMapsActivity.this, LivestockActivity.class));
                    return true;

                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(farmMapsActivity.this, farmMapsActivity.class));
                    return true;

                } else {
                    return false;
                }
            }
        });
    }
}
