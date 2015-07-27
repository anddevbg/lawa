package com.anddevbg.lawa.ui.activity.weather;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.ForecastAdapter;
import com.anddevbg.lawa.model.ForecastData;
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
 * Created by adri.stanchev on 20/07/2015.
 */
public class ForecastActivity extends AppCompatActivity implements IForecastCallback {

    LocationManager locationManager;
    Location mLocationLast;
    public List<ForecastData> dataList;
    private ForecastWrapper forecastWrapper;
    private ForecastAdapter mForecastAdapter;
    private TextView emptyTextView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        fetchForecast();
        initControls();
        initLocation();
    }

    @Override
    public void onForecastReceived(JSONObject response) {
        dataList = new ArrayList<>();
        try {
            JSONObject city = response.getJSONObject("city");
            String cityName = city.getString("name");

            JSONArray jsonArray = (JSONArray) response.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jMain = jsonArray.getJSONObject(i);
                ForecastData forecastDataObj = new ForecastData();
                int timestamp = jMain.getInt("dt");
                Date date = new Date(timestamp*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(date);

                JSONObject main = jMain.getJSONObject("temp");
                float maxTempDay = (float) main.getDouble("max");
                float minTempDay = (float) main.getDouble("min");
                forecastDataObj.setMaximalTemperature(maxTempDay);
                forecastDataObj.setMinimalTemperature(minTempDay);
                forecastDataObj.setDay(day);
                dataList.add(forecastDataObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(dataList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            mForecastAdapter.setData(dataList);
        }
    }

    @Override
    public void onForeastError(VolleyError error) {
    }

    private void fetchForecast() {
        Intent i = getIntent();
        int idOfCity = i.getIntExtra("id", -1);
        forecastWrapper = new ForecastWrapper(idOfCity);
        forecastWrapper.receiveWeatherForecast(this);
    }

    private void initLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationLast = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void initControls() {
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);
    }

}
