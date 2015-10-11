package com.anddevbg.lawa.model;

/**
 * Created by adri.stanchev on 22/07/2015.
 */
public class ForecastData {

    private String imageUrl;
    private String mDay;
    private double mMinimalTemperature;
    private double mMaximalTemperature;
    private String mDescription;
    private String mCityName;

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }



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

    public double getMinimalTemperature() {
        return mMinimalTemperature;
    }

    public void setMinimalTemperature(double mMinimalTemperature) {
        this.mMinimalTemperature = mMinimalTemperature;
    }

    public double getMaximalTemperature() {
        return mMaximalTemperature;
    }

    public void setMaximalTemperature(double mMaximalTemperature) {
        this.mMaximalTemperature = mMaximalTemperature;
    }

}
