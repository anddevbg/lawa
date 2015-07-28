package com.anddevbg.lawa.panoramio;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by adri.stanchev on 12/07/2015.
 */
public interface IPanoramioCallback {
     void onPanoramioResponse(JSONObject result);
     void onPanoramioErrorResponse(VolleyError error);
}
