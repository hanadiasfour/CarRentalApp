package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCarActivity extends AppCompatActivity {


    private ImageView carImg, ownerImg ;
    private TextView carName, price,description,location,ownerName,ownerNumber;
    private String carID,ownerGmail;
    private final String GET_SPECIFIC_CAR_URL = "http://10.0.2.2/api/get_specific_car_and_owner_h.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_car);
        setViews();



        Intent givenIntent = getIntent();//obtain the intent that invoked this activity
        handleIntent(givenIntent);


        String constructedUrl = GET_SPECIFIC_CAR_URL + "?carID=" + carID;
        System.out.println(constructedUrl);
        getData(constructedUrl);


    }

    private void handleIntent(Intent intent){


        if(intent.hasExtra(getString(R.string.CAR_ID))) {
            carID = intent.getStringExtra(getString(R.string.CAR_ID));
        }

    }


    private void setViews(){
        carImg = (ImageView)findViewById(R.id.view_car_image);
        ownerImg = (ImageView)findViewById(R.id.view_car_owner_pic);
        ownerName = (TextView)findViewById(R.id.view_car_owner_name);
        ownerNumber = (TextView)findViewById(R.id.view_car_owner_number);
        carName = (TextView)findViewById(R.id.view_car_name);
        price = (TextView)findViewById(R.id.view_car_price);
        description = (TextView)findViewById(R.id.view_car_info);
        location = (TextView)findViewById(R.id.view_car_location);

    }


    private void getData(String url){

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        JSONObject obj = response.getJSONObject(0);

                        String location = obj.getString("location");
                        String carImg = obj.getString("picture");
                        int price =obj.getInt("price_per_day");
                        String info =obj.getString("description");
                        String carName = obj.getString("name");
                        ownerGmail = obj.getString("owner");
                        String ownerName = obj.getString("owner_name");
                        int phone = obj.getInt("owner_phone");
                        String ownerImg = obj.getString("owner_photo");

                        setInfo(location,carImg,price,info,carName,ownerName,phone,ownerImg);



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



    //given the parameter info obtained from the backend database, set teh views to these variables
    private void setInfo(String loc,String cImg,int price,String info,String cName,String oName, int phone,String oImg){

        //using glide to load the link images to image views
        Glide.with(this).load(cImg).into(carImg);
        Glide.with(this).load(oImg).into(ownerImg);

        //setting the text views with the correlated values obtained from backend
        this.price.setText(price+"");
        this.description.setText(info);
        this.carName.setText(cName);
        this.location.setText(loc);
        ownerNumber.setText("0"+phone);
        ownerName.setText(oName);

    }

    public void setDetailsToRentCar(View view){

        Intent intent = new Intent(this,CreateRentRequestActivity.class);
        startActivity(intent);

    }

    public void backToBrowse(View view){
Intent intent = new Intent(this,BrowseCarsActivity.class);
startActivity(intent);
finish();



    }
}