package com.example.mobileproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class CreateRentRequestActivity extends AppCompatActivity {

    private DatePickerDialog startDatePickerDialog,endDatePickerDialog;
    private Button startDateButton,endDateButton;
    private Spinner location_spinner;
    private TextView startTxt,endTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_rent_request);




        initDatePicker();
        setViews();

        String[] values =getResources().getStringArray(R.array.location_values);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);

        // Apply the adapter to the spinner
        location_spinner.setAdapter(adapter);

        startTxt.setText("------");
        endTxt.setText("------");


    }

    private void setViews(){
        startTxt = (TextView)findViewById(R.id.create_rent_request_start_text);
        endTxt = (TextView)findViewById(R.id.create_rent_request_end_text);
        location_spinner  = (Spinner) findViewById(R.id.create_rent_request_spinner);


    }

    public void backToViewCarDetails(View view){
        Intent intent = new Intent(this,ViewCarActivity.class);
        startActivity(intent);


    }


    public void sendRentRequest(View view){
        Toast.makeText(this, "request this car", Toast.LENGTH_SHORT).show();

    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startTxt.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                endTxt.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        startDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, startDateSetListener, year, month, day);
        endDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, endDateSetListener, year, month, day);


    }

    private String makeDateString(int day, int month, int year)
    {
        return  day + " " + month + " " + year;
    }


    public void openStartDatePicker(View view)
    {
        startDatePickerDialog.show();
    }

    public void openEndDatePicker(View view)
    {
        endDatePickerDialog.show();
    }

}