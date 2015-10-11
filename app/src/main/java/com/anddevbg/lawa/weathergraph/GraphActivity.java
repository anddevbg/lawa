package com.anddevbg.lawa.weathergraph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.weather.ForecastWrapper;
import com.anddevbg.lawa.weather.IForecastCallback;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class GraphActivity extends AppCompatActivity implements IForecastCallback{

    private List<Float> mGraphMinimum;
    private List<Float> mGraphMaximum;
    private List<String> mDaysOfWeek;

    private TextView mCityNameTextView;
    private WeatherGraphView mWeatherGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mWeatherGraphView = (WeatherGraphView) findViewById(R.id.weather_graph_view);

        initArraysAndPaths();
        initControls();
        fetchGraphData();
    }

    private void initArraysAndPaths() {
        mDaysOfWeek = new ArrayList<>();
        mGraphMinimum = new ArrayList<>();
        mGraphMaximum = new ArrayList<>();
    }

    private void initControls() {
        mCityNameTextView = (TextView) findViewById(R.id.city_graph_text_view);
    }

    private void fetchGraphData() {
        Intent i = getIntent();
        int cityId = i.getIntExtra("id", 0);
        String cityName = i.getStringExtra("name");
        ForecastWrapper mForecastWrapper = new ForecastWrapper(cityId);
        mForecastWrapper.receiveWeatherForecast(this);
        mCityNameTextView.setText(cityName);
    }

    @Override
    public void onForecastReceived(JSONObject response) {
        try {
            JSONArray jsonArray = (JSONArray) response.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jMain = jsonArray.getJSONObject(i);
                JSONObject main = jMain.getJSONObject("temp");
                float maxTempDay = (float) main.getDouble("max");
                mGraphMaximum.add(maxTempDay+20);
                float minTempDay = (float) main.getDouble("min");
                mGraphMinimum.add(minTempDay+20);

                int timestamp = jMain.getInt("dt");
                Date date = new Date(timestamp*1000L);
                String dayOfWeek = new SimpleDateFormat("EEE").format(date);
                mDaysOfWeek.add(dayOfWeek);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        prepareGraph();
    }


    private void prepareGraph() {
        float[] arrayMin = new float[5];
        float[] arrayMax = new float[5];
        for(int i=0; i<mGraphMinimum.size() && i<mGraphMaximum.size(); i++) {
            arrayMin[i] = mGraphMinimum.get(i);
            arrayMax[i] = mGraphMaximum.get(i);
        }

        mWeatherGraphView.setArrays(arrayMin, arrayMax, mDaysOfWeek);
    }

    @Override
    public void onForeastError(VolleyError error) {
        Toast.makeText(this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
    }
}
