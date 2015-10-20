package com.anddevbg.lawa.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by adri.stanchev on 01/10/2015.
 */
public class Connectivity {
    private static Connectivity sInstance;
    private Context mContext;
    private ConnectivityManager mConnectivityManager;

    protected Connectivity(Context context) {
        this.mContext = context;
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }
    public static Connectivity getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new Connectivity(context);
        }
        return sInstance;
    }

    public boolean isConnecting() {
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    public boolean isConnected() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
