package com.anddevbg.lawa.weather;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 20/07/2015.
 */
public interface IForecastCallback {
     void onForecastReceived(JSONObject response);
     void onForecastError(VolleyError error);
}
