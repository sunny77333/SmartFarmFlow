package com.example.smartfarmflow;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardpage);


        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_home) {

                    return true;

                } else if (itemId == R.id.nav_map) {

                    startActivity(new Intent(MainActivity.this, farmMapsActivity.class));
                    return true;

                } else if (itemId == R.id.nav_tags) {
                    startActivity(new Intent(MainActivity.this, LivestockActivity.class));
                    return true;

                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, farmMapsActivity.class));
                    return true;

                } else {
                    return false;
                }
            }
        });

    }


}