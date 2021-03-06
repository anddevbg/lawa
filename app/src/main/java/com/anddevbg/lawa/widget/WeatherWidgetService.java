package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.weather.CurrentWeatherWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by adri.stanchev on 25/09/2015.
 */
public class WeatherWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViews(getApplicationContext(), intent);
    }
}

class StackRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<WeatherData> mWeatherDataList;
    private WeatherDatabaseManager mDatabaseManager;
    private List<Location> mLocationList;
    private AppWidgetManager mAppWidgetManager;

    public StackRemoteViews(Context context, Intent intent) {
        mDatabaseManager = WeatherDatabaseManager.getInstance();
        mContext = context;
        mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    @Override
    public void onCreate() {
        mLocationList = new ArrayList<>();
        mWeatherDataList = mDatabaseManager.showAll();
        for (int i = 0; i < mWeatherDataList.size(); i++) {
            WeatherData data = mWeatherDataList.get(i);
            Location location = new Location("");
            location.setLongitude(data.getLongitude());
            location.setLatitude(data.getLatitude());
            mLocationList.add(location);
        }
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
        CurrentWeatherWrapper currentWeatherWrapper = new CurrentWeatherWrapper(mLocationList.get(i));
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stack_view_item_layout);
        try {
            JSONObject object = currentWeatherWrapper.getWeatherUpdateSync().get(30, TimeUnit.SECONDS);
            setUpWidgetInformation(object, remoteViews, i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Intent fillIntent = new Intent();
        Bundle extras = new Bundle();
        extras.putInt(WeatherWidgetProvider.EXTRA_ITEM, i);
        fillIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.stack_widget_item, fillIntent);

        return remoteViews;
    }

    private void setUpWidgetInformation(JSONObject object, RemoteViews remoteViews, int position) {
        try {
            JSONArray weather = object.getJSONArray("weather");
            JSONObject w1 = weather.getJSONObject(0);
            String pngFileUrl = w1.getString("icon");
            String fullForecastIconUrl = "http://openweathermap.org/img/w/" + pngFileUrl + ".png";

            JSONObject main = object.getJSONObject("main");
            double currentTemp =  main.getDouble("temp");
            DecimalFormat decimalFormat = new DecimalFormat("#");
            remoteViews.setTextViewText(R.id.widget_temperature_text_view, mWeatherDataList.get(position).getCityName());
            remoteViews.setTextViewText(R.id.city_text_widget_view, String.valueOf(decimalFormat.format(currentTemp)) +"°C");
            try {
                Bitmap b = Picasso.with(mContext).load(fullForecastIconUrl).get();
                remoteViews.setImageViewBitmap(R.id.widget_image_view, b);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return mWeatherDataList.size();
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
