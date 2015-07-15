package com.anddevbg.lawa.ui.activity.weather;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.model.WeatherData;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    ViewPager viewPager;


    private WeatherFragmentAdapter mWeatherAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private ConnectivityManager connectivityManager;
    private WeatherData city1Data;
    private WeatherData city2Data;

    private List<WeatherData> createMockData() {
        List<WeatherData> result = new ArrayList<>();
        city1Data = new WeatherData();

        city1Data.setCityName("New York");
        city1Data.setCurrent(22);
        city1Data.setMin(5);
        city1Data.setMax(22);

        city2Data = new WeatherData();
        city2Data.setCityName("Veliko Tarnovo");
        city2Data.setCurrent(25);
        city2Data.setMax(32);


        result.add(city1Data);
        result.add(city2Data);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
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
        mWeatherAdapter.setWeatherData(createMockData());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);

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

