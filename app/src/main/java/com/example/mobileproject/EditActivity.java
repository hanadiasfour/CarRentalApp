package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    ImageView profilePhoto;
    EditText carName, pricePerDay, carInformation, description;
    Button addPhotoButton, deleteButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        profilePhoto = findViewById(R.id.profile_photo3);
        carName = findViewById(R.id.car_name2);
        pricePerDay = findViewById(R.id.price_per_day2);
        carInformation = findViewById(R.id.car_information2);
        description = findViewById(R.id.description2);
        addPhotoButton = findViewById(R.id.add_photo_button2);
        deleteButton = findViewById(R.id.Delete_button);
        saveButton = findViewById(R.id.Save_button);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "Add/Edit Photo clicked", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "Delete Car clicked", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "Save clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
