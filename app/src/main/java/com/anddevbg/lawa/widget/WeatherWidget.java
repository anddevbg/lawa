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
import com.anddevbg.lawa.ui.activity.weather.WeatherActivity;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String URI_SCHEME = "ABC";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, WeatherWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(appWidgetIds));
            intent.setData(data);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setRemoteAdapter(R.id.stack_view, intent);

            Intent goToAppIntent = new Intent();
            goToAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, goToAppIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
            remoteViews.setOnClickPendingIntent(R.id.widget_frame_layout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }


    }
}
