package com.anddevbg.lawa.ui.activity.weather;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
    ForecastAdapter mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        dataList = new ArrayList<>();
        getIntentInfo();
        initControls();
        initLocation();

    }

    @Override
    public void onForecastReceived(JSONObject response) {
        try {
            Log.d("asd", "JSON response is " + response.toString(2));
            JSONObject city = response.getJSONObject("city");
            String cityName = city.getString("name");

            JSONArray jsonArray = (JSONArray) response.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jMain = jsonArray.getJSONObject(i);
                ForecastData forecastDataObj = new ForecastData();

                JSONObject main = jMain.getJSONObject("temp");
                float maxTempDay = (float) main.getDouble("max");
                float minTempDay = (float) main.getDouble("min");
                forecastDataObj.setmMaximalTemperature(maxTempDay);
                forecastDataObj.setmMinimalTemperature(minTempDay);
                forecastDataObj.setmDay(cityName);
                dataList.add(forecastDataObj);
                Log.d("asd", "max/min temp is " + maxTempDay + " " + minTempDay);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(dataList.isEmpty()) {
            Toast.makeText(this, "empty data list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onForeastError(VolleyError error) {
    }

    private void getIntentInfo() {
        Intent i = getIntent();
        int idOfCity = i.getIntExtra("id", -1);
        Log.d("asd", "id of city is " + idOfCity);
        forecastWrapper = new ForecastWrapper(idOfCity);
        forecastWrapper.receiveWeatherForecast(this);
    }

    private void initLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationLast = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void initControls() {
        mForecastAdapter = new ForecastAdapter(this, dataList);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mForecastAdapter);

    }

}
