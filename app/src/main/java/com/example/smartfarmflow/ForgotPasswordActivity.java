package com.example.smartfarmflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, farmNameEditText;
    private DatabaseReference usersRef;
    private ImageView backButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        farmNameEditText = findViewById(R.id.farm_name);
        Button verifyButton = findViewById(R.id.verify_button);
        backButton = findViewById(R.id.back_arrow);

        // Set up the verify button click listener
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String farmName = farmNameEditText.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || farmName.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    verifyUser(username, email, farmName);
                }
            }
        });

        //setting the onClickListener for the back arrow button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the LivestockActivity page
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void verifyUser(String username, String email, String farmName) {
        // Query Firebase to match the user inputs with the database
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbFarmName = userSnapshot.child("farmName").getValue(String.class);

                    if (dbEmail != null && dbEmail.equals(email) && dbFarmName != null && dbFarmName.equals(farmName)) {
                        userFound = true;
                        showPasswordResetDialog(userSnapshot.getRef());
                        break;
                    }
                }
                if (!userFound) {
                    Toast.makeText(ForgotPasswordActivity.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "verifyUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void showPasswordResetDialog(DatabaseReference userRef) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Set up the input field for the new password
        final EditText newPasswordEditText = new EditText(this);
        newPasswordEditText.setHint("Enter new password");
        builder.setView(newPasswordEditText);

        // Set up the buttons for the dialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                if (newPassword.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the user's password in Firebase
                    userRef.child("password").setValue(newPassword);
                    Toast.makeText(ForgotPasswordActivity.this, "Password successfully reset", Toast.LENGTH_LONG).show();
                    finish();  // Optionally close the activity
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
