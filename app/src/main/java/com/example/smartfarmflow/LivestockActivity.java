package com.example.smartfarmflow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LivestockActivity extends AppCompatActivity {

    private RecyclerView livestockRecyclerView; // Setting up the RecyclerView for displaying livestock data
    private LivestockAdapter livestockAdapter;
    private List<Animal> animalList = new ArrayList<>();
    private DatabaseReference farmersRef; // Reference to the Firebase database for data retrieval
    private String userId; // Declaring a string variable to store the User ID to dynamically fetch data from firebase
    private Spinner animalTypeSpinner;
    private List<Animal> filteredAnimalList = new ArrayList<>(); // Store filtered animals here


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_tags);
        overridePendingTransition(0, 0);
        // Setting up the RecyclerView for displaying livestock data
        livestockRecyclerView = findViewById(R.id.livestock_recycler_view);
        livestockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button addAnimalButton = findViewById(R.id.button_open_add_animal_dialog);

        animalTypeSpinner = findViewById(R.id.livestock_spinner);


        // Retrieving the user id from userSession
        userId = userSession.getInstance().getUserId();

        if (userId != null) {
            // Set the Firebase reference to the logged-in user's livestock data based on user id
            farmersRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("livestock");
        } else {
            Toast.makeText(this, "User ID not found, cannot load livestock data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchLivestockData();
        setupBottomNavigationView();
        addAnimalButton.setOnClickListener(v -> showAddAnimalDialog());
        livestockRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                filterAnimalsByType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterAnimalsByType("All"); // Show all animals if no type is selected
            }
        });

    }
    /**
     * Fetches livestock data from Firebase and updates the RecyclerView.
     */
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
                livestockAdapter = new LivestockAdapter(LivestockActivity.this, filteredAnimalList);
                livestockRecyclerView.setAdapter(livestockAdapter);

                // Initially, display all animals
                filterAnimalsByType("All");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LivestockActivity", "Failed to fetch data.", databaseError.toException());
            }
        });
    }

    private void filterAnimalsByType(String type) {
        filteredAnimalList.clear();

        if (type.equals("All")) {
            // If "All" is selected add all animals from each type
            filteredAnimalList.addAll(animalList);
            livestockAdapter.notifyDataSetChanged();
            return;
        }


        farmersRef.child(type.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filteredAnimalList.clear();
                for (DataSnapshot animalSnapshot : dataSnapshot.getChildren()) {
                    Animal animal = animalSnapshot.getValue(Animal.class);
                    if (animal != null) {
                        filteredAnimalList.add(animal);
                    }
                }
                // Update the RecyclerView with the filtered animals
                livestockAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LivestockActivity", "Error fetching animals by type", databaseError.toException());
            }
        });
    }


    /**
     * Displays a dialog to add a new animal to the livestock.
     */

    //Inflate the layout for the add animal dialog
    private void showAddAnimalDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.activity_add_animal, null);
        EditText animalNameEditText = dialogView.findViewById(R.id.editText_animal_name);
        EditText genderEditText = dialogView.findViewById(R.id.editText_animal_gender);
        EditText ageYearsEditText = dialogView.findViewById(R.id.editText_age_years);
        EditText ageMonthsEditText = dialogView.findViewById(R.id.editText_age_months);
        Spinner animalTypeSpinner = dialogView.findViewById(R.id.spinner_animal_type);
        Button addAnimalButton = dialogView.findViewById(R.id.button_add_animal);

        //Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add New Animal")
                .setView(dialogView)
                .setCancelable(true)
                .create();

        //Handle the add animal button click event
        addAnimalButton.setOnClickListener(v -> {
            String name = animalNameEditText.getText().toString().trim();
            String gender = genderEditText.getText().toString().trim();
            String ageYears = ageYearsEditText.getText().toString().trim();
            String ageMonths = ageMonthsEditText.getText().toString().trim();
            String animalType = animalTypeSpinner.getSelectedItem().toString();

            if (!name.isEmpty() && !gender.isEmpty() && !ageYears.isEmpty() && !ageMonths.isEmpty()) {
                //Call the method to add the animal to the database
                addAnimalToDatabase(name, gender, ageYears, ageMonths, animalType);
                dialog.dismiss();
            } else {
                Toast.makeText(LivestockActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    /**
     * Function to add a new animal to the Firebase database under the current user's livestock.
     *
     * @param name The name of the animal
     * @param gender The gender of the animal
     * @param ageYears  The age of the animal in years
     * @param ageMonths The age of the animal in months
     * @param animalType The type of the animal
     */
    private void addAnimalToDatabase(String name, String gender, String ageYears, String ageMonths, String animalType) {
        String animalId = farmersRef.push().getKey();

        // Define the central latitude and longitude
        final double BASE_LATITUDE = -36.961651;
        final double BASE_LONGITUDE = 174.972320;
        final double METERS_TO_DEGREES = 0.0009;

        // Generate random offsets within 100 meters
        double latOffset = (new Random().nextDouble() * 2 - 1) * METERS_TO_DEGREES;
        double lonOffset = (new Random().nextDouble() * 2 - 1) * METERS_TO_DEGREES;
        double randomLatitude = BASE_LATITUDE + latOffset;
        double randomLongitude = BASE_LONGITUDE + lonOffset;

        // Define the animal data
        Map<String, Object> animalData = new HashMap<>();
        animalData.put("name", name);
        animalData.put("gender", gender);
        animalData.put("ageYears", ageYears);
        animalData.put("ageMonths", ageMonths);
        animalData.put("activityDate", "20 Jan, 2024");
        animalData.put("drinkTime", "01:00:00");
        animalData.put("eatTime", "02:00:00");
        animalData.put("latitude", String.valueOf(randomLatitude));
        animalData.put("longitude", String.valueOf(randomLongitude));
        animalData.put("sitTime", "04:00:00");
        animalData.put("standTime", "02:15:00");
        animalData.put("status", "Healthy");
        animalData.put("tagNumber", "NewTag");
        animalData.put("temperature", "37");
        animalData.put("walkTime", "01:30:00");
        animalData.put("weight", "150");
        animalData.put("isHeat", false);
        animalData.put("isSick", true);
        animalData.put("isPregnant", false);
        animalData.put("isMedicated", true);

        //Adding the animal with data in the database under the current user's livestock
        if (animalId != null) {
            farmersRef.child(animalType.toLowerCase()).child(animalId).setValue(animalData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LivestockActivity.this, "Animal added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LivestockActivity.this, "Failed to add animal", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    /**
     * Sets up the bottom navigation view with listeners for each menu item to navigate to different activities.
     */
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
