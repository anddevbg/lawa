package com.anddevbg.lawa.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddevbg.lawa.Networking.ContextHelper;
import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.WeatherData;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment {

    private static final String WEATHER_DATA = "weather_data";
    private Context mContext;

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
    private TextView mTimeLastRefresh;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherData = (WeatherData) getArguments().get(WEATHER_DATA);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mContext = this.getActivity();
        initControls(view);
        setupControls();
        return view;

    }

    private void initControls(View view) {
        mWeatherImage = (ImageView) view.findViewById(R.id.current_weather_imageView);
        mCurrentTemp = (TextView) view.findViewById(R.id.temperature_textView);
        mCity = (TextView) view.findViewById(R.id.city_textView);
        mMin = (TextView) view.findViewById(R.id.min_temp_textView);
        mMax = (TextView) view.findViewById(R.id.max_temp_textView);
        mTimeLastRefresh = (TextView) view.findViewById(R.id.last_refresh_textView);
    }

    private void setupControls() {
        mCurrentTemp.setText(Integer.toString(mWeatherData.getCurrent()));
        mCity.setText(mWeatherData.getCityName());
        mMin.setText(Integer.toString(mWeatherData.getMin()));
        mMax.setText(Integer.toString(mWeatherData.getMax()));
        mTimeLastRefresh.setText(Double.toString(mWeatherData.getTimeLastRefresh()));
        //TODO

    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

}
