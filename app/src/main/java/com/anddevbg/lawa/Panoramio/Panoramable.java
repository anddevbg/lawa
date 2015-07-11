package com.anddevbg.lawa.Panoramio;

import android.location.Location;
import android.view.View;

import org.json.JSONArray;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public interface Panoramable {
    public void processResponse(JSONArray jsonArray);
    public void retrieveImageUrl();
}
