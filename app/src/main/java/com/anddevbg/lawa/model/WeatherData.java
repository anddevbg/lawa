package com.anddevbg.lawa.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by tpenkov on 15.6.2015 г..
 */
public class WeatherData implements Serializable {



    private int mCurrent;
    private int mHumidity;
    private double mWindSpeed;
    private String mCityName;
    private String mDescription;
    private double mLongitude;
    private double mLatitude;

    private int id;

    public WeatherData() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWindSpeed(double mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public void setHumidity(int mHumidity) {
        this.mHumidity = mHumidity;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public void setTimeLastRefresh(String mTimeLastRefresh) {
        this.mDescription = mTimeLastRefresh;
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
        return mDescription;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
    }

    public int getMin() {
        return mHumidity;
    }

    public double getMax() {
        return mWindSpeed;
    }

    public String getCityName() {
        return mCityName;
    }


    @Override
    public String toString() {
        return "WeatherData{" +
                "cityId=" + id +
                ", mLongitude=" + mLongitude +
                ", mLatitude=" + mLatitude +
                '}';
    }
}
