package com.anddevbg.lawa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.ui.fragment.BaseWeatherFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherFragmentAdapter extends FragmentStatePagerAdapter {
    private List<WeatherData> mWeatherData;
    private Map<Integer, BaseWeatherFragment> mPageMap = new HashMap<>();

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
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        BaseWeatherFragment baseWeatherFragment = BaseWeatherFragment.createInstance(mWeatherData.get(position));
        mPageMap.put(position, baseWeatherFragment);
//        return BaseWeatherFragment.createInstance(mWeatherData.get(position));
        return baseWeatherFragment;
    }

    @Override
    public int getCount() {
        return mWeatherData.size();
    }

    public BaseWeatherFragment getFragment(int key) {
        return mPageMap.get(key);
    }

    public void removeView(int index) {
        mWeatherData.remove(index);
        notifyDataSetChanged();
    }

}
