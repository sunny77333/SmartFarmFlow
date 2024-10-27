package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //Declaring variables for Firebase and UI elements
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        overridePendingTransition(0, 0);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");

        // Initialising UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.sign_up_button);

        // Setting up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting the entered username and password to string variables
                String enteredUsername = usernameEditText.getText().toString().trim();
                String enteredPassword = passwordEditText.getText().toString().trim();

                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {

                    // call the loginUser method to authenticate the user
                    loginUser(enteredUsername, enteredPassword);
                }
            }
        });

        // Setting up the signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity
                Intent intent = new Intent(LoginActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Logs in the user by authenticating the user using Firebase database using username and password
     *
     * @param username The username entered by the user
     * @param password The password entered by the user
     */
    private void loginUser(String username, String password) {
        // Query the database to find the user with the entered username
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("password").getValue(String.class);
                        if (dbPassword != null && dbPassword.equals(password)) {
                            // Get the unique user ID and store into userID string variable
                            String userId = userSnapshot.getKey();

                            // Store the userId in the UserSession class
                            userSession.getInstance().setUserId(userId);

                            // Start the MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show a toast message if password is incorrect
                            Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Show a toast message if username is not found
                    Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "loginUser:onCancelled", databaseError.toException());
            }
        });
    }


}
