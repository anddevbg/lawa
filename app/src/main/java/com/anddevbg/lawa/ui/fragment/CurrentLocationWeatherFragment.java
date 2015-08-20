package com.anddevbg.lawa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by adri.stanchev on 13/07/2015.
 */

public class CurrentLocationWeatherFragment extends BaseWeatherFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initLocation();
        initWeather();
        return view;
    }

    @Override
    public void initLocation() {
        super.initLocation();
    }

    @Override
    protected void initWeather() {
        super.initWeather();
    }
}
