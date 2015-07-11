package com.anddevbg.lawa.Networking;

import android.app.Application;
import android.content.Context;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public class ContextHelper extends Application {
    private static ContextHelper contextInstance;

    public static Context getInstance() {
        return contextInstance;
    }
    public static Context getAppContext() {
        return contextInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextInstance = this;
    }
}
