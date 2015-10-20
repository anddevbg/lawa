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
        mGoogleApiClient = googleApiClient;
        mAutocompleteFilter = filter;
        latLngBounds = bounds;
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    mResultList = getAutoComplete(charSequence);
                    if (mResultList != null) {
                        filterResults.values = mResultList;
                        filterResults.count = mResultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutoComplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient,
                    constraint.toString(), latLngBounds, mAutocompleteFilter);
            AutocompletePredictionBuffer autocompletePredictions = results.await(10, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                autocompletePredictions.release();
                return null;
            }
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getDescription()));
            }
            autocompletePredictions.release();
            return resultList;
        }
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
