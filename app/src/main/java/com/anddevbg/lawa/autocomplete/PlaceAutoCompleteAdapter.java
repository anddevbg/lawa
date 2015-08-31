package com.anddevbg.lawa.autocomplete;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by adri.stanchev on 28/08/2015.
 */
public class PlaceAutoCompleteAdapter extends ArrayAdapter<PlaceAutoCompleteAdapter.PlaceAutocomplete> implements Filterable {

    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private AutocompleteFilter mAutocompleteFilter;
    private LatLngBounds latLngBounds;

    public PlaceAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter filter) {
        super(context, resource);
        Log.d("asd", "PlaceAutoCompleteAdapter constructor");
        mGoogleApiClient = googleApiClient;
        mAutocompleteFilter = filter;
        latLngBounds = bounds;
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        Log.d("asd", "GET ITEM");
        return mResultList.get(position);
    }

    @Override
    public int getCount() {
        Log.d("asd", "GET COUNT");
        return mResultList.size();
    }

    @Override
    public Filter getFilter() {
        Log.d("asd", "FILTER");
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("asd", "perform filtering");
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    Log.d("asd", "char sequence != null");
                    mResultList = getAutoComplete(charSequence);
                    if (mResultList != null) {
                        Log.d("asd", "mResultList != NULL");
                        filterResults.values = mResultList;
                        filterResults.count = mResultList.size();
                    } else {
                        Log.d("asd", "result list is NULL :(");
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    Log.d("asd", "notifying data set changed");
                    notifyDataSetChanged();
                } else {
                    Log.d("asd", "data set invalidating :(");
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutoComplete(CharSequence charSequence) {
        if (mGoogleApiClient.isConnected()) {
            Log.d("asd", "getting autocomplete predictions");
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient,
                    charSequence.toString(), latLngBounds, mAutocompleteFilter);
            Log.d("asd", "after getAutoCompletePredictions call");
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
            Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Log.d("asd", "Error connecting to API " + status.toString());
                autocompletePredictions.release();
                return null;
            }
            Log.d("asd", "query completed. received " + autocompletePredictions.getCount() + " predictions");
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getDescription()));
            }
            autocompletePredictions.release();
            return resultList;
        }
        Log.d("asd", "Google Api Client not connected");
        return null;
    }

    public class PlaceAutocomplete {
        public CharSequence description;

        public PlaceAutocomplete(CharSequence description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}
