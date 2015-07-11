package com.anddevbg.lawa.Panoramio;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.anddevbg.lawa.Location.RetrieveLocation;
import com.anddevbg.lawa.Networking.ContextHelper;
import com.anddevbg.lawa.Networking.NetworkRequestManager;
import com.anddevbg.lawa.model.WeatherData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public class PanoramioWrapper implements Panoramable {
    private Location mLocation;
    private RetrieveLocation retrieveLocation;
    private RequestQueue queue;
    private double mLatitude;
    private double mLongitude;
    private WeatherData mWeatherData;
    private List<WeatherData> weatherDataList;

    private static final double BOUNDING_BOX = 0.005;

    private static final String getPanoramioURL(double minx, double maxx, double miny, double maxy) {
        return "http://www.panoramio.com" +
                "/map/get_panoramas.php?" +
                "set=public&from=0&to=100&minx=" + minx + "&miny=" + miny + "&maxx=" + maxx + "&maxy=" + maxy;
    }

    public PanoramioWrapper(RetrieveLocation retrieveLoc) {
        retrieveLocation = retrieveLoc;
    }

    public void processResponse(JSONArray array) {
        JSONObject jObject;
        weatherDataList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            try {
                mWeatherData = new WeatherData();
                jObject = array.getJSONObject(i);
                mWeatherData.setWeatherImage(jObject.getString("photo_file_url"));
                mWeatherData.setCityName("VT");
                mWeatherData.setCurrent(22);
                weatherDataList.add(mWeatherData);
                Toast.makeText(ContextHelper.getAppContext(), "JSON response", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void retrieveImageUrl() {

    }

    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    public void fetchPictures(Location location) {
        mLocation = retrieveLocation.retrieveLocation();
        mLocation = location;
        queue = NetworkRequestManager.getInstance(ContextHelper.getAppContext()).getRequestQueue();
        String panoURl = getPanoramioURL(getLongitude() - BOUNDING_BOX, getLongitude() + BOUNDING_BOX, getLatitude() - BOUNDING_BOX, getLatitude() + BOUNDING_BOX);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, panoURl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray array = null;
                try {
                    Log.d("JSON", "Response is: " + response.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    array = response.getJSONArray("photos");
                    processResponse(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError();
            }
        });
        queue.add(request);
    }

    private void handleVolleyError() {
        Toast.makeText(ContextHelper.getAppContext(), "Volley Error", Toast.LENGTH_SHORT);
    }


}
