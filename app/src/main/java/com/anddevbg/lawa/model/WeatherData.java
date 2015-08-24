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
    private String mTimeLastRefresh;
    private double mLongitude;
    private double mLatitude;

    public WeatherData() {
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public String getDescription() {
        return mTimeLastRefresh;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public int getMin() {
        return mHumidity;
    }

    public int getMax() {
        return mmWindSpeed;
    }

    public String getCityName() {
        return mCityName;
    }


    @Override
    public String toString() {
        return "WeatherData{" +
                "mCurrent=" + mCurrent +
                ", mHumidity=" + mHumidity +
                ", mWindSpeed=" + mmWindSpeed +
                ", mCityName='" + mCityName + '\'' +
                ", mTimeLastRefresh=" + mTimeLastRefresh +
                '}';
    }

}
