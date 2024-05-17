package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RentingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renting);


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.renting_nav_item);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.browse_cars_nav_item) {
                    startActivity(new Intent(getApplicationContext(), BrowseCarsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if (id == R.id.renting_nav_item) {
                    return true;
                } else if (id == R.id.my_cars_nav_item){
                    startActivity(new Intent(getApplicationContext(), MyCarsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if(id == R.id.settings_nav_item){
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    overridePendingTransition(0,0);




                }

                return false;
            }
        });



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.renting_xml), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}