package com.anddevbg.lawa.ui.activity.weather;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.SearchView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ViewPager viewPager;
    SearchView searchView;

    private SwipeRefreshLayout mSwipeRefresh;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initControls() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        WeatherFragmentAdapter mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
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

}

