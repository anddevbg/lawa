package com.anddevbg.lawa.ui.activity.weather;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.anddevbg.lawa.Location.RetrieveLocation;
import com.anddevbg.lawa.Panoramio.PanoramioWrapper;
import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.MyAdapter;
import com.anddevbg.lawa.model.WeatherData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RetrieveLocation {
    ViewPager viewPager;

    private MyAdapter mWeatherAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private PanoramioWrapper panoramioWrapper;
    private ConnectivityManager connectivityManager;
    private String PanoURL;
    private ImageView mImageView;
    private JSONArray array;


    private List<WeatherData> createMockData() {
        List<WeatherData> result = new ArrayList<>();


        WeatherData city1Data = new WeatherData();
        city1Data.setCityName("New York");
        city1Data.setCurrent(22);
        city1Data.setMin(5);
        city1Data.setMax(22);
        //city1Data.setWeatherImage(picassoImage());
        city1Data.setTimeLastRefresh(2400);

        WeatherData city2Data = new WeatherData();
        city2Data.setCityName("Paris");
        city2Data.setCurrent(22);
        city2Data.setMin(5);
        city2Data.setMax(22);
        city2Data.setTimeLastRefresh(2300);

        result.add(city1Data);
        result.add(city2Data);

        return result;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initControls();
        buildGoogleApiClient();
        if(mLastLocation == null) {
            checkLocationEnabled();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetEnabled();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        panoramioWrapper.fetchPictures(mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public Location retrieveLocation() {
        if (mLastLocation != null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        return mLastLocation;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                checkIsRefreshing();
            }
        });
    }


    private void initControls() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        panoramioWrapper = new PanoramioWrapper(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mImageView = (ImageView) findViewById(R.id.current_weather_imageView);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.action_image);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .build();
    }

    private void checkIsRefreshing() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);

                }
            }
        }, 1000);
        mWeatherAdapter = new MyAdapter(getSupportFragmentManager());
        mWeatherAdapter.setWeatherData(createMockData());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);
    }

    public boolean isConnected() {
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    private void checkInternetEnabled() {
        if (!isConnected()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage(R.string.internet_dialog_message)
                    .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(WeatherActivity.this, "Please enable Internet connection to continue", Toast.LENGTH_LONG)
                                    .show();
                        }
                    })
                    .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent internetIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(internetIntent);
                        }
                    });
            alertBuilder.show();
        }


    }

    private void checkLocationEnabled() {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(WeatherActivity.this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (locationMode != Settings.Secure.LOCATION_MODE_OFF) {
            Toast.makeText(WeatherActivity.this, "Location mode ON", Toast.LENGTH_LONG);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(WeatherActivity.this);
            dialog.setMessage(R.string.location_dialog_message);
            dialog.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    WeatherActivity.this.startActivity(intent);
                }
            });
            dialog.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(WeatherActivity.this, "Please enable location services to continue", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }

    }
}

