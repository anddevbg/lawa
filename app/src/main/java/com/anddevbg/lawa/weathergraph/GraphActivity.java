package com.anddevbg.lawa.weathergraph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.weather.ForecastWrapper;
import com.anddevbg.lawa.weather.IForecastCallback;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class GraphActivity extends AppCompatActivity implements IForecastCallback{

    private List<Float> mGraphMinimum;
    private List<Float> mGraphMaximum;
    private ForecastWrapper mForecastWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mGraphMinimum = new ArrayList<>();
        mGraphMaximum = new ArrayList<>();
        fetchGraphData();

//        float[] graphData = new float[]{0,30,10,-20,0};
//        for (int i=0; i< graphData.length; i++) {
//            graphData[i]= graphData[i]+20;
//            Log.d("qwe", "graph data is "+ graphData[i]);
//        }

//        mWeatherGraph.setChartData(graphData);
    }

    private void fetchGraphData() {
        Intent i = getIntent();
        int cityId = i.getIntExtra("id", 0);
        mForecastWrapper = new ForecastWrapper(cityId);
        Log.d("graph", "city id is " + cityId);
        mForecastWrapper.receiveWeatherForecast(this);
    }

    @Override
    public void onForecastReceived(JSONObject response) {
        try {
            JSONArray jsonArray = (JSONArray) response.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jMain = jsonArray.getJSONObject(i);
                JSONObject main = jMain.getJSONObject("temp");
                float maxTempDay = (float) main.getDouble("max");
                mGraphMaximum.add(maxTempDay);
                float minTempDay = (float) main.getDouble("min");
                mGraphMinimum.add(minTempDay);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        float[] arrayMin = new float[5];
        float[] arrayMax = new float[5];
        for(int i=0; i<mGraphMinimum.size() && i<mGraphMaximum.size(); i++) {
            arrayMin[i] = mGraphMinimum.get(i);
            arrayMax[i] = mGraphMaximum.get(i);
        }

        for(int i=0; i<arrayMin.length && i<arrayMax.length; i++) {
            arrayMin[i] = arrayMin[i] + 20;
            arrayMax[i] = arrayMax[i] + 20;
        }

        WeatherGraph mWeatherGraph = (WeatherGraph) findViewById(R.id.weather_graph);
        mWeatherGraph.setChartData(arrayMin, arrayMax);
    }

    @Override
    public void onForeastError(VolleyError error) {

    }
}
