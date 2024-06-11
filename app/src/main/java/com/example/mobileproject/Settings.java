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

public class Settings extends AppCompatActivity {

    private String USER_KEY ="";//should be changed depending on the user that logged in


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);



        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.settings_nav_item);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.browse_cars_nav_item) {
                    Intent intent = new Intent(getApplicationContext(), BrowseCarsActivity.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }else if (id == R.id.settings_nav_item) {
                    return true;
                } else if (id == R.id.my_cars_nav_item){
                    Intent intent = new Intent(getApplicationContext(), MyCarsActivity.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(id == R.id.renting_nav_item){

                    Intent intent = new Intent(getApplicationContext(), RentingActivity.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }

                return false;
            }
        });


    }
}