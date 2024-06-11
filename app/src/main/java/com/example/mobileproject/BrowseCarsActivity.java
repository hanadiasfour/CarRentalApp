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
import android.widget.TextView;

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

    //views as global variables
    private RecyclerView CCI_RV;
    private TextView emptyView;
    private SearchView searchView;
    private BottomNavigationView bottomNav;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    //adapters and lists for recycler view
    private CCI_RecyclerViewAdapter CCI_adapter;
    private ArrayList<CarCatalogItemModel> catalogList = new ArrayList<>();

    //variables to keep track of what filters were added
    private String location = "",minPrice = "0", maxPrice = "2000",sortOrder = "", contructedFilterURL=null;

    private String USER_KEY ="";//should be changed depending on the user that logged in
    private final int BROWSE_TO_FILTER = 121;// key for callback methods when using startActivityForResult



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_cars);
        setViews();// to initiate the view objects

        if (isFirstLaunch()) {//(it was?) well, now its not the first launch

            System.out.println("ITS THE FIRST TIME EVER");
            setCatalogData(getString(R.string.GET_DATA_URL));
            editor.putBoolean(getString(R.string.PREF_FIRST_LAUNCH), false);
            editor.apply();

        }else{
            System.out.println("NOT FIRST TIME");

            String URL = getfilteredURLFromSHP();//getting filtered url from shared preferences
            System.out.println(URL);

            // fill the recyclerView with all items of database if no filters were ever applied earlier
            if(URL != null){
                System.out.println("Not null");
                setCatalogData(URL);
                dissectUrlForVars(URL);
            }else{
                System.out.println("was null");
                setCatalogData(getString(R.string.GET_DATA_URL));
            }

        }

        //setting the layout of the recyclerView
        CCI_RV.setLayoutManager(new LinearLayoutManager(this));


        //takes the gmail in which this user logged in with
        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.USER_KEY))){//get key from intents triggering this activity
            USER_KEY = intent.getStringExtra(getString(R.string.USER_KEY));

        }else {//get intent form shared preferences in-case activity was destroyed after being interrupted
            USER_KEY = getUserKeyFromSHP();
        }

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

        handelTabSwitch();// to handle switching the tabs
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","on stop browse");
        saveInfoToSHP();//saving user key and filters added
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROYED");
        editor.clear();
        editor.apply();
    }

    // given the url, access the back-end and obtain the appropriate information using an API
    public void setCatalogData(String url){

        catalogList = new ArrayList<>();// to hold the data of cars

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

                if (CCI_adapter == null) {// first time adapter is used/initialized
                    CCI_adapter = new CCI_RecyclerViewAdapter(BrowseCarsActivity.this, catalogList, USER_KEY);
                    CCI_RV.setAdapter(CCI_adapter);

                } else {// just manipulate the existing adapter
                    CCI_adapter.setFiltered(catalogList);
                }

                checkIfEmpty();// checks if the resulted filtered list is empty to present with message
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error2", error.toString());
            }
        });

        // adding the request to be performed by volley
        Volley.newRequestQueue(BrowseCarsActivity.this).add(request);
    }


    public void handelTabSwitch(){

        // set this browsing tab as selected
        bottomNav.setSelectedItemId(R.id.browse_cars_nav_item);

        // adding item selected listener
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();//tab that was selected

                if (id == R.id.my_cars_nav_item) {//my cars activity
                    Intent intent = new Intent(getApplicationContext(), MyCarsActivity.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    return true;
                }else if (id == R.id.browse_cars_nav_item) {
                    return true;
                } else if (id == R.id.settings_nav_item){

                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    return true;
                }else if(id == R.id.renting_nav_item){

                    Intent intent = new Intent(getApplicationContext(), RentingActivity.class);
                    intent.putExtra(getString(R.string.USER_KEY),USER_KEY);//sending the logged in user
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                return false;
            }
        });
    }


    // invoke the filtering action appropriate to the given the typed search criteria
    private void performSearch(String criteria) {
        if (criteria.isEmpty()) {//nothing typed
            setCatalogData(getString(R.string.GET_DATA_URL)); // load all data

        } else {
            String newURL = getString(R.string.GET_SEARCHED_URL) +"?criteria=" + criteria;
            setCatalogData(newURL); // load filtered data using the criteria given
        }
    }

    /*
     * starts the activity for adding filters
     * sends what ever filter status was previously applied
     * this way, we don't have to restart the filters from nothing
     */
    public void openFilters(View view){

        Intent openFilter = new Intent(this, FilterActivity.class);//creating intent
        openFilter.putExtras(fillBundleWithFilterInfo());//adding bundle to intent
        startActivityForResult(openFilter,BROWSE_TO_FILTER);//invoking the filter activity
    }


    private Bundle fillBundleWithFilterInfo(){
        //creating and filling the bundle with what ever filter information was applied earlier
        Bundle prevFilters = new Bundle();
        prevFilters.putString(getString(R.string.LOC),location);
        prevFilters.putString(getString(R.string.LOW),minPrice);
        prevFilters.putString(getString(R.string.HIGH),maxPrice);
        prevFilters.putString(getString(R.string.ORD),sortOrder);
        return prevFilters;
    }

    //call back method to accept result of chosen filters from the other activity
    @Override
    protected void onActivityResult (int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BROWSE_TO_FILTER && resultCode == RESULT_OK && data != null) {
            contructedFilterURL = data.getStringExtra(getString(R.string.FILTER_CONSTRUCTED_URL));//constructed url with vars
            setCatalogData(contructedFilterURL);//setting the recyclerVIew to the filtered date
            dissectUrlForVars(contructedFilterURL);//assigning the variables with the set filters to save for later
        }
    }

    /*
     *  this method takes in the constructed url from the filter activity
     *  then dissects it for the set values of the variables  to filter on
     *   this is used for when the user wants to reopen the filters and find their old selections
     * */
    public void dissectUrlForVars(String url) {
        // removing the unwanted first part of URL
        String paramsLine = url.split("\\?")[1];

        // splitting by & to get the variables
        String[] params = paramsLine.split("&");

        //initializing the variables
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


    /* checks if the list of cars generated by accessing the backend and applying filters is empty
     * used for showing and hiding a message about how there are no matching results
     */
    private void checkIfEmpty() {
        if (catalogList.isEmpty()) {
            CCI_RV.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        } else {
            CCI_RV.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    //initiates the views
    private void setViews(){
        CCI_RV = (RecyclerView)findViewById(R.id.CCI_RecyclerView);
        searchView = (SearchView) findViewById(R.id.searchView);
        emptyView = (TextView) findViewById(R.id.emptyView);
        bottomNav =(BottomNavigationView) findViewById(R.id.bottom_navigation);
        sharedPref = getSharedPreferences(getString(R.string.MY_PREF_KEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void saveInfoToSHP() {
        //adding user key
        editor.putString(getString(R.string.USER_KEY), USER_KEY);
        editor.putString(getString(R.string.FilterConstructedURL),contructedFilterURL);
        editor.putBoolean(getString(R.string.PREF_FIRST_LAUNCH), false);//mark as not the first launch
        editor.apply(); // saving asynchronously using apply
    }

    public String getUserKeyFromSHP() {
        // setting a default value if no value is found
        return sharedPref.getString(getString(R.string.USER_KEY), "fatenWoman@gmail.com");
    }

    public String getfilteredURLFromSHP() {
        // setting a default value if no value is found
        return sharedPref.getString(getString(R.string.FilterConstructedURL), null);
    }

    public boolean isFirstLaunch(){
        return sharedPref.getBoolean(getString(R.string.PREF_FIRST_LAUNCH), true);
    }

}