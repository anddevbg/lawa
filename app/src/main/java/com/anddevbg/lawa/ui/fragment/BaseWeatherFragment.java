package com.anddevbg.lawa.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.panoramio.IPanoramioCallback;
import com.anddevbg.lawa.panoramio.PanoramioWrapper;
import com.anddevbg.lawa.ui.activity.weather.ForecastActivity;
import com.anddevbg.lawa.util.RandomUtil;
import com.anddevbg.lawa.weather.CurrentWeatherWrapper;
import com.anddevbg.lawa.weather.ICurrentWeatherCallback;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class BaseWeatherFragment extends Fragment implements IPanoramioCallback, ICurrentWeatherCallback {

    private static final String WEATHER_DATA = "weather_data";
    protected LocationManager locationManager;
    private int cityID;

    private ImageView mWeatherImage;
    private TextView mCity;
    private TextView mCurrentTemp;
    private TextView mMin;
    private TextView mMax;
    private WeatherData mWeatherData;
    private TextView descriptionWeatherText;

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
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mWeatherData = (WeatherData) getArguments().get(WEATHER_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initLocation();
        initControls(view);
        setupControls();
        if (mLastKnownLocation != null) {
            panoramioWrapper.fetchPictures(mLastKnownLocation, this);
        } else {
            Location mLocationLast = new Location(LocationManager.NETWORK_PROVIDER);
            mLocationLast.setLatitude(42.6964);
            mLocationLast.setLongitude(23.3260);
            panoramioWrapper.fetchPictures(mLocationLast, this);
        }
        initWeather();
        return view;
    }

    private void initWeather() {
        CurrentWeatherWrapper weatherWrapper = new CurrentWeatherWrapper(mLastKnownLocation);
        weatherWrapper.getWeatherUpdate(this);
    }

    @Override
    public void onWeatherApiResponse(JSONObject result) {
        int currentWeather;

        try {
            Log.d("asd", "json response");
            JSONObject main = result.getJSONObject("main");
            currentWeather = main.getInt("temp");
            mCurrentTemp.setText(String.valueOf(currentWeather + "ºC"));

            JSONObject windSpeed = result.getJSONObject("wind");
            int wind = windSpeed.getInt("speed");
            mMax.setText(String.valueOf(wind) + " m/s");

            int humidity = main.getInt("humidity");
            mMin.setText(String.valueOf(humidity) + "%");

            cityID = result.getInt("id");
            String name = result.getString("name");
            mCity.setText(name);

            JSONArray jArray = result.getJSONArray("weather");
            JSONObject description = jArray.getJSONObject(0);
            String desc = description.getString("description");
            descriptionWeatherText.setText(desc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeatherApiErrorResponse(VolleyError error) {
        Log.d("asd", "json error");
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void onPanoramioResponse(JSONObject result)  {
        JSONArray array;
        ArrayList<String> photoArray = new ArrayList<>();
        try {
            array = result.getJSONArray("photos");
            JSONObject jsonObject = array.getJSONObject(RandomUtil.randInt(0,5));
            photoArray.add(jsonObject.getString("photo_file_url"));
            Picasso.with(getActivity()).load(photoArray.get(0)).into(mWeatherImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPanoramioErrorResponse(VolleyError error) {
        respondToPanoramioErrorResponse();
    }

    private void initControls(View view) {
        Button forecastButton = (Button) view.findViewById(R.id.forecast_button);
        forecastButton.setText("see forecast");
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForecastActivity();
            }
        });
        mWeatherImage = (ImageView) view.findViewById(R.id.current_weather_imageView);
        mCurrentTemp = (TextView) view.findViewById(R.id.temperature_textView);
        mCity = (TextView) view.findViewById(R.id.city_textView);
        mMin = (TextView) view.findViewById(R.id.min_temp_textView);
        mMax = (TextView) view.findViewById(R.id.max_temp_textView);
        descriptionWeatherText = (TextView) view.findViewById(R.id.last_refresh_textView);
    }

    public void goToForecastActivity() {
        Intent i = new Intent(getActivity(), ForecastActivity.class);
        i.putExtra("id", cityID);
        startActivity(i);
    }

    private void setupControls() {
        mCurrentTemp.setText(Integer.toString(mWeatherData.getCurrent()));
        mCity.setText(mWeatherData.getCityName());
        mMin.setText(Integer.toString(mWeatherData.getMin()));
        mMax.setText(Integer.toString(mWeatherData.getMax()));
        descriptionWeatherText.setText((mWeatherData.getTimeLastRefresh()));
    }

    private void respondToPanoramioErrorResponse() {
    }

    public void initLocation() {
        panoramioWrapper = new PanoramioWrapper();
        mLastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
    }

}
