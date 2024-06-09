package com.example.mobileproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CreateRentRequestActivity extends AppCompatActivity {

    private DatePickerDialog startDatePickerDialog,endDatePickerDialog;
    private TextView startTxt,endTxt,priceToPayTxt, carNameTitle;
    private String USER_KEY ,carID, selectedStart,selectedEnd,cName;
    private int carPrice =0;
    private ProgressDialog progressDialog;
    private  SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
    private final String INSERT_REQUEST_IF_VALID_URL = "http://10.0.2.2/api/insert_rent_request_h.php";
    private AlertDialog alertDialog;

//    private String[] status = {"",""};// to store the status of the API (error?,context)

    private boolean isError = false;
    private String context="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_rent_request);

        initDatePicker();
        setViews();
        handleIntent(getIntent());
        carNameTitle.setText(cName);

    }

    private void handleIntent(Intent intent){
       USER_KEY = intent.getStringExtra(getString(R.string.USER_KEY));
        carID = intent.getStringExtra(getString(R.string.CAR_ID));
        carPrice = intent.getIntExtra(getString(R.string.CAR_PRICE),0);
        cName = intent.getStringExtra(getString(R.string.CAR_NAME));

    }


    private void showAlertDialog(String title,String context) {
        // inflating the custom layout (alert_layout)
        View alertView = LayoutInflater.from(this).inflate(R.layout.alert_layout, null);

        // creating AlertDialog builder to show as alert
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(alertView);

        alertDialog = alertDialogBuilder.create();// initializing the AlertDialog

        // initializing the views in the custom layout made
        TextView alertTitle = alertView.findViewById(R.id.alert_title);
        TextView alertContext = alertView.findViewById(R.id.alert_context);
        Button okButton = alertView.findViewById(R.id.alert_button);

        // setting the title and context texts
        alertTitle.setText(title);
        alertContext.setText(context);

        //button listener
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // closing the dialog
            }
        });

        alertDialog.show();// showing the dialog
    }



    public void sendRentRequest(View view){

        if(selectedStart == null||selectedEnd == null){
            showAlertDialog("Notice!","You must select a date range first to proceed with this action.");
            return;
        }


        // Show the progress dialog
        progressDialog = ProgressDialog.show(this, "Please Wait", "Processing...", true);


        insertIfNoConflicts();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {


                if(!isError){//no error was detected (added request in db)
                    showAlertDialog("Success!",context);

                }else{//error occurred (failed to add to db)
                    showAlertDialog("Failed!",context);
                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        }, 1000); // 1 second delay


//



    }

    private void insertIfNoConflicts() {


        StringRequest request = new StringRequest(Request.Method.POST, INSERT_REQUEST_IF_VALID_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    isError = jsonObject.getBoolean("error");
                    context = jsonObject.getString("message");


                    System.out.println(String.valueOf(isError) + context);

                    ////////////////////////////////////////////////
                    // OPEN A WARNING DIALOG THAT SAYS WHATS WRONG//
                    ////////////////////////////////////////////////
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error2", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<>();
                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("start", selectedStart);
                params.put("end", selectedEnd);
                params.put("renter_gmail", USER_KEY);
                params.put("car_number", carID);
                // at last we are returning our params.
                return params;
            }
        };

        Volley.newRequestQueue(CreateRentRequestActivity.this).add(request);

    }



    private boolean validateDates(String startDateStr, String endDateStr) {
        Date startDate = null;
        Date endDate =null;
        try {
            startDate = formatter.parse(startDateStr);
            endDate = formatter.parse(endDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date today = new Date();

        if (startDate.before(today)) {
            // Start date is before today
            return false;
        }
        if (endDate.before(startDate)) {
            // End date is before start date
            return false;
        }

        if (startDate.equals(endDate)) {
            // Start date is the same as end date
            return false;
        }
        return true;
    }



    public void backToViewCarDetails(View view){
        Intent intent = new Intent(this,ViewCarActivity.class);
        intent.putExtra(getString(R.string.CAR_ID),carID);
        startActivity(intent);
    }


    private void setViews(){
        startTxt = (TextView)findViewById(R.id.create_rent_request_start_text);
        endTxt = (TextView)findViewById(R.id.create_rent_request_end_text);
        priceToPayTxt = (TextView)findViewById(R.id.create_rent_request_price);
        carNameTitle = (TextView)findViewById(R.id.create_rent_request_name);
    }




    private void initDatePicker(){

        final boolean[] areDatesPicked = {false,false};
        final Calendar today = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startTxt.setText(date);
                selectedStart = date;
                areDatesPicked[0] = true;// mark start date as picked

                // Set minimum date for end date picker to the day after the selected start date
                Calendar startDate = Calendar.getInstance();
                startDate.set(year, month - 1, day);
                startDate.add(Calendar.DAY_OF_MONTH, 1);
                endDatePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());

                if(areDatesPicked[1]){//other date was also selected
                    calculatePrice(selectedStart,selectedEnd);
                }

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
                selectedEnd = date;
                areDatesPicked[1] = true;

                // Set maximum date for start date picker to the day before the selected end date
                Calendar endDate = Calendar.getInstance();
                endDate.set(year, month - 1, day);
                endDate.add(Calendar.DAY_OF_MONTH, -1);
                startDatePickerDialog.getDatePicker().setMaxDate(endDate.getTimeInMillis());

                if(areDatesPicked[0]){//other date was also selected
                    calculatePrice(selectedStart,selectedEnd);
                }
            }
        };

        //obtaining todays date
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        //initialize date pickers (having todays date when opening)
        startDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, startDateSetListener, year, month, day);
        endDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, endDateSetListener, year, month, day);

        //set that the minimum allowed date to select is today
        today.add(Calendar.DAY_OF_MONTH, 1);
        startDatePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        endDatePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());

    }



    private String makeDateString(int day, int month, int year) {
        // Ensure day and month are always two digits
        String dayStr = (day < 10) ? "0" + day : String.valueOf(day);
        String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + monthStr + "-" + dayStr;
    }



    private void calculatePrice(String start, String end){
        Date startDate = null;
        Date endDate =null;
        try {
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        //get days in between these selected dates
        long days = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            days = ChronoUnit.DAYS.between(startDate.toInstant(),endDate.toInstant());
        }

        System.out.println(days);
        int calPrice = ((int)days) * carPrice;//new price
        priceToPayTxt.setText(calPrice + "");//setting the amount

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



