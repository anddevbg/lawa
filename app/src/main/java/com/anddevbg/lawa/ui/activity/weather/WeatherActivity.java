package com.anddevbg.lawa.ui.activity.weather;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.MyAdapter;
import com.anddevbg.lawa.model.WeatherData;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener  {
    ViewPager viewPager;

    private MyAdapter mWeatherAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    private List<WeatherData> createMockData() {
        List<WeatherData> result = new ArrayList<>();

        WeatherData city1Data = new WeatherData();
        city1Data.setCityName("New York");
        city1Data.setCurrent(22);
        city1Data.setMin(5);
        city1Data.setMax(22);
        city1Data.setWeatherImage("asd");

        WeatherData city2Data = new WeatherData();
        city2Data.setCityName("Paris");
        city2Data.setCurrent(22);
        city2Data.setMin(5);
        city2Data.setMax(22);
        city2Data.setWeatherImage("asd");

        result.add(city1Data);
        result.add(city2Data);
        result.add(city1Data);
        result.add(city2Data);
        result.add(city2Data);

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_LONG).show();
                checkIsRefreshing();
            }
        });

        
        mWeatherAdapter = new MyAdapter(getSupportFragmentManager());
        mWeatherAdapter.setWeatherData(createMockData());

/*
Instantiate viewpager and fragment manager +
set adapter to the viewpager by passing it a fragment manager object
*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mWeatherAdapter);

// create image view for floating action button and set drawable to it and instantiate it
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.mipmap.action_button);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .build();
    }

    private void checkIsRefreshing() {
        if(mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
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
    public void onRefresh() {
        

    }
}

