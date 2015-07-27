package com.anddevbg.lawa.weather;

import android.location.Location;
import android.util.Log;

import com.anddevbg.lawa.networking.NetworkRequestManagerImpl;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by adri.stanchev on 20/07/2015.
 */
public class ForecastWrapper {

    int cityIdName;


    public ForecastWrapper(int cityIdentifier) {
        cityIdName = cityIdentifier;
    }

    private final String getForecastUrl() {


        Log.d("asd", "city api call: " + "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + cityIdName +
                "&cnt=5&units=metric&APPID=8b632a903448af2dfe8865826f40b459");
        return "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + cityIdName +
                "&cnt=5&units=metric&APPID=8b632a903448af2dfe8865826f40b459";

    }

    public void receiveWeatherForecast(final IForecastCallback callback) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getForecastUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onForecastReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onForeastError(error);
            }
        });
        NetworkRequestManagerImpl.getInstance().performRequest(request);

    }
}
