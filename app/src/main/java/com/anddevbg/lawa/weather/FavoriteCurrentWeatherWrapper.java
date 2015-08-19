package com.anddevbg.lawa.weather;

import android.util.Log;

import com.anddevbg.lawa.networking.NetworkRequestManager;
import com.anddevbg.lawa.ui.fragment.FavoriteCityFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 18/08/2015.
 */
public class FavoriteCurrentWeatherWrapper {
    private String cityName;

    public FavoriteCurrentWeatherWrapper(String name) {
        cityName = name;
    }
    private String getWeather() {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + cityName +
                "&units=metric&APPID=8b632a903448af2dfe8865826f40b459";
    }
    public void getWeatherUpdate(final ICurrentWeatherCallback weatherCallback) {
        Log.d("asd", "in favoritecurrentweatherwrapper" + getWeather());
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getWeather(), new Response.Listener<JSONObject>() {
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
