package com.example.mycarsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.mobileproject.AddNewCar;
import com.example.mobileproject.Request;
import com.example.mobileproject.login;

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
        carImage1 = findViewById(R.id.carImage1);
        carImage2 = findViewById(R.id.carImage2);
        carName1 = findViewById(R.id.carName1);
        carPrice1 = findViewById(R.id.carPrice1);
        carDescription1 = findViewById(R.id.carDescription1);
        carName2 = findViewById(R.id.carName2);
        carPrice2 = findViewById(R.id.carPrice2);
        carDescription2 = findViewById(R.id.carDescription2);

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
    }

    private void openPhoto() {
        Intent intent = new Intent(this, Request.class);
        startActivity(intent);
    }

    private void addCarsOpen() {
        Intent intent = new Intent(this, AddNewCar.class);
        startActivity(intent);
    }
}
