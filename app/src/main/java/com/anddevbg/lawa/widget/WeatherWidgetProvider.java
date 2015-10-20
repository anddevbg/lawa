package com.anddevbg.lawa.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.networking.Connectivity;
import com.anddevbg.lawa.ui.activity.weather.WeatherActivity;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {


    public static final String URI_SCHEME = "ABC";
    public static final String CLICK_ACTION = "com.anddevbg.lawa.CLICK_";
    public static final String EXTRA_ITEM = "com.anddevbg.lawa.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
//        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(CLICK_ACTION)) {
            Log.d("asdasd", "in onReceive after IF");
//            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Intent goToActivity = new Intent(context, WeatherActivity.class);
            goToActivity.putExtra("position", viewIndex);
            Log.d("widget", "viewIndex is " + viewIndex);
            goToActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(goToActivity);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final Connectivity connectivity = Connectivity.getInstance(context);
        if (connectivity.isConnecting()) {
            for (int i = 0; i < appWidgetIds.length; ++i) {
                Intent intent = new Intent(context, WeatherWidgetService.class);
                Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(appWidgetIds[i]));
                intent.setData(data);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                remoteViews.setRemoteAdapter(R.id.stack_view, intent);
                Intent clickIntent = new Intent(context, WeatherWidgetProvider.class);
                clickIntent.setAction(WeatherWidgetProvider.CLICK_ACTION);
                clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                clickIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

                PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.stack_view);
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

}
