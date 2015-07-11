package com.anddevbg.lawa.Networking;

import com.android.volley.RequestQueue;

/**
 * Created by adri.stanchev on 10/07/2015.
 */
public interface Requestable {
    /*
    Gets a request queue
     */
    public RequestQueue getRequestQueue();
    /*
    Noifies that request has failed
     */
    public void onErrorRequestQueue();
}
