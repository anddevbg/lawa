package com.anddevbg.lawa.ui.activity.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.autocomplete.PlaceAutoCompleteAdapter;
import com.anddevbg.lawa.weather.ISearchCityCallback;
import com.anddevbg.lawa.weather.SearchCityWrapper;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 18/08/2015.
 */
public class SearchCityActivity extends FragmentActivity implements ISearchCityCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AutoCompleteTextView searchCityEditText;
    private Button searchCityButton;
    private ListView searchCityListView;
    private List<String> cityList;
    private int current_request_code = 2;
    private String[] myCities;
    private GoogleApiClient googleApiClient;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_activity);
        setupGoogleApiClient();
        myCities = getResources().getStringArray(R.array.city_list);
        initControls();

        searchCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCityWrapper searchCityWrapper = new SearchCityWrapper(searchCityEditText.getText().toString());
                searchCityWrapper.receiveSearchCityList(SearchCityActivity.this);
            }
        });
        searchCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String c1Name = (String) adapterView.getItemAtPosition(position);
                Intent i = getIntent();
                i.putExtra("c1name", c1Name);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1, googleApiClient,
                BOUNDS_GREATER_SYDNEY, null);
        searchCityEditText.setAdapter(placeAutoCompleteAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setupGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, R.string.google_maps_key , this)
                .addConnectionCallbacks(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        googleApiClient.connect();

    }

    private void initControls() {
        searchCityEditText = (AutoCompleteTextView) findViewById(R.id.search_city_edit_text);
        searchCityButton = (Button) findViewById(R.id.button_search_city);
        searchCityListView = (ListView) findViewById(R.id.city_list);

    }

    @Override
    public void onJSONResponse(JSONObject response) {
        cityList = new ArrayList<>();
        try {
            JSONArray searchCityArray = response.getJSONArray("list");
            for (int i = 0; i < searchCityArray.length(); i++) {
                JSONObject city = searchCityArray.getJSONObject(i);
                String searchNameCity = city.getString("name");
                cityList.add(searchNameCity);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityList);
        searchCityListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onJSONError(VolleyError error) {
        Log.d("asd", "JSOn error maybe " + error.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("asd", "connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
