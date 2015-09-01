package com.anddevbg.lawa;

import android.app.Application;
import android.content.Context;

import com.anddevbg.lawa.networking.NetworkRequestManager;

/**
 * Created by adri.stanchev on 12/07/2015.
 */
public class LawaApplication extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkRequestManager.setup(this);
        instance = this;
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
