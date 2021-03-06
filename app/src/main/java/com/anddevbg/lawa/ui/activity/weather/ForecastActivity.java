package com.anddevbg.lawa.ui.activity.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.ForecastAdapter;
import com.anddevbg.lawa.model.ForecastData;
import com.anddevbg.lawa.recyclerview.OnStartDragListener;
import com.anddevbg.lawa.recyclerview.SimpleItemTouchHelperCallback;
import com.anddevbg.lawa.weather.ForecastWrapper;
import com.anddevbg.lawa.weather.IForecastCallback;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adri.stanchev on 20/07/2015.
 */
public class ForecastActivity extends AppCompatActivity implements IForecastCallback, OnStartDragListener {

    public List<ForecastData> mDataList;
    ForecastAdapter mForecastAdapter;
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        fetchForecast();
        initControls();
    }

    @Override
    public void onForecastReceived(JSONObject response) {
        mDataList = new ArrayList<>();
        try {
            JSONObject city = response.getJSONObject("city");
            String cityName = city.getString("name");

            JSONArray jsonArray = (JSONArray) response.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jMain = jsonArray.getJSONObject(i);
                JSONArray j1 = jMain.getJSONArray("weather");
                ForecastData forecastDataObj = new ForecastData();

                forecastDataObj.setmCityName(cityName);

                int timestamp = jMain.getInt("dt");
                Date date = new Date(timestamp*1000L);
                String monday = new SimpleDateFormat("EEE, MMM d").format(date);
                JSONObject main = jMain.getJSONObject("temp");
                double maxTempDay = main.getDouble("max");
                double minTempDay = main.getDouble("min");

                DecimalFormat decimalFormat = new DecimalFormat("#");
                forecastDataObj.setMaximalTemperature(Double.parseDouble(decimalFormat.format(maxTempDay)));
                forecastDataObj.setMinimalTemperature(Double.parseDouble(decimalFormat.format(minTempDay)));
                forecastDataObj.setDay(monday);

                JSONObject weatherObj = j1.getJSONObject(0);
                String mainString = weatherObj.getString("main");
                forecastDataObj.setmDescription(mainString);

                String pngFileUrl = weatherObj.getString("icon");
                String fullForecastIconUrl = "http://openweathermap.org/img/w/" + pngFileUrl + ".png";
                forecastDataObj.setImageUrl(fullForecastIconUrl);

                mDataList.add(forecastDataObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mDataList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mForecastAdapter.setData(mDataList);
        }
    }

    @Override
    public void onForecastError(VolleyError error) {
        error.printStackTrace();
    }

    private void fetchForecast() {
        Intent i = getIntent();
        int idOfCity = i.getIntExtra("id", -1);
        ForecastWrapper forecastWrapper = new ForecastWrapper(idOfCity);
        forecastWrapper.receiveWeatherForecast(this);
    }

    private void initControls() {
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mForecastAdapter = new ForecastAdapter(this, this);
        mRecyclerView.setAdapter(mForecastAdapter);
        mForecastAdapter.onAttachedToRecyclerView(mRecyclerView);
        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mForecastAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    }
}
