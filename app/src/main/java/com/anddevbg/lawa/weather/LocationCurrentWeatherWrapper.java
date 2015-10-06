package com.anddevbg.lawa.weather;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.LinkAddress;
import android.util.Log;
import android.widget.Toast;

import com.anddevbg.lawa.LawaApplication;
import com.anddevbg.lawa.networking.NetworkRequestManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by adri.stanchev on 18/07/2015.
 */
public class LocationCurrentWeatherWrapper {
    private Location mLocation;
    private String cityNameOne;
    private String mResultResponse;

    public LocationCurrentWeatherWrapper(Location mLastKnownLocation) {
        mLocation = mLastKnownLocation;
    }

    public final String getOpenWeatherApiUrl() {
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        Geocoder geocoder = new Geocoder(LawaApplication.getContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList.size() > 0) {
                cityNameOne = addressList.get(0).getLocality();
            }
        } catch (IOException e) {
            Log.d("wiidget", "IO Exception " + e.toString());
            e.printStackTrace();
        }
        if (cityNameOne != null) {
            mResultResponse = cityNameOne.replaceAll("\\s", "%20");
        }
        String myNewResult = "http://api.openweathermap.org/data/2.5/weather?q=" + mResultResponse +
        "&units=metric&APPID=8b632a903448af2dfe8865826f40b459";
        return myNewResult;
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

    public RequestFuture<JSONObject> getWeatherUpdateSync() {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest a = new JsonObjectRequest(Request.Method.GET, getOpenWeatherApiUrl(), future, future);
        NetworkRequestManager.getInstance().performRequest(a);
        return future;
    }
}
