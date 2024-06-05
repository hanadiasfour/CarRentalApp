package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private CCI_RecyclerViewAdapter CCI_adapter;
    private final String GET_DATA_URL = "http://10.0.2.2/api/get_all_cars_h.php";
    private final String GET_SEARCHED_URL = "http://10.0.2.2/api/get_searched_car_h.php";
    private SearchView searchView;
    private final int BROWSE_TO_FILTER = 121;
//    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;


    //variables to keep track of what filters were added
    private String location = "";
    private String minPrice = "0";
    private String maxPrice = "2000";
    private String sortOrder = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);

//        //initializing the shared pref. with the special key nad its editor
//        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_KEY), Context.MODE_PRIVATE);
//        edit = sharedPreferences.edit();

        handelTabSwitch();// to handle switching the tabs
        setViews();// to initiate the view objects
        setCatalogData(GET_DATA_URL);// fill the recyclerView with all items of database
        CCI_RV.setLayoutManager(new LinearLayoutManager(this));//setting the layout of the recyclerView


        // SearchView listener for changing content of recyclerView when searching
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
        searchView = (SearchView) findViewById(R.id.searchView);
//        progressBar = findViewById(R.id.progressBar);


    }

    public void openFilters(View view){

        //creating and filling the bundle
        Bundle prevFilters = new Bundle();
        prevFilters.putString(getString(R.string.LOC),location);
        prevFilters.putString(getString(R.string.LOW),minPrice);
        prevFilters.putString(getString(R.string.HIGH),maxPrice);
        prevFilters.putString(getString(R.string.ORD),sortOrder);

        //creating intent
        Intent openFilter = new Intent(this, FilterActivity.class);
        openFilter.putExtras(prevFilters);//adding bundle to intent
        startActivityForResult(openFilter,BROWSE_TO_FILTER);//invoking the filter activity

    }

    @Override
    protected void onActivityResult (int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BROWSE_TO_FILTER && resultCode == RESULT_OK && data != null) {
            String returnedURL = data.getStringExtra(getString(R.string.FILTER_CONSTRUCTED_URL));
            setCatalogData(returnedURL);//setting the recyclerVIew to the filtered date
            dissectUrlForVars(returnedURL);//assigning the variables with the set filters to save for later
        }
    }

    public void dissectUrlForVars(String url) {
        // removing the main first part of URL
        String paramsLine = url.split("\\?")[1];

        // splitting  by & to get the variables
        String[] params = paramsLine.split("&");

        //initializing the variables as string name "null"
        location = "";
        minPrice = "0";
        maxPrice = "2000";
        sortOrder = "";

        //looping all of the coupled parameters + values to assign them to the correct variables
        for (String param : params) {
            String[] pair = param.split("=");//splitting couple of parameter + value by "="

            String varName = pair[0];//var name
            String value = pair.length > 1 ? pair[1] : "";//value

            // assigning the values or null if they are empty
            switch (varName) {
                case "location":
                    location = value.isEmpty() ? "" : value;
                    break;
                case "minPrice":
                    minPrice = value.isEmpty() ? "0" : value;
                    break;
                case "maxPrice":
                    maxPrice = value.isEmpty() ? "2000" : value;
                    break;
                case "sortOrder":
                    sortOrder = value.isEmpty() ? "" : value;
                    break;
            }
        }

    }


    public void fetchData(String url){

//        // showing the progress bar
//        progressBar.setVisibility(View.VISIBLE);


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


    public void setCatalogData(String url){


        // Show the progress bar
//        progressBar.setVisibility(View.VISIBLE);

                fetchData(url);

//        progressBar.setVisibility(View.INVISIBLE);
    }







    private void performSearch(String query) {
        if (query.isEmpty()) {
            setCatalogData(GET_DATA_URL); // Load all data
        } else {
            String newURL = GET_SEARCHED_URL+"?criteria=" + query;
            setCatalogData(newURL); // Load filtered data
        }
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