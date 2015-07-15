package com.anddevbg.lawa.panoramio;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.anddevbg.lawa.location.RetrieveLocation;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.networking.NetworkRequestManagerImpl;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public class PanoramioWrapper {
    private Location mLocation;
    private RetrieveLocation retrieveLocation;
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

    public void fetchPictures(Location location, final IPanoramioCallback callback) {
        // FIXME memory leak
        Log.d("asd", "callback.onPanoramioResponse(response)");
        mLocation = location;
        String panoramioURL = getPanoramioURL(getLongitude() - BOUNDING_BOX, getLongitude() + BOUNDING_BOX, getLatitude() - BOUNDING_BOX, getLatitude() + BOUNDING_BOX);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, panoramioURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    callback.onPanoramioResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Volley error");
                callback.onPanoramioErrorResponse(error);
            }
        });
        NetworkRequestManagerImpl.getInstance().performRequest(request);
    }

}
