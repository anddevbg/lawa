package com.anddevbg.lawa.Networking;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public class NetworkRequestManager implements Requestable {
    private static NetworkRequestManager networkRequestManager = null;
    private RequestQueue mRequestQueue;
    private Context mCtx;

    private NetworkRequestManager(Context context) {
        mRequestQueue = getRequestQueue();
        mCtx = context;
    }
    public static synchronized NetworkRequestManager getInstance(Context context) {
        if (networkRequestManager == null) {
            networkRequestManager = new NetworkRequestManager(ContextHelper.getAppContext());
        }
        return networkRequestManager;
    }

    @Override
    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ContextHelper.getAppContext());
        }
        return mRequestQueue;
    }

    @Override
    public void onErrorRequestQueue() {
        Toast.makeText(ContextHelper.getAppContext(), "Error Request Queue", Toast.LENGTH_SHORT)
                .show();
    }
}
