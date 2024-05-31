package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class createAccount extends AppCompatActivity {

    private EditText usernameEditText, dobEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        Button createAccountButton = findViewById(R.id.create_account_button);
        TextView logInTextView = findViewById(R.id.log_in);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String dob = dobEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

            }
        });

        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(createAccount.this,login.class);
                startActivity(intent);
            }
        });
    }
}
