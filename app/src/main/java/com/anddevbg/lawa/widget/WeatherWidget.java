package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.WeatherData;

import java.util.List;
import java.util.Set;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidget extends AppWidgetProvider {

    private List<WeatherData> mWeatherDataList;
    private WeatherDatabaseManager mDatabaseManager;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mDatabaseManager = WeatherDatabaseManager.getInstance();
        mDatabaseManager.getDatabase();
        mWeatherDataList = mDatabaseManager.showAll();

        SharedPreferences preferences = context.getSharedPreferences("prefs", 0);
        int currentTemp = preferences.getInt("temp", 5);
        Log.d("widget", "current temp is "+ currentTemp);
        for (int i=0; i<appWidgetIds.length; i++) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            String cityName = mWeatherDataList.get(mWeatherDataList.size()-1).getCityName();
            int temperature = mWeatherDataList.get(0).getCurrent();
            Log.d("widget", "temp and city are " + temperature + " " + cityName);
            remoteViews.setTextViewText(R.id.widget_temperature_text_view, String.valueOf(currentTemp));
            remoteViews.setTextViewText(R.id.widget_city_text_view, cityName);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }


    }
}
