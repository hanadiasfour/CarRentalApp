package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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
