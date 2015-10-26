package com.anddevbg.lawa.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.panoramio.IPanoramioCallback;
import com.anddevbg.lawa.panoramio.PanoramioWrapper;
import com.anddevbg.lawa.ui.activity.weather.ForecastActivity;
import com.anddevbg.lawa.util.RandomUtil;
import com.anddevbg.lawa.weather.ICurrentWeatherCallback;
import com.anddevbg.lawa.weather.CurrentWeatherWrapper;
import com.anddevbg.lawa.weathergraph.GraphActivity;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class BaseWeatherFragment extends Fragment implements IPanoramioCallback, ICurrentWeatherCallback, Serializable {

    private static final String WEATHER_DATA = "weather_data";

    private int mCityID;
    public String mPanoramioURL;

    private ImageView mWeatherImage;
    private TextView mCity;
    private TextView mCurrentTemp;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private WeatherData mWeatherData;
    private TextView mDescriptionWeatherText;
    private View mCoordinatorView;

    private CurrentWeatherWrapper mWeatherWrapper;
    private String mCityName;
    private WeatherDatabaseManager mWeatherDatabaseManager;
    private NotificationManager mNotificationManager;

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
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mWeatherDatabaseManager = WeatherDatabaseManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initControls(view);
        double lat = mWeatherData.getLatitude();
        double lon = mWeatherData.getLongitude();
        initWeatherAndPanoramio(lon, lat);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initWeatherAndPanoramio(double longitude, double latitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        PanoramioWrapper panoramioWrapper = new PanoramioWrapper();
        panoramioWrapper.fetchPictures(location, this);
        mWeatherWrapper = new CurrentWeatherWrapper(location);
        mWeatherWrapper.getWeatherUpdate(this);
    }

    @Override
    public void onWeatherApiResponse(JSONObject result) {
        try {
            if (result.has("main")) {
                JSONObject main = result.getJSONObject("main");
                setupCurrentTemperature(main);
                setupWindSpeed(result);
                setUpHumidity(main);
                trimCityNameAndGetCityID(result);
                setupWeatherDescription(result);
                showNotification();
            } else {
                showSnackBar();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void trimCityNameAndGetCityID(JSONObject result) {
        try {
            mCityID = result.getInt("id");
            mCityName = result.getString("name");
            String trimmedCityName = mCityName.replaceAll("Obshtina ", "");
            mCity.setText(trimmedCityName);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void setupWeatherDescription(JSONObject result) {
        try {
            JSONArray jArray = result.getJSONArray("weather");
            JSONObject description = jArray.getJSONObject(0);
            String desc = description.getString("description");
            mDescriptionWeatherText.setText(desc);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void setUpHumidity(JSONObject main) {
        try {
            int humidity = main.getInt("humidity");
            String humidityText = String.valueOf(humidity) + "%";
            mHumidity.setText(humidityText);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void setupWindSpeed(JSONObject result) {
        try {
            JSONObject windSpeed = result.getJSONObject("wind");
            double wind = windSpeed.getDouble("speed");
            String windSpeedText = String.valueOf(wind) + "m/s";
            mWindSpeed.setText(windSpeedText);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void setupCurrentTemperature(JSONObject main) {
        try {
            int currentWeather = main.getInt("temp");
            mCurrentTemp.setText(String.valueOf(currentWeather + "ºC"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSnackBar() {
        Snackbar.make(mCoordinatorView, "Error loading data", Snackbar.LENGTH_LONG)
                .setAction("retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWeatherWrapper.getWeatherUpdate(BaseWeatherFragment.this);
                    }
                })
                .show();
    }

    private void showNotification() {
        if (getActivity() != null) {
            Notification notification = new NotificationCompat.Builder(getActivity())
                    .setContentTitle("LAWA")
                    .setSmallIcon(R.mipmap.ic_white_not)
                    .setContentText("Weather in " + mCity.getText() + " is " + mCurrentTemp.getText())
                    .build();
            mNotificationManager.notify(1, notification);
        }
    }

    @Override
    public void onWeatherApiErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), "Something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void onPanoramioResponse(JSONObject result) {
        JSONArray array;
        ArrayList<String> photoArray = new ArrayList<>();
        try {
            array = result.getJSONArray("photos");
            JSONObject jsonObject = array.getJSONObject(RandomUtil.randInt(0, 5));
            photoArray.add(jsonObject.getString("photo_file_url"));
            mPanoramioURL = photoArray.get(0);
            Picasso.with(getActivity()).load(mPanoramioURL).placeholder(R.layout.progress).into(mWeatherImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPanoramioErrorResponse(VolleyError error) {
        respondToPanoramioErrorResponse(error);
    }

    protected void initControls(View view) {
        mWeatherImage = (ImageView) view.findViewById(R.id.current_weather_imageView);
        mCurrentTemp = (TextView) view.findViewById(R.id.temperature_textView);
        mCity = (TextView) view.findViewById(R.id.city_textView);
        mHumidity = (TextView) view.findViewById(R.id.min_temp_textView);
        mWindSpeed = (TextView) view.findViewById(R.id.max_temp_textView);
        mDescriptionWeatherText = (TextView) view.findViewById(R.id.last_refresh_textView);
        mCoordinatorView = view.findViewById(R.id.snackbar);
        Button forecastButton = (Button) view.findViewById(R.id.forecast_button);
        forecastButton.setText("see forecast");
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForecastActivity();
            }
        });
        Button mGraphButton = (Button) view.findViewById(R.id.graph_button);
        mGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGraphActivity();
            }
        });
    }

    private void goToGraphActivity() {
        Intent graphIntent = new Intent(getActivity(), GraphActivity.class);
        graphIntent.putExtra("id", mCityID);
        graphIntent.putExtra("name", mCityName);
        startActivity(graphIntent);
    }

    public void goToForecastActivity() {
        Intent i = new Intent(getActivity(), ForecastActivity.class);
        i.putExtra("id", mCityID);
        startActivity(i);
    }

    private void respondToPanoramioErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
