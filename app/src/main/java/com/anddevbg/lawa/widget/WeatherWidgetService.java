package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.weather.ICurrentWeatherCallback;
import com.anddevbg.lawa.weather.LocationCurrentWeatherWrapper;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 25/09/2015.
 */
public class WeatherWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViews(getApplicationContext(), intent);
    }

}

class StackRemoteViews implements RemoteViewsService.RemoteViewsFactory, ICurrentWeatherCallback {

    private Context mContext;
    private List<WeatherData> mWeatherDataList;
    private int mAppWidgetId;
    private int[] mTempArray;
    private List<Float> mCurrentTemperatureList;
    private List<String> mIconArray;
    private WeatherDatabaseManager mDatabaseManager;

    public StackRemoteViews(Context context, Intent intent) {
        mDatabaseManager = WeatherDatabaseManager.getInstance();
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mCurrentTemperatureList = new ArrayList<>();
        mIconArray = new ArrayList<>();
        mWeatherDataList = mDatabaseManager.showAll();
        mTempArray = new int[10];
        for (int i = 0; i < mWeatherDataList.size(); i++) {
            WeatherData data = mWeatherDataList.get(i);
            Location location = new Location("");
            location.setLongitude(data.getLongitude());
            location.setLatitude(data.getLatitude());
            LocationCurrentWeatherWrapper locationCurrentWeatherWrapper = new LocationCurrentWeatherWrapper(location);
            locationCurrentWeatherWrapper.getWeatherUpdate(this);
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
        if(mIconArray != null && mCurrentTemperatureList != null) {
            Log.d("widgetz", "mIcon array is " + mIconArray.toString());
            Log.d("widgetz", "mCurrentTemp array is " + mCurrentTemperatureList.toString());
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.stack_view_item_layout);
        remoteViews.setTextViewText(R.id.widget_temperature_text_view, mWeatherDataList.get(i).getCityName());
        if (i < mCurrentTemperatureList.size()) {
            remoteViews.setTextViewText(R.id.city_text_widget_view, String.valueOf(mCurrentTemperatureList.get(i)) + "°C");
        }
        if (mIconArray.size() > 0) {
            try {
                Bitmap b = Picasso.with(mContext).load(mIconArray.get(i)).get();
                remoteViews.setImageViewBitmap(R.id.widget_image_view, b);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return remoteViews;
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

    @Override
    public void onWeatherApiResponse(JSONObject result) {
        try {
            JSONArray weather = result.getJSONArray("weather");
            JSONObject w1 = weather.getJSONObject(0);
            String pngFileUrl = w1.getString("icon");
            String fullForecastIconUrl = "http://openweathermap.org/img/w/" + pngFileUrl + ".png";
            mIconArray.add(fullForecastIconUrl);

            JSONObject main = result.getJSONObject("main");
            float currentTemp = (float) main.getDouble("temp");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            float newCurrentTemp = Float.valueOf(decimalFormat.format(currentTemp));
            mCurrentTemperatureList.add(newCurrentTemp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeatherApiErrorResponse(VolleyError error) {

    }
}
