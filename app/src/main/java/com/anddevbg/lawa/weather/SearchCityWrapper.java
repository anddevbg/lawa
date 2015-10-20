package com.anddevbg.lawa.weather;

import android.util.Log;

import com.anddevbg.lawa.networking.NetworkRequestManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 18/08/2015.
 */
public class SearchCityWrapper {

    String mCitySearchName;
    public SearchCityWrapper(String cityName) {
        mCitySearchName = cityName;
    }
    private String getCityList() {
        String spaces = mCitySearchName.replaceAll("\\s","");
        Log.d("asd", "spaces string = " + spaces);
        return "http://api.openweathermap.org/data/2.5/find?q=" + spaces + "&units=metric&APPID=8b632a903448af2dfe8865826f40b459";
    }

    public void receiveSearchCityList(final ISearchCityCallback callback) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getCityList(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onJSONResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onJSONError(error);
            }
        });
        NetworkRequestManager.getInstance().performRequest(request);
    }

}
