package com.anddevbg.lawa.panoramio;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.anddevbg.lawa.networking.NetworkRequestManagerImpl;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public class PanoramioWrapper {
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;


    private static final double BOUNDING_BOX = 0.05;

    private static final String getPanoramioURL(double minx, double maxx, double miny, double maxy) {
        return "http://www.panoramio.com" +
                "/map/get_panoramas.php?" +
                "order=popularity&" +
                "set=public&from=0&to=100&minx=" + minx + "&miny=" + miny + "&maxx=" + maxx + "&maxy=" + maxy + "&size=small";
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
