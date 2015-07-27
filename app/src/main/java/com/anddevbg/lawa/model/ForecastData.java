package com.anddevbg.lawa.model;

/**
 * Created by adri.stanchev on 22/07/2015.
 */
public class ForecastData {

    private String imageUrl;
    private String mDay;
    private float mMinimalTemperature;
    private float mMaximalTemperature;

    public ForecastData() {

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public float getmMinimalTemperature() {
        return mMinimalTemperature;
    }

    public void setmMinimalTemperature(float mMinimalTemperature) {
        this.mMinimalTemperature = mMinimalTemperature;
    }

    public float getmMaximalTemperature() {
        return mMaximalTemperature;
    }

    public void setmMaximalTemperature(float mMaximalTemperature) {
        this.mMaximalTemperature = mMaximalTemperature;
    }

}
