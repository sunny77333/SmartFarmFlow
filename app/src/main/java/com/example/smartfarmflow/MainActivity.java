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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.nav_profile) {

                    return true;
                } else if (itemId == R.id.nav_home) {

                    return true;
                } else if (itemId == R.id.nav_map) {

                    return true;
                } else if (itemId == R.id.nav_tags) {
                    Intent intent = new Intent(MainActivity.this, LivestockActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
}
