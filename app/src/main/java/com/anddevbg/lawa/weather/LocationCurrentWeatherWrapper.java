package com.anddevbg.lawa.weather;

import android.location.Location;
import android.util.Log;

import com.anddevbg.lawa.networking.NetworkRequestManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 18/07/2015.
 */
public class LocationCurrentWeatherWrapper {
    private Location mLocation;
    double latitude;
    double longitude;

    public LocationCurrentWeatherWrapper(Location mLastKnownLocation) {
        mLocation = mLastKnownLocation;
    }

    public final String getOpenWeatherApiUrl() {

        if (mLocation != null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        } else {
            latitude = 42.6964;
            longitude = 23.3260;
        }
        return "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude +
                "&units=metric&APPID=8b632a903448af2dfe8865826f40b459";
    }

    public void getWeatherUpdate(final ICurrentWeatherCallback weatherCallback) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getOpenWeatherApiUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherCallback.onWeatherApiResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherCallback.onWeatherApiErrorResponse(error);
            }
        });
        NetworkRequestManager.getInstance().performRequest(request);
    }
}
