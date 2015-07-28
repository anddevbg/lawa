package com.anddevbg.lawa.model;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.panoramio.IPanoramioCallback;
import com.anddevbg.lawa.panoramio.PanoramioWrapper;
import com.anddevbg.lawa.util.RandomUtil;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by adri.stanchev on 16/07/2015.
 */
public class SearchActivity extends Activity implements IPanoramioCallback {
    private ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        searchImageView = (ImageView) findViewById(R.id.search_activity_image);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            final Geocoder geocoder = new Geocoder(this);
            new AsyncTask<Void, Void, Location>() {
                @Override
                protected Location doInBackground(Void... voids) {
                    List<Address> address = null;
                    try {
                        address = geocoder.getFromLocationName(query, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address a = address.get(RandomUtil.randInt(0, 0));
                    Location mLocation = new Location(LocationManager.NETWORK_PROVIDER);
                    mLocation.setLatitude(a.getLatitude());
                    mLocation.setLongitude(a.getLongitude());
                    return mLocation;
                }

                @Override
                protected void onPostExecute(Location location) {
                    super.onPostExecute(location);
                    PanoramioWrapper panoramioWrapObject = new PanoramioWrapper();
                    panoramioWrapObject.fetchPictures(location, SearchActivity.this);
                }
            }.execute();
        }
    }

    @Override
    public void onPanoramioResponse(JSONObject result) {
        JSONArray array;
        try {
            array = result.getJSONArray("photos");
            JSONObject json = array.getJSONObject(RandomUtil.randInt(0, 5));
            String photoURl = json.getString("photo_file_url");
            Picasso.with(this).load(photoURl).into(searchImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPanoramioErrorResponse(VolleyError error) {
        handlePanoramioError();
    }

    private void handlePanoramioError() {
        Toast.makeText(this, "Panoramio error", Toast.LENGTH_LONG).show();
    }
}
