package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

    private ArrayList<CarCatalogItemModel> catalogList = new ArrayList<>();//to hold the cars which appear in catalog
    private RecyclerView CCI_RV;
//    private EditText searchBox;
    private CCI_RecyclerViewAdapter CCI_adapter;
    private final String GET_DATA_URL = "http://10.0.2.2/api/get_all_cars_h.php";
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);
        handelTabSwitch();// to handle switching the tabs
        setViews();// to initiate the view objects
        setCatalogData(GET_DATA_URL);// fill the recyclerView with all items of database
        CCI_RV.setLayoutManager(new LinearLayoutManager(this));//setting the layout of the recyclerView



        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });

    }

    private void setViews(){

        CCI_RV = (RecyclerView)findViewById(R.id.CCI_RecyclerView);
        searchView = findViewById(R.id.searchView);

//        searchBox = (EditText)findViewById(R.id.CC_searchBox);

    }

    public void openFilters(View view){
        startActivity(new Intent(this, FilterActivity.class));

    }


    public void setCatalogData(String url){
        catalogList = new ArrayList<>();// to hold the data for browsing

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        catalogList.add(new CarCatalogItemModel(obj.getString("car_number"),obj.getString("picture"),obj.getInt("price_per_day"),obj.getString("name")));
                    }catch(JSONException exception){
                        Log.d("volley_error1", exception.toString());
                    }

                }


                if (CCI_adapter == null) {
                    CCI_adapter = new CCI_RecyclerViewAdapter(BrowseCarsActivity.this, catalogList);
                    CCI_RV.setAdapter(CCI_adapter);
                } else {
                    CCI_adapter.setFiltered(catalogList);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error2", error.toString());
            }
        });


        Volley.newRequestQueue(BrowseCarsActivity.this).add(request);


    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("LifeCycle","New Intent in Browse");


    }


    private void performSearch(String query) {
        if (query.isEmpty()) {
            setCatalogData(GET_DATA_URL); // Load all data
        } else {
            String newURL = "http://10.0.2.2/api/get_searched_car_h.php?criteria=" + query;
            setCatalogData(newURL); // Load filtered data
        }
    }


    //when pressing the search button, this method fills recyclerView with refined results according to the specified criteria
//    public void searchBtn(View view){
//
//        String searchCriteria = searchBox.getText().toString();//obtain the searching criteria
//
//        if(searchCriteria.isEmpty()){//when nothing put into the search box
//            setCatalogData(GET_DATA_URL);//fill with all information
//
//        }else{//all inputs are fine
//            String newURL = GET_DATA_URL +"?criteria=" + searchCriteria;//construct URL
//            setCatalogData(newURL);// invoke method to obtain and display results
//        }
//
//
//    }


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

}