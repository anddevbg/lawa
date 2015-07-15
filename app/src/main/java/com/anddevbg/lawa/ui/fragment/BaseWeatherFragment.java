package com.anddevbg.lawa.ui.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.panoramio.IPanoramioCallback;
import com.anddevbg.lawa.panoramio.PanoramioWrapper;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class BaseWeatherFragment extends Fragment implements IPanoramioCallback {

    private static final String WEATHER_DATA = "weather_data";
    protected LocationManager locationManager;

    private ImageView mWeatherImage;
    private TextView mCity;
    private TextView mCurrentTemp;
    private TextView mMin;
    private TextView mMax;
    protected WeatherData mWeatherData;
    private TextView mTimeLastRefresh;

    protected Location mLastKnownLocation;
    protected PanoramioWrapper panoramioWrapper;
    protected String locationProvider = LocationManager.NETWORK_PROVIDER;

    public static BaseWeatherFragment createInstance(WeatherData weatherData) {
        BaseWeatherFragment fragment = new BaseWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(WEATHER_DATA, weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherData = (WeatherData) getArguments().get(WEATHER_DATA);
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        initLocation();
        initControls(view);
        setupControls();
        panoramioWrapper.fetchPictures(mLastKnownLocation, this);

        return view;
    }

    @Override
    public void onPanoramioResponse(JSONObject result)  {
        JSONArray array = null;
        ArrayList<String> photoArray = new ArrayList<>();
        try {
            for (int i = 0; i<3; i++) {
                array = result.getJSONArray("photos");
                JSONObject jsonObject = array.getJSONObject(i);
                photoArray.add(jsonObject.getString("photo_file_url"));
                Picasso.with(getActivity()).load(photoArray.get(i)).into(mWeatherImage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPanoramioErrorResponse(VolleyError error) {
        respondToPanoramioErrorResponse();
        Log.d("asd", "JSON ERROR RESPONSE");
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
    }
    protected void initLocation() {
        panoramioWrapper = new PanoramioWrapper();
        mLastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    private void respondToPanoramioErrorResponse() {
    }

}
