package com.example.smartfarmflow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


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
    private String currentPassword;

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

        // Initialise the Edit Profile button and set click listener
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        // Initialize the logout button and set click listener
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
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
                currentPassword = snapshot.child("password").getValue(String.class); // Store real password
                String email = snapshot.child("email").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                String farmName = snapshot.child("farmName").getValue(String.class);

                // Setting the profile information to the UI elements
                profileUsername.setText(name);
                profilePassword.setText(currentPassword != null ? "********" : "N/A"); // Display masked password
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
     * Displays a dialog to edit profile information and also confirms current password before saving changes.
     */
    private void showEditProfileDialog() {
        // Inflate the custom layout for the dialog view
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editLocation = dialogView.findViewById(R.id.edit_location);
        EditText editFarmName = dialogView.findViewById(R.id.edit_farm_name);
        EditText confirmPassword = dialogView.findViewById(R.id.confirm_password);

        // Set current profile data in edit fields for editing
        editName.setText(profileName.getText().toString());
        editPassword.setText(currentPassword); // Set to real password to allow editing
        editEmail.setText(profileEmail.getText().toString());
        editLocation.setText(profileLocation.getText().toString());
        editFarmName.setText(profileFarmName.getText().toString());

        // Building the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set the positive button click listener to save changes
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString().trim();
                String newPassword = editPassword.getText().toString().trim();
                String newEmail = editEmail.getText().toString().trim();
                String newLocation = editLocation.getText().toString().trim();
                String newFarmName = editFarmName.getText().toString().trim();
                String enteredPassword = confirmPassword.getText().toString().trim();

                // Verify that the entered password matches the actual current password from Firebase
                if (!enteredPassword.equals(currentPassword)) {
                    Toast.makeText(profileActivity.this, "Incorrect password. Changes not saved.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update Firebase with new data if password is confirmed
                userRef.child("username").setValue(newName);
                userRef.child("password").setValue(newPassword);
                userRef.child("email").setValue(newEmail);
                userRef.child("location").setValue(newLocation);
                userRef.child("farmName").setValue(newFarmName);

                Toast.makeText(profileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    /**
     * Clears the user session data and navigates to LoginActivity.
     */
    private void logout() {
        // Clear user session
        userSession.getInstance().clearSession();

        // Navigate to LoginActivity
        Intent intent = new Intent(profileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
