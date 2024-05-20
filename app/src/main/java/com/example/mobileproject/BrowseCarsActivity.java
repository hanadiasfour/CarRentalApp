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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BrowseCarsActivity extends AppCompatActivity {


    private ArrayList<CarCatalogItemModel> catalogList = new ArrayList<>();
    private RecyclerView CCI_RV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("LifeCycle","on Create browse");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);
        handelTabSwitch();

        CCI_RV = (RecyclerView)findViewById(R.id.CCI_RecyclerView);

        setCatalogData();

        CCI_RecyclerViewAdapter CCI_adapter = new CCI_RecyclerViewAdapter(this,catalogList);
        CCI_RV.setAdapter(CCI_adapter);
        CCI_RV.setLayoutManager(new LinearLayoutManager(this));


    }

    public void openFilters(View view){
        startActivity(new Intent(this, FilterActivity.class));

    }


    public void setCatalogData(){

        for(int i =1;i<=5 ;i++) {
            catalogList.add(new CarCatalogItemModel(R.drawable.default_car, i*1000, "Very Nice Car " + i));
        }
    }


    public void handelTabSwitch(){

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