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
    private List<TextView> mTextViews;

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
        mTextViews = new ArrayList<>();
        mDaysOfWeek = new ArrayList<>();
        mGraphMinimum = new ArrayList<>();
        mGraphMaximum = new ArrayList<>();
    }

    private void initControls() {
        mCityNameTextView = (TextView) findViewById(R.id.city_graph_text_view);
        TextView mDay1TextView = (TextView) findViewById(R.id.day1_text_view);
        TextView mDay2TextView = (TextView) findViewById(R.id.day2_text_view);
        TextView mDay3TextView = (TextView) findViewById(R.id.day3_text_view);
        TextView mDay4TextView = (TextView) findViewById(R.id.day4_text_view);
        TextView mDay5TextView = (TextView) findViewById(R.id.day5_text_view);
        mTextViews.add(mDay1TextView);
        mTextViews.add(mDay2TextView);
        mTextViews.add(mDay3TextView);
        mTextViews.add(mDay4TextView);
        mTextViews.add(mDay5TextView);
    }

    private void fetchGraphData() {
        Intent i = getIntent();
        int cityId = i.getIntExtra("id", 0);
        String cityName = i.getStringExtra("name");
        ForecastWrapper mForecastWrapper = new ForecastWrapper(cityId);
        Log.d("graph", "city id is " + cityId);
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
                String monday = new SimpleDateFormat("EEE").format(date);
                mDaysOfWeek.add(monday);
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Log.d("metrics", "width is " + width);
        for(int i=0; i<mDaysOfWeek.size(); i++) {
            mTextViews.get(i).setWidth(width/5 + 10);
            mTextViews.get(i).setText(mDaysOfWeek.get(i));
        }
        mWeatherGraphView.setArrays(arrayMin, arrayMax);
    }

    @Override
    public void onForeastError(VolleyError error) {
        Toast.makeText(this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
    }
}
