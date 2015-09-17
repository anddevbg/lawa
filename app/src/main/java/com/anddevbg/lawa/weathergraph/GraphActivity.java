package com.anddevbg.lawa.weathergraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.anddevbg.lawa.R;


/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        float[] graphData = new float[]{0,30,10,-20,0};
        for (int i=0; i< graphData.length; i++) {
            graphData[i]= graphData[i]+20;
            Log.d("qwe", "graph data is "+ graphData[i]);
        }
        WeatherGraph mWeatherGraph = (WeatherGraph) findViewById(R.id.weather_graph);
        mWeatherGraph.setChartData(graphData);
    }
}
