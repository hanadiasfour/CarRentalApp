package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrowseCarsActivity extends AppCompatActivity {

    private ArrayList<CarCatalogItemModel> catalogList = new ArrayList<>();
    private RecyclerView CCI_RV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);
        handelTabSwitch();


        CCI_RV = (RecyclerView)findViewById(R.id.CCI_RecyclerView);
        setCatalogData();

        CCI_RV.setLayoutManager(new LinearLayoutManager(this));


    }


    public void openFilters(View view){
        startActivity(new Intent(this, FilterActivity.class));

    }


    public void setCatalogData(){

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2/api/get_all_cars_h.php",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        catalogList.add(new CarCatalogItemModel(obj.getString("car_number"),obj.getString("picture"),obj.getInt("price_per_day"),obj.getString("name")));
                    }catch(JSONException exception){
                        Log.d("volley_error", exception.toString());
                    }
                    Log.d("volley_done", "read it");

                }
                CCI_RecyclerViewAdapter CCI_adapter = new CCI_RecyclerViewAdapter(BrowseCarsActivity.this,catalogList);
                CCI_RV.setAdapter(CCI_adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error1", error.toString());
            }
        });


        Volley.newRequestQueue(BrowseCarsActivity.this).add(request);


    }



    public void handelTabSwitch(){

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.browse_cars_nav_item);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.my_cars_nav_item) {
                    startActivity(new Intent(getApplicationContext(), MyCarsActivity.class));
                    overridePendingTransition(0, 0);

                    return true;
                }else if (id == R.id.browse_cars_nav_item) {
                    return true;
                } else if (id == R.id.settings_nav_item){
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    overridePendingTransition(0, 0);

                    return true;
                }else if(id == R.id.renting_nav_item){

                    startActivity(new Intent(getApplicationContext(), RentingActivity.class));
                    overridePendingTransition(0, 0);


                }

                return false;
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("LifeCycle","New Intent in Browse");


    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle","on pause browse");

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","on stop browse");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle","on Destroy browse");

    }


}