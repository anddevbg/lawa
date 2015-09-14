package com.anddevbg.lawa.weathergraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class GraphActivity extends AppCompatActivity {

    private WeatherGraph mWeatherGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherGraph = new WeatherGraph(this);
        setContentView(mWeatherGraph);
    }
}
