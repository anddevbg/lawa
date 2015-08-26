package com.anddevbg.lawa.ui.activity.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.weather.ISearchCityCallback;
import com.anddevbg.lawa.weather.SearchCityWrapper;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 18/08/2015.
 */
public class SearchCityActivity extends AppCompatActivity implements ISearchCityCallback {

    private EditText searchCityEditText;
    private Button searchCityButton;
    private Button useCurrentLocationButton;
    private ListView searchCityListView;
    private List<String> cityList;
    private int current_request_code = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_activity);
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
                Log.d("asd", "c1name is " + c1Name);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        useCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchCityActivity.this, WeatherActivity.class);
                startActivityForResult(i, current_request_code);
            }
        });
    }

    private void initControls() {
        searchCityEditText = (EditText) findViewById(R.id.search_city_edit_text);
        searchCityButton = (Button) findViewById(R.id.button_search_city);
        searchCityListView = (ListView) findViewById(R.id.city_list);
        useCurrentLocationButton = (Button) findViewById(R.id.button_location);
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
}
