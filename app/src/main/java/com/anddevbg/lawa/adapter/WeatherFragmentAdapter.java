package com.anddevbg.lawa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.ui.activity.weather.WeatherActivity;
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
    public int getItemPosition(Object object) {
        return WeatherFragmentAdapter.POSITION_NONE;
    }

//    public void removeWeatherData(int position) {
//        if(mWeatherData.size() > 0) {
//            mWeatherData.remove(position);
//        }
//        notifyDataSetChanged();
//    }

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
