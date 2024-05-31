package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_car);

    }


    public void setDetailsToRentCar(View view){

        Intent intent = new Intent(this,CreateRentRequestActivity.class);
        startActivity(intent);

    }

    public void backToBrowse(View view){
Intent intent = new Intent(this,BrowseCarsActivity.class);
startActivity(intent);



    }
}