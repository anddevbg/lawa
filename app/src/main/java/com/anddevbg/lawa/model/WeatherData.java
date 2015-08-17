package com.anddevbg.lawa.model;

import java.io.Serializable;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherData implements Serializable {

    private int mCurrent;
    private int mHumidity;
    private int mmWindSpeed;
    private String mCityName;
    private String mWeatherImageUrl;
    private String mTimeLastRefresh;

    public WeatherData() {
    }

    public String getDescription() {
        return mTimeLastRefresh;
    }

    public void setDescription(String description) {
            this.mTimeLastRefresh= description;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int current) {
        this.mCurrent = current;
    }

    public int getMin() {
        return mHumidity;
    }

    public void setMin(int min) {
        this.mHumidity = min;
    }

    public int getMax() {
        return mmWindSpeed;
    }

    public void setMax(int max) {
        this.mmWindSpeed = max;
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
                ", mHumidity=" + mHumidity +
                ", mWindSpeed=" + mmWindSpeed +
                ", mCityName='" + mCityName + '\'' +
                ", mWeatherImageUrl='" + mWeatherImageUrl + '\'' +
                ", mTimeLastRefresh=" + mTimeLastRefresh +
                '}';
    }

}
