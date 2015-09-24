package com.anddevbg.lawa.ui.activity.weather;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.animation.ZoomPagerTransformation;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private WeatherFragmentAdapter mWeatherAdapter;
    private ViewPager mViewPager;
    private List<WeatherData> mResult;
    private int search_request_code = 1;
    private SearchView searchView;
    private double mLocationLatitude;
    private double mLocationLongitude;
    private GoogleApiClient mGoogleClient;
    private Location mLastKnownLocation;
    private WeatherDatabaseManager mWeatherDataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mResult = new ArrayList<>();
        initControls();
        setUpGoogleApiClient();
        getManagerAndShowData();

    }

    private void getManagerAndShowData() {
        mWeatherDataBaseManager = WeatherDatabaseManager.getInstance();
        mResult = mWeatherDataBaseManager.showAll();
        mWeatherAdapter.setWeatherData(mResult);
    }

    private void setUpGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline()) {
            Log.d("network", "device is online");
        } else {
            Toast.makeText(this, "No network access.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
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
//        Button add = (Button) menu.findItem(R.id.action_add).getActionView();
//        Button remove = (Button) menu.findItem(R.id.action_remove).getActionView();
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
                new AlertDialog.Builder(this)
                        .setTitle("Delete this city?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int index = mViewPager.getCurrentItem();
                                String cityNameForDeletion = mResult.get(index).getCityName();
                                Log.d("asd", "name is: " + cityNameForDeletion);
                                mWeatherDataBaseManager.deleteData(cityNameForDeletion);
                                mWeatherAdapter.removeView(index);
                                mResult.remove(index);
                                mWeatherAdapter.notifyDataSetChanged();
                                Toast.makeText(WeatherActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.action_location:
                WeatherData currentLocationWeatherData = new WeatherData();
                if (mLastKnownLocation != null) {
                    currentLocationWeatherData.setLatitude(mLastKnownLocation.getLatitude());
                    currentLocationWeatherData.setLongitude(mLastKnownLocation.getLongitude());
                    mResult.add(currentLocationWeatherData);
                    mWeatherAdapter.setWeatherData(mResult);
                    mViewPager.setCurrentItem(mResult.size());
                    mWeatherDataBaseManager.insertData("city", mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude());
                } else {
                    Toast.makeText(this, "Unknown location. Please try again.", Toast.LENGTH_SHORT).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == search_request_code) {
            if (resultCode == RESULT_OK) {
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
                mResult.add(data1);
                mWeatherAdapter.setWeatherData(mResult);
                mViewPager.setCurrentItem(mResult.size());
                mWeatherDataBaseManager.insertData(locationName, mLocationLatitude, mLocationLongitude);
            }
        }
    }

    private void initControls() {
        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mWeatherAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageTransformer(false, new ZoomPagerTransformation());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("location", "google api client connected");
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("location", "location suspended " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("asd", "location failed" + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
        }
    }

}

