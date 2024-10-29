package com.example.smartfarmflow;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signupActivity extends AppCompatActivity {

    // Declaring the variables for UI elements and Firebase database
    private EditText usernameEditText, emailEditText, passwordEditText, locationEditText, farmNameEditText;
    private Button signupButton;
    private DatabaseReference usersRef;
    private ImageView backButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        // Initialising the Firebase database reference to users node
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialising the UI elements
        usernameEditText = findViewById(R.id.signup_username);
        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        locationEditText = findViewById(R.id.signup_location);
        farmNameEditText = findViewById(R.id.signup_farmname);
        signupButton = findViewById(R.id.signup_button);
        backButton = findViewById(R.id.back_arrow);

        // Setting up the signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieving the user input from the each field
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String farmName = farmNameEditText.getText().toString().trim();

                // Check if all fields are filled before registering the user
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !location.isEmpty() && !farmName.isEmpty()) {
                    registerUser(username, email, password, location, farmName);
                } else {
                    Toast.makeText(signupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //setting the onClickListener for the back arrow button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the LivestockActivity page
                Intent intent = new Intent(signupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /**
     * Registers a new user in the Firebase database.
     *
     * @param username The entered username
     * @param email    The entered email address
     * @param password The entered password
     * @param location The user's location
     * @param farmName The name of the user's farm
     */

    private void registerUser(String username, String email, String password, String location, String farmName) {
        String userId = usersRef.push().getKey();

        if (userId != null) {
            // Create map to store user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("location", location);
            userData.put("farmName", farmName);

            usersRef.child(userId).setValue(userData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(signupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    // Navigate to the login page after successful signup
                    Intent intent = new Intent(signupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(signupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Failed to generate user ID. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}