package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddNewCarActivity extends AppCompatActivity {

    private TextView addLabel;
    private ImageView profilePhoto;
    private Button addPhotoButton;
    private EditText carName;
    private EditText pricePerDay;
    private EditText carInformation;
    private EditText description;
    private Button addButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        addLabel = findViewById(R.id.add3_label);
        profilePhoto = findViewById(R.id.profile_photo4);
        addPhotoButton = findViewById(R.id.add_photo_button4);
        carName = findViewById(R.id.car_name2);
        pricePerDay = findViewById(R.id.price_per_day4);
        carInformation = findViewById(R.id.car_information2);
        description = findViewById(R.id.description4);
        addButton = findViewById(R.id.Delete_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCar();
            }
        });
    }

    private void editPhoto() {
    }

    private void addNewCar() {
        // Add code to handle adding a new car
        String name = carName.getText().toString();
        String price = pricePerDay.getText().toString();
        String info = carInformation.getText().toString();
        String desc = description.getText().toString();

    }
}
