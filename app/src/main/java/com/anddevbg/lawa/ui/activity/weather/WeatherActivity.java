package com.anddevbg.lawa.ui.activity.weather;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private WeatherFragmentAdapter mWeatherAdapter;
    private ViewPager viewPager;
    private List<WeatherData> result;
    int search_request_code = 1;
    private SearchView searchView;
    private double mLatitude;
    private double mLongitude;


    private LocationManager locationManager;
    private Location mLastKnownLocation;

    private List<WeatherData> getWeatherData() {
        result = new ArrayList<>();
        if(mLastKnownLocation != null) {
            mLatitude = mLastKnownLocation.getLatitude();
            mLongitude = mLastKnownLocation.getLongitude();
        }
        WeatherData city1Data = new WeatherData();
        city1Data.setLatitude(mLatitude);
        city1Data.setLongitude(mLongitude);
        result.add(city1Data);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        locationManager = (LocationManager) this.getApplicationContext().getSystemService(LOCATION_SERVICE);
        initControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLocation();
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
                WeatherData weatherData = new WeatherData();
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                double latitude;
                double longitude;
                try {
                    addresses = geocoder.getFromLocationName(locationName, 1);
                    Address address = addresses.get(0);
                    longitude = address.getLongitude();
                    latitude = address.getLatitude();
                    weatherData.setLatitude(latitude);
                    weatherData.setLongitude(longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result.add(weatherData);
                mWeatherAdapter.setWeatherData(result);
                mWeatherAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(result.size(), true);
            }
        }
    }

    private void initControls() {
        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        mWeatherAdapter.setWeatherData(getWeatherData());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);
        viewPager.setPageTransformer(false, new ZoomPagerTransformation());
    }

}

