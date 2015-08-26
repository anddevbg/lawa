package com.anddevbg.lawa.ui.activity.weather;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private static final String WEATHER_ARRAY = "weather_array";

    private WeatherFragmentAdapter mWeatherAdapter;
    private ViewPager viewPager;
    private List<WeatherData> result;
    private int search_request_code = 1;
    private int current_request_code = 2;
    private SearchView searchView;
    private double mLocationLatitude;
    private double mLocationLongitude;


    private LocationManager locationManager;
    private Location mLastKnownLocation;

    private List<WeatherData> getWeatherData() {
        result = new ArrayList<>();
        WeatherData city1Data = new WeatherData();
        if(mLastKnownLocation != null) {
            mLocationLatitude = mLastKnownLocation.getLatitude();
            mLocationLongitude = mLastKnownLocation.getLongitude();
        }
        city1Data.setLatitude(mLocationLatitude);
        city1Data.setLongitude(mLocationLongitude);
        result.add(city1Data);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        locationManager = (LocationManager) this.getApplicationContext().getSystemService(LOCATION_SERVICE);
        initLocation();
        initControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(WEATHER_ARRAY, (Serializable) result);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        result = (List<WeatherData>) savedInstanceState.get(WEATHER_ARRAY);
        mWeatherAdapter.setWeatherData(result);
    }

    private void initLocation() {
        mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        Button add = (Button) menu.findItem(R.id.action_add).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("asd", "action add clicked");
                Intent searchActivityIntent = new Intent(WeatherActivity.this, SearchCityActivity.class);
                startActivityForResult(searchActivityIntent, search_request_code);
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
                WeatherData weatherData = new WeatherData();
                weatherData.setLatitude(mLocationLatitude);
                weatherData.setLongitude(mLocationLongitude);
                result.add(weatherData);
                mWeatherAdapter.setWeatherData(result);
                viewPager.setCurrentItem(result.size(), true);
            }
        }
        /*
        if (requestCode == current_request_code) {
            Log.d("asd", "request success");
            if (resultCode == RESULT_OK) {
                WeatherData currentData = new WeatherData();
                currentData.setLatitude(mLastKnownLocation.getLatitude());
                currentData.setLongitude(mLastKnownLocation.getLongitude());
                result.add(currentData);
                mWeatherAdapter.setWeatherData(result);
            } if (resultCode == RESULT_CANCELED) {
                Log.d("asd", "result cancelled");
            }
        }
        */
    }

    private void initControls() {
        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);
        mWeatherAdapter.setWeatherData(getWeatherData());
        viewPager.setPageTransformer(false, new ZoomPagerTransformation());
    }

}

