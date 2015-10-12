package com.anddevbg.lawa.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.networking.Connectivity;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final String URI_SCHEME = "ABC";
    private static final String REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(REFRESH_ACTION)) {
            updateWidgetContent(context);
        }
    }

    private void updateWidgetContent(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.refresh_widget_button, buildPendingIntent(context));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName myWidget = new ComponentName(context, WeatherWidgetProvider.class);
        int[] appwiIds = appWidgetManager.getAppWidgetIds(myWidget);
        widgetUpdate(context, remoteViews, appwiIds);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final Connectivity connectivity = Connectivity.getInstance(context);
        if (connectivity.isConnecting()) {
            for (int i = 0; i < appWidgetIds.length; ++i) {
                Intent intent = new Intent(context, WeatherWidgetService.class);
                Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(appWidgetIds));
                intent.setData(data);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                remoteViews.setRemoteAdapter(R.id.stack_view, intent);
                remoteViews.setOnClickPendingIntent(R.id.widget_frame_layout, buildPendingIntent(context));

                remoteViews.setEmptyView(R.id.widget_frame_layout, R.id.refresh_widget_button);
                buildPendingIntent(context);
                widgetUpdate(context, remoteViews, appWidgetIds);
            }
        } else {
            Log.d("widgetz", "no internet connection");
        }
    }

    public static void widgetUpdate(Context context, RemoteViews remoteViews, int[] appWidgetIds) {
        Log.d("wiidget", "in widgetUpdate");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    private static PendingIntent buildPendingIntent(Context context) {
        Log.d("wiidget", "in buildPendingIntent");
        Intent refreshIntent = new Intent();
        refreshIntent.setAction(REFRESH_ACTION);
        return PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
