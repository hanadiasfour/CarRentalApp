package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Main1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Button logButton = findViewById(R.id.log);
        Button signButton = findViewById(R.id.sign);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlogin();
            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensignip();
            }
        });
    }

    public void openlogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public void opensignip() {
        Intent intent = new Intent(this, createAccount.class);
        startActivity(intent);
    }
}
