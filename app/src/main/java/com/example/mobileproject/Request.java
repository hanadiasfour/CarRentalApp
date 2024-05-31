package com.example.mobileproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class Request extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Button acceptButton = findViewById(R.id.accept_button);
        Button rejectButton = findViewById(R.id.reject_button);
        acceptButton.setOnClickListener(view -> {
        });

        rejectButton.setOnClickListener(view -> {
        });
    }
}
