package com.anddevbg.lawa.ui.activity.weather;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ViewPager viewPager;
    SearchView searchView;

    private WeatherFragmentAdapter mWeatherAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private ConnectivityManager connectivityManager;
    WeatherData city1Data;

    private ArrayList<WeatherData> getWeatherData() {
        ArrayList<WeatherData> result = new ArrayList<>();
        city1Data = new WeatherData();
        result.add(city1Data);
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initControls();
        checkLocationEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetEnabled();
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
    public boolean onSearchRequested() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra("name", searchView.getQuery());
        startActivity(i);
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkIsRefreshing();
            }
        });
    }

    private void checkIsRefreshing() {

    }

    public boolean isConnected() {
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    private void initControls() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        mWeatherAdapter.setWeatherData(getWeatherData());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);

        /*ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.action_image);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .build();
                */
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

