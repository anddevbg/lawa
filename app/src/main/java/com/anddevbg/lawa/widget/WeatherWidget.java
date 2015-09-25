package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.anddevbg.lawa.R;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidget extends AppWidgetProvider {

//    public final static String EXTRA_ITEM = "com.anddevbg.lawa.EXTRA_ITEM";
//    public final static String TOAST_ACTION = "com.anddevbg.lawa.TOAST_ACTION";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, WeatherWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setRemoteAdapter(R.id.stack_view, intent);
            Log.d("widgetz", "after setting remote adapter");

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        mDatabaseManager = WeatherDatabaseManager.getInstance();
//        mDatabaseManager.getDatabase();
//        mWeatherDataList = mDatabaseManager.showAll();

//        SharedPreferences preferences = context.getSharedPreferences("prefs", 0);
//        int currentTemp = preferences.getInt("temp", 5);
//        Log.d("widget", "current temp is "+ currentTemp);
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        for (int i=0; i<mWeatherDataList.size(); i++) {
//            String cityName = mWeatherDataList.get(mWeatherDataList.size()-1).getCityName();
//            remoteViews.setTextViewText(R.id.widget_temperature_text_view, String.valueOf(currentTemp));
//            remoteViews.setTextViewText(R.id.widget_city_text_view, cityName);
//            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
//        }


    }
}
