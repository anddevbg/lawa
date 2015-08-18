package com.anddevbg.lawa.weather;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 18/08/2015.
 */
public interface ISearchCityCallback {
    void onJSONResponse(JSONObject response);
    void onJSONError(VolleyError error);
}
