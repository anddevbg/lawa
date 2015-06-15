package com.anddevbg.lawa.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherData implements Serializable {

    private int mCurrent;
    private int mMin;
    private int mMax;
    private String mCityName;
    private String mWeatherImageUrl;

    public WeatherData() {
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int current) {
        this.mCurrent = current;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        this.mMin = min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }

    public String getWeatherImage() {
        return mWeatherImageUrl;
    }

    public void setWeatherImage(String weatherImageUrl) {
        this.mWeatherImageUrl = weatherImageUrl;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "mCurrent=" + mCurrent +
                ", mMin=" + mMin +
                ", mMax=" + mMax +
                ", mCityName='" + mCityName + '\'' +
                ", mWeatherImageUrl='" + mWeatherImageUrl + '\'' +
                '}';
    }
}
