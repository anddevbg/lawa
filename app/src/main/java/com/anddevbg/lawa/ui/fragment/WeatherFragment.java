package com.anddevbg.lawa.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.WeatherData;



/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment {

    private static final String WEATHER_DATA = "weather_data";

    public static WeatherFragment getInstance(WeatherData weatherData) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER_DATA, weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView mWeatherImage;
    private TextView mCity;
    private TextView mCurrentTemp;
    private TextView mMin;
    private TextView mMax;
    private WeatherData mWeatherData;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherData = (WeatherData) getArguments().get(WEATHER_DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        initControls(view);
        setupControls();
        /*
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        */

        return view;

    }

    private void initControls(View view) {
        mWeatherImage = (ImageView) view.findViewById(R.id.current_weather_imageView);
        mCurrentTemp = (TextView) view.findViewById(R.id.temperature_textView);
        mCity = (TextView) view.findViewById(R.id.city_textView);
        mMin = (TextView) view.findViewById(R.id.min_temp_textView);
        mMax = (TextView) view.findViewById(R.id.max_temp_textView);
    }

    private void setupControls() {
        mCurrentTemp.setText(Integer.toString(mWeatherData.getCurrent()));
        mCity.setText(mWeatherData.getCityName());
        mMin.setText(Integer.toString(mWeatherData.getMin()));
        mMax.setText(Integer.toString(mWeatherData.getMax()));

    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

}
