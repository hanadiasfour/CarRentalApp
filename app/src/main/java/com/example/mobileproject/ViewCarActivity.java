package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCarActivity extends AppCompatActivity {


    // views
    private ImageView carImg, ownerImg ;
    private TextView carName, price,description,location,ownerName,ownerNumber;
    private String carID,ownerGmail,cName,USER_KEY ="";
    private int carPrice;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_car);
        setViews();
        handleIntent(getIntent());


        //creating the url that will obtain the info of a certain car from db
        String constructedUrl = getString(R.string.GET_SPECIFIC_CAR_URL) + "?carID=" + carID;
        getData(constructedUrl);// get data from backend using API


        //action to dial number of owner
        RelativeLayout relativeLayout = findViewById(R.id.owner_layout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:"+ ownerNumber.getText().toString();
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse(phoneNumber));
                startActivity(dialIntent);
            }
        });


    }


    private void handleIntent(Intent intent){

        if(intent !=null) {
            carID = intent.getStringExtra(getString(R.string.CAR_ID));
            USER_KEY = intent.getStringExtra(getString(R.string.USER_KEY));
        }else{
            USER_KEY = sharedPref.getString(getString(R.string.USER_KEY),null);
            carID = sharedPref.getString(getString(R.string.CAR_ID),null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","on stop browse");
        editor.putString(getString(R.string.USER_KEY), USER_KEY);
        editor.putString(getString(R.string.CAR_ID),carID);
        editor.apply(); // saving asynchronously using apply
    }


/*
* given the constructed url, the backend db is accessed using an php api
* the data obtained from the db is then presented to the user through the views
* */
    private void getData(String url){

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        JSONObject obj = response.getJSONObject(0);

                        //extracting info from JSON object from backend
                        String location = obj.getString("location");
                        String carImg = obj.getString("picture");
                        carPrice = obj.getInt("price_per_day");
                        String info =obj.getString("description");
                        cName = obj.getString("name");
                        ownerGmail = obj.getString("owner");
                        String ownerName = obj.getString("owner_name");
                        int phone = obj.getInt("owner_phone");
                        String ownerImg = obj.getString("owner_photo");

                        //setting views info
                        setInfo(location,carImg,carPrice,info,cName,ownerName,phone,ownerImg);

                    }catch(JSONException exception){
                        Log.d("volley_error1", exception.toString());
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error2", error.toString());
            }
        });

        Volley.newRequestQueue(this).add(request);
    }



    //given info obtained from the backend db, set the views to these variables to present to the user
    private void setInfo(String loc,String cImg,int price,String info,String cName,String oName, int phone,String oImg){

        // to show a placeholder while loading images
        RequestOptions reqOp = new RequestOptions()
                .placeholder(R.drawable.loading_dots) // the placeholder image
                .error(R.drawable.ic_launcher_foreground); // the error image

        //using glide to load the link images to image views
        Glide.with(this).setDefaultRequestOptions(reqOp).load(cImg).into(carImg);
        Glide.with(this).setDefaultRequestOptions(reqOp).load(oImg).into(ownerImg);

        //setting the text views with the correlated values obtained from backend
        this.price.setText(price+"");
        this.description.setText(info);
        this.carName.setText(cName);
        this.location.setText(loc);
        ownerNumber.setText("0"+phone);
        ownerName.setText(oName);

    }

    /*
    * action performed when choosing to rent the selected car
    * saves the important data needed for the next activity into a intent
    * invokes the next activity(for creating a new request)
    * */
    public void setDetailsToRentCar(View view){
        Intent intent = new Intent(this,CreateRentRequestActivity.class);
        intent.putExtra(getString(R.string.USER_KEY),USER_KEY);
        intent.putExtra(getString(R.string.CAR_ID),carID);
        intent.putExtra(getString(R.string.CAR_PRICE),carPrice);
        intent.putExtra(getString(R.string.CAR_NAME),cName);
        startActivity(intent);
    }

    //back button action that returns to browsing screen activity
    public void backToBrowse(View view){
    Intent intent = new Intent(this,BrowseCarsActivity.class);
    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);// keep track of this person that logged in
    startActivity(intent);
    finish();// destroy this activity
    }

    //initializes views
    private void setViews(){
        carImg = (ImageView)findViewById(R.id.view_car_image);
        ownerImg = (ImageView)findViewById(R.id.view_car_owner_pic);
        ownerName = (TextView)findViewById(R.id.view_car_owner_name);
        ownerNumber = (TextView)findViewById(R.id.view_car_owner_number);
        carName = (TextView)findViewById(R.id.view_car_name);
        price = (TextView)findViewById(R.id.view_car_price);
        description = (TextView)findViewById(R.id.view_car_info);
        location = (TextView)findViewById(R.id.view_car_location);

        sharedPref = getSharedPreferences(getString(R.string.MY_PREF_KEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
}