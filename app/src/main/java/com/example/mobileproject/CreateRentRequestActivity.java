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
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private String USER_KEY ,carID, selectedStart,selectedEnd,cName,context="";
    private int carPrice =0;
    private boolean isError = false;
    private  SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");


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


    ////////////////////////////////////////////////
    // OPEN A WARNING DIALOG THAT SAYS WHATS WRONG//
    ////////////////////////////////////////////////
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



    //performed when send request button is pressed
    //sends request if no errors or conflicts occur
    public void sendRentRequest(View view){

        //only one date or non of the dates were selected
        // show alert informing them about their mistake
        if(selectedStart == null||selectedEnd == null){
            showAlertDialog("Notice!","You must select a date range first to proceed with this action.");
            return;
        }

        // showing the progress dialog to inform the user that some action is being done
        progressDialog = ProgressDialog.show(this, "Hold on Tightly!", "Your Request is Being Processed ...", true);

        insertIfNoConflicts();// inserts the request if no conflicts were found

        //using handler to give a delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!isError){//no error was detected (added request in db)
                    showAlertDialog("Success!",context);

                }else{//error occurred (failed to add to db)
                    showAlertDialog("Failed!",context);
                }

                // turn off the loading dialog (actions are done)
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, 1000); // 1 second delay to show that something is happening
    }


    /*
    * accesses the backend db through php file using volley (api)
    * inserts the car rental request if no conflict found in db
    *
    * */
    private void insertIfNoConflicts() {

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.INSERT_REQUEST_IF_VALID_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // updating the status of the backend post request access through api
                    isError = jsonObject.getBoolean("error");
                    context = jsonObject.getString("message");

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
                //  passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            //maps the data to the variables in the backend
            @Override
            protected Map<String, String> getParams() {
                //creating map key and value pair
                Map<String, String> params = new HashMap<>();

                // passing the key and value pairs parameters
                params.put("start", selectedStart);
                params.put("end", selectedEnd);
                params.put("renter_gmail", USER_KEY);
                params.put("car_number", carID);

                //returning the params
                return params;
            }
        };

        Volley.newRequestQueue(CreateRentRequestActivity.this).add(request);

    }

    /*
    * this method initiates and sets the listeners on the start and end date pickers
    * also adds constraints and triggers the method to calculated the price required for this renting req
    *
    * */
    private void initDatePicker(){

        final boolean[] areDatesPicked = {false,false};//to keep track of what dates were picked so far

        //start date listener
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;// adding one since its zero based
                String date = makeDateString(day, month, year);//constructing string representation of date
                startTxt.setText(date);//setting text
                selectedStart = date;// marking the selected start date
                areDatesPicked[0] = true;// mark start date as picked

                // setting the minimum date for end date picker to the day after the selected start date
                //creating the date instance that represents one day after selected start day
                Calendar startDate = Calendar.getInstance();
                startDate.set(year, month - 1, day);
                startDate.add(Calendar.DAY_OF_MONTH, 1);

                //restricting the end date picker
                endDatePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());

                if(areDatesPicked[1]){//if the other date was also selected
                    calculatePrice(selectedStart,selectedEnd);//time to calculate the total price to pay
                }

            }
        };

        //end date listener
        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;// adding one since its zero based
                String date = makeDateString(day, month, year);//constructing string representation of date
                endTxt.setText(date);
                selectedEnd = date;
                areDatesPicked[1] = true;// mark end date as picked

                // setting the maximum date for start date picker to the day before the selected end date
                //creating the date instance that represents one day before selected start day
                Calendar endDate = Calendar.getInstance();
                endDate.set(year, month - 1, day);
                endDate.add(Calendar.DAY_OF_MONTH, -1);
                startDatePickerDialog.getDatePicker().setMaxDate(endDate.getTimeInMillis());

                if(areDatesPicked[0]){//other date was also selected
                    calculatePrice(selectedStart,selectedEnd);//time to calculate the total price to pay
                }
            }
        };

        //obtaining todays date
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        //initialize date pickers (having todays date when opening any for the first time)
        startDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, startDateSetListener, year, month, day);
        endDatePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, endDateSetListener, year, month, day);

        //set that the minimum allowed date to select is today
        today.add(Calendar.DAY_OF_MONTH, 1);
        startDatePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        endDatePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
    }


    /*
     * given the start and end dates chosen by the user, date objects are created and
     * the number of days in between is found then multiplied by the price of the car per day
     */
    private void calculatePrice(String start, String end){

        //getting selected dates as date object from a certain format
        Date startDate = null;
        Date endDate =null;
        try {
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            Log.e("MY_ERROR","Problem with parsing");
        }


        // get days in between these selected dates
        long days = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            days = ChronoUnit.DAYS.between(startDate.toInstant(),endDate.toInstant());
        }

        int calPrice = ((int)days) * carPrice;//new price
        priceToPayTxt.setText(calPrice + "");//setting the amount
    }


    //given the day, month, year, the date string is made in a certain format
    private String makeDateString(int day, int month, int year) {
        // making sure the day and month are always two digits
        String d = (day < 10) ? "0" + day : String.valueOf(day);
        String m = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + m + "-" + d;//separate with -
    }

    //actions performed when clicking on the calendar buttons to open the date picker dialog
    public void openStartDatePicker(View view)
    {
        startDatePickerDialog.show();
    }

    public void openEndDatePicker(View view)
    {
        endDatePickerDialog.show();
    }


    //initialize views
    private void setViews(){
        startTxt = (TextView)findViewById(R.id.create_rent_request_start_text);
        endTxt = (TextView)findViewById(R.id.create_rent_request_end_text);
        priceToPayTxt = (TextView)findViewById(R.id.create_rent_request_price);
        carNameTitle = (TextView)findViewById(R.id.create_rent_request_name);
    }


    // back button that leads to the view car details activity
    // collects and sends the relevant information needed for the previous activity
    public void backToViewCarDetails(View view){
        Intent intent = new Intent(this,ViewCarActivity.class);
        intent.putExtra(getString(R.string.CAR_ID),carID);// to reload the information of the selected car
        intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//keep track of the person logged in
        startActivity(intent);// invoking the activity
    }

}



