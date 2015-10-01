package com.anddevbg.lawa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.networking.Connectivity;

/**
 * Created by adri.stanchev on 24/09/2015.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String URI_SCHEME = "ABC";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Log.d("widgetz", "after if in onReceive");
        }
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Connectivity connectivity = Connectivity.getInstance(context);
        if (connectivity.isConnecting()) {
            for (int i = 0; i < appWidgetIds.length; ++i) {
                Intent intent = new Intent(context, WeatherWidgetService.class);
                Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(appWidgetIds));
                intent.setData(data);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                remoteViews.setRemoteAdapter(R.id.stack_view, intent);

                appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        } else {
            Log.d("widgetz", "No internet connection");

        }
    }
}
