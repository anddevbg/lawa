package com.anddevbg.lawa;

import android.app.Application;

import com.anddevbg.lawa.networking.NetworkRequestManagerImpl;

/**
 * Created by adri.stanchev on 12/07/2015.
 */
public class LawaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkRequestManagerImpl.setup(this);
    }
}
