package com.anddevbg.lawa.weather;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 18/07/2015.
 */
public interface ICurrentWeatherCallback {
         void onWeatherApiResponse(JSONObject result);
         void onWeatherApiErrorResponse(VolleyError error);
}
