package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyCarsActivity extends AppCompatActivity {

    private ImageButton photoButton;
    private ImageButton addButton;
    private TextView topLabel;
    private ImageView carImage1;
    private ImageView carImage2;
    private TextView carName1;
    private TextView carPrice1;
    private TextView carDescription1;
    private TextView carName2;
    private TextView carPrice2;
    private TextView carDescription2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);

        photoButton = findViewById(R.id.photoButton);
        addButton = findViewById(R.id.addButton);
        topLabel = findViewById(R.id.topLabel);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarsOpen();
            }
        });
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.my_cars_nav_item);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.browse_cars_nav_item) {
                    startActivity(new Intent(getApplicationContext(), BrowseCarsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.my_cars_nav_item) {
                    return true;
                } else if (id == R.id.settings_nav_item) {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.renting_nav_item) {

                    startActivity(new Intent(getApplicationContext(), RentingActivity.class));
                    overridePendingTransition(0, 0);

                }

                return false;
            }
        });
    }
    private void openPhoto() {
        Intent intent = new Intent(this, Request.class);
        startActivity(intent);
    }

    private void addCarsOpen() {
        Intent intent = new Intent(this, AddNewCarActivity.class);
        startActivity(intent);
    }
}
