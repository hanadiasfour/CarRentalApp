package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BrowseCarsActivity extends AppCompatActivity {

    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("LifeCycle","on Create browse");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);
        b= findViewById(R.id.b);



        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.browse_cars_nav_item);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.my_cars_nav_item) {
                    startActivity(new Intent(getApplicationContext(), MyCarsActivity.class));
                    overridePendingTransition(0, 0);

                    return true;
                }else if (id == R.id.browse_cars_nav_item) {
                    return true;
                } else if (id == R.id.settings_nav_item){
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);

                        return true;
                }else if(id == R.id.renting_nav_item){

                    startActivity(new Intent(getApplicationContext(), RentingActivity.class));
                    overridePendingTransition(0, 0);


                }

                return false;
            }
        });

    }

    public void trial(View view){

        startActivity(new Intent(this, tryMe.class));



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("LifeCycle","New Intent in Browse");


    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle","on pause browse");

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","on stop browse");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle","on Destroy browse");

    }


}