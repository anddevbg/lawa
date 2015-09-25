package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.WeatherData;

import java.util.List;

/**
 * Created by adri.stanchev on 25/09/2015.
 */
public class WeatherWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("widgetz", "in weatherWidgetService");
        return new StackRemoteViews(getApplicationContext(), intent);
    }

}
class StackRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<WeatherData> mWeatherDataList;
    private int mAppWidgetId;
    private WeatherDatabaseManager mDatabaseManager;

    public StackRemoteViews(Context context, Intent intent) {
        Log.d("widgetz", "in StackRemoteViews constructor");
        mDatabaseManager = WeatherDatabaseManager.getInstance();
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mWeatherDataList = mDatabaseManager.showAll();
        Log.d("widgetz", "is: " + mWeatherDataList.toString());

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mWeatherDataList.clear();

    }

    @Override
    public int getCount() {
        return mWeatherDataList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        String[] names = new String[]{"Varna", "Burgas", "Sofia", "Yambol"};
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stack_view_item_layout);
        for(int y=0; y<names.length; y++) {
            remoteViews.setTextViewText(R.id.widget_temperature_text_view, names[y]);
        }
        Log.d("widgetz", "city name iss ss: " + mWeatherDataList.get(0).getCityName());
//        Bundle extras = new Bundle();
//        extras.putInt(WeatherWidget.EXTRA_ITEM, i);
//        Intent fillIntent = new Intent();
//        fillIntent.putExtras(extras);
//        remoteViews.setOnClickFillInIntent(R.id.widget_city_text_view, fillIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
