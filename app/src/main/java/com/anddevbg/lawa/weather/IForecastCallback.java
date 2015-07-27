package com.anddevbg.lawa.weather;

import com.anddevbg.lawa.model.ForecastData;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by adri.stanchev on 20/07/2015.
 */
public interface IForecastCallback {
    public void onForecastReceived(JSONObject response);
    public void onForeastError(VolleyError error);
}
