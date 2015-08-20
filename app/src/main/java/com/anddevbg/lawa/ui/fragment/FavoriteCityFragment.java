package com.anddevbg.lawa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by adri.stanchev on 04/08/2015.
 */
public class FavoriteCityFragment extends BaseWeatherFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initLocation();
        return view;
    }

    @Override
    public void initLocation() {
        super.initLocation();
        mLastKnownLocation = null;
    }

}



