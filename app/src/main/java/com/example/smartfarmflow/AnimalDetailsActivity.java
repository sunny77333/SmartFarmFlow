package com.example.smartfarmflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfarmflow.models.Animal;

public class AnimalDetailsActivity extends AppCompatActivity {
    // Declare all the TextView variables to display animal details
    private TextView name, age, gender, temperature, weight, status, stand, walk, sit, eat, drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the layout for the activity
        setContentView(R.layout.animal_info_page);

        //Initialising the back arrow button
        ImageView backArrow = findViewById(R.id.back_button);

        //Initialising all the TextView variables to display the animal details
        name = findViewById(R.id.animal_name);
        age = findViewById(R.id.animal_age);
        gender = findViewById(R.id.animal_gender);
        temperature = findViewById(R.id.animal_temperature);
        weight = findViewById(R.id.animal_weight);
        status = findViewById(R.id.animal_status);
        stand = findViewById(R.id.stand_time);
        walk = findViewById(R.id.walk_time);
        sit = findViewById(R.id.sit_time);
        eat = findViewById(R.id.eat_time);
        drink = findViewById(R.id.drink_time);

        //Retrieving the animal data object passed from the activity
        Animal animal = (Animal) getIntent().getSerializableExtra("animalData");

        //If statement checks if animal data is available
        if (animal != null) {

            //setting the text of the TextView variables to display the animal details
            name.setText(animal.getName());
            age.setText(animal.getAge());
            gender.setText(animal.getGender());
            temperature.setText(animal.getTemperature()+ "°C");
            weight.setText(animal.getWeight()+ "kg");
            status.setText("Status: " + animal.getStatus());
            stand.setText(animal.getStandTime());
            walk.setText(animal.getWalkTime());
            sit.setText(animal.getSitTime());
            eat.setText(animal.getSitTime());
            drink.setText(animal.getDrinkTime());

        }

        //setting the onClickListener for the back arrow button
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the LivestockActivity page
                Intent intent = new Intent(AnimalDetailsActivity.this, LivestockActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
