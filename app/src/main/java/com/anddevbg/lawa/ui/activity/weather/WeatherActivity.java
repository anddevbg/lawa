package com.anddevbg.lawa.ui.activity.weather;


import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.animation.ZoomPagerTransformation;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

//    private static final String WEATHER_ARRAY = "weather_array";
//    private static final String CITY_TABLE_NAME = "city_information_table";
//
//    private static final String DATABASE_NAME = "city_databse";
//    private static final String TABLE_NAME = "city_table";
//    private static final String CITY_NAME = "city_name";
//    private static final int DATABASE_VERSION = 1;
//    private static final String UID = "_id";
//    private CityTable mCityTable;

    private WeatherFragmentAdapter mWeatherAdapter;
    private ViewPager viewPager;
    private List<WeatherData> result;
    private int search_request_code = 1;
    private SearchView searchView;
    private double mLocationLatitude;
    private double mLocationLongitude;
    private String mCityName;
    private WeatherData weatherData;
    private SQLiteDatabase sqLiteDatabase;

    private Location myLastLocation;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private WeatherDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
//        mCityTable = new CityTable(this);

        result = new ArrayList<>();

        setUpGoogleApiClient();
        initControls();
        getManagerAndShowData();
//        getInformationFromSQLiteDatabase();

    }

    private void getManagerAndShowData() {
        manager = WeatherDatabaseManager.getInstance();
        sqLiteDatabase = manager.getDatabase();
        result = manager.showAll();
        Log.d("db", "result array list is: "+result);
        mWeatherAdapter.setWeatherData(result);
    }

    private void setUpGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onSearchRequested() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra("name", searchView.getQuery());
        startActivity(i);
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));
        Button add = (Button) menu.findItem(R.id.action_add).getActionView();
        Button remove = (Button) menu.findItem(R.id.action_remove).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("asd", "action add clicked");
                Intent searchActivityIntent = new Intent(WeatherActivity.this, SearchCityActivity.class);
                startActivityForResult(searchActivityIntent, search_request_code);
                break;
            case R.id.action_remove:
                Log.d("asd", "action remove clicked");
                int index = viewPager.getCurrentItem();
                String cityNameForDeletion = result.get(index).getCityName();
                Log.d("asd", "name is: " + cityNameForDeletion);
                manager.deleteData(cityNameForDeletion);
                mWeatherAdapter.removeView(index);
                result.remove(index);
                mWeatherAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == search_request_code) {
            if (resultCode == RESULT_OK) {
                Log.d("asd", "in weather activity result " + data.getStringExtra("c1name"));
                String locationName = data.getStringExtra("c1name");
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(locationName, 1);
                    Address address = addresses.get(0);
                    mLocationLongitude = address.getLongitude();
                    mLocationLatitude = address.getLatitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WeatherData data1 = new WeatherData();
                data1.setLongitude(mLocationLongitude);
                data1.setLatitude(mLocationLatitude);
                data1.setCityName(locationName);
                result.add(data1);
                mWeatherAdapter.setWeatherData(result);
                viewPager.setCurrentItem(result.size());
                Log.d("asd", "data1 id is: " + locationName);
                manager.insertData(locationName, mLocationLatitude, mLocationLongitude);
            }
        }
    }

    private void initControls() {
        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setPageTransformer(false, new ZoomPagerTransformation());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("asd", "connected to google api");
//        if (result.size() == 0) {
//            handleWeatherInformation();
//        }
    }

//    private void handleWeatherInformation() {
//        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(1000);
//        WeatherData weatherDat4 = new WeatherData();
//        if (myLastLocation != null) {
//            weatherDat4.setLatitude(myLastLocation.getLatitude());
//            weatherDat4.setLongitude(myLastLocation.getLongitude());
//            result.add(weatherDat4);
//            mWeatherAdapter.setWeatherData(result);
//        } else {
//            Log.d("asd", "location is null");
//            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
//        }
//    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("asd", "location changed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("asd", "location changed");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("asd", "location changed");
        if (location != null) {
            Log.d("asd", "location FOUND!");
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
//            handleWeatherInformation();
        }
    }

}

