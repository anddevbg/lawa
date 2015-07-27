package com.anddevbg.lawa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.ui.fragment.CurrentLocationWeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherFragmentAdapter extends FragmentStatePagerAdapter {

    private List<WeatherData> mWeatherData;
    // create adapter to handle the views

    public WeatherFragmentAdapter(FragmentManager fm) {
        super(fm);
        mWeatherData = new ArrayList<>();
    }

    public void setWeatherData(List<WeatherData> data) {
        mWeatherData.clear();
        mWeatherData.addAll(data);

        notifyDataSetChanged();
    }

    // Check where we are and show fragment at each position

    @Override
    public Fragment getItem(int position) {

        Log.d("asd", "position: " + position + " Setting up: " + mWeatherData.get(position).toString());
        return CurrentLocationWeatherFragment.createInstance(mWeatherData.get(position));
    }

    @Override
    public int getCount() {
        return mWeatherData.size();
    }
}
