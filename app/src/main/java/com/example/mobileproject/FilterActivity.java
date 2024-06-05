package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.net.URLEncoder;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private RadioGroup orderGroup;
    private RadioButton ascBtn,descBtn;
    private RangeSlider priceSlider;
    private String GET_Filtered_URL = "http://10.0.2.2/api/get_filtered_cars_h.php";

    private String[] spinnerValuesArray;
    private String location = "";
    private String minPrice = "0";
    private String maxPrice = "2000";
    private String sortOrder = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        setViews();//initializing the views
        setSpinner();//initializing the spinner
        Intent sentIntent = getIntent();//getting intent which invoked this activity
        Bundle sentBundle = sentIntent.getExtras();//extracting the bundle in the intent


        //getting data from bundle
        if(sentBundle != null){
        location = sentBundle.getString(getString(R.string.LOC));
        minPrice = sentBundle.getString(getString(R.string.LOW));
        maxPrice = sentBundle.getString(getString(R.string.HIGH));
        sortOrder = sentBundle.getString(getString(R.string.ORD));
        }

        setFilterValues();//sets the values of the filters according to any previously used filters
    }


    //this method gives the initial value of the filters in case it was previously set
    private void setFilterValues(){

        locationSpinner.setSelection(getIndex(location));//setting location selection
        priceSlider.setValues(Float.parseFloat(minPrice), Float.parseFloat(maxPrice));//setting slider

        if(sortOrder.isEmpty()){// no order was specified
            orderGroup.clearCheck();//clears all selections

        }else if(sortOrder.equals("ASC")){//ascending was previously checked
            orderGroup.clearCheck();//clears all selections
            orderGroup.check(R.id.price_asc_button);

        }else{//descending was previously checked
            orderGroup.clearCheck();//clears all selections
            orderGroup.check(R.id.price_dsc_button);
        }
    }


    //given the location name, return its index in the spinner array
    private int getIndex(String loc){

        //loop the array and find the index of the matched location name
        for(int i =0;i<spinnerValuesArray.length;i++){
            if(spinnerValuesArray[i].equals(loc))
                return i;//return the found index
        }
        return 0; //not found in spinner (impossible case)
    }


    //initializes the views with their ids
    private void setViews(){
        orderGroup = (RadioGroup) findViewById(R.id.price_sorting_group);
        ascBtn = (RadioButton) findViewById(R.id.price_asc_button);
        descBtn = (RadioButton)findViewById(R.id.price_dsc_button);
        locationSpinner = (Spinner)findViewById(R.id.location_spinner_filter);
        priceSlider =(RangeSlider)findViewById(R.id.range_slider_filter);

    }


    //on click action invoked by the apply button
    // takes the filter inputs from the layout views and creates a url to access the api
    public void applyFilters(View view){

        ////////////////////////////////////
        /////getting selected location/////
        ////////////////////////////////////
        String selectedLoc = locationSpinner.getSelectedItem().toString();
        if(selectedLoc.equals("Select")){//default choice (nothing was selected)
            selectedLoc="";//empty the location string
        }

        //////////////////////////////////////
        /////getting selected price range/////
        //////////////////////////////////////
        //get set range from slider
        List<Float> values = priceSlider.getValues();
        int lowerBound = Math.round(values.get(0));
        int upperBound = Math.round(values.get(1));


        ///////////////////////////////////
        /////getting selected ordering/////
        ///////////////////////////////////
        String order =""; //to hold DESC or ASC
        int selectedID = orderGroup.getCheckedRadioButtonId();//get id of selected radio button

        if(selectedID != -1){//something was selected

            //getting the view associated with this id
            RadioButton selectedOrder = (RadioButton) findViewById(selectedID);
            String value =selectedOrder.getText().toString();//button label

            if(value.equals(getString(R.string.price_asc_label))){//ascending
                order = "ASC";

            }else{//descending
                order = "DESC";
            }


        }

        //obtaining the url to invoke the backend api
        String constructedURL = getConstructedURL(selectedLoc,lowerBound,upperBound,order);


        //sending back result url through intent to the Browsing activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.FILTER_CONSTRUCTED_URL), constructedURL);
        setResult(RESULT_OK, returnIntent);
        finish();



    }


    //constructs the url to invoke the backend api to filter cars
    private String getConstructedURL(String location,int lower,int upper, String order){

        String constructedURL = GET_Filtered_URL+"?";
        constructedURL += "location=" + location;
        constructedURL += "&minPrice=" + lower;
        constructedURL += "&maxPrice=" + upper +"&sortOrder=" + order;

        return constructedURL;

    }


    //clears the filters set (rest all views to initial values)
    public void clearFilters(View view){
        locationSpinner.setSelection(0);
        priceSlider.setValues((float) 0, (float)2000);
        orderGroup.clearCheck();//clears all selections

    }

    //initializes and occupies the spinner with the values for the locations
    private void setSpinner(){

        //initializing an array with the values set in the array string resources
        spinnerValuesArray =getResources().getStringArray(R.array.location_values);
        // creating ArrayAdapter using the string array and a special spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, spinnerValuesArray);

        // setting the adapter to the spinner
        locationSpinner.setAdapter(adapter);

    }


}