package com.example.smartfarmflow;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfarmflow.models.Animal;

public class AnimalDetailsActivity extends AppCompatActivity {

    private TextView name, age, gender, temperature, weight, status, stand, walk, sit, eat, drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_info_page);

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


        Animal animal = (Animal) getIntent().getSerializableExtra("animalData");

        if (animal != null) {

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
    }
}
