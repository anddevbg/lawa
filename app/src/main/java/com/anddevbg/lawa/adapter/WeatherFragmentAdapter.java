package com.anddevbg.lawa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.ui.fragment.BaseWeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherFragmentAdapter extends FragmentPagerAdapter {

    private List<WeatherData> mWeatherData;

    public WeatherFragmentAdapter(FragmentManager fm) {
        super(fm);
        mWeatherData = new ArrayList<>();
    }

    public void setWeatherData(List<WeatherData> data) {
        mWeatherData.clear();
        mWeatherData.addAll(data);

        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseWeatherFragment baseFragment = (BaseWeatherFragment) super.instantiateItem(container, position);

        return baseFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("asd", "creating weather screen for:");
        Log.d("asd", "position: " + position);
        Log.d("asd", "wd: " + mWeatherData.get(position));
        Log.d("asd", "------------------------------");

        return BaseWeatherFragment.createInstance(mWeatherData.get(position));
    }


    @Override
    public int getCount() {
        return mWeatherData.size();
    }
}
