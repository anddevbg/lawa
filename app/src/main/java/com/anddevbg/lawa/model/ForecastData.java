package com.anddevbg.lawa.model;

/**
 * Created by adri.stanchev on 22/07/2015.
 */
public class ForecastData {

    private String imageUrl;
    private String mDay;
    private float mMinimalTemperature;
    private float mMaximalTemperature;
    private String mDescription;

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

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

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public float getMinimalTemperature() {
        return mMinimalTemperature;
    }

    public void setMinimalTemperature(float mMinimalTemperature) {
        this.mMinimalTemperature = mMinimalTemperature;
    }

    public float getMaximalTemperature() {
        return mMaximalTemperature;
    }

    public void setMaximalTemperature(float mMaximalTemperature) {
        this.mMaximalTemperature = mMaximalTemperature;
    }

}
