package com.anddevbg.lawa.networking;

import android.content.Context;
import android.util.SparseArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by adri.stanchev on 12/07/2015.
 */
public class NetworkRequestManager implements IRequestManager<Request<?>>, RequestQueue.RequestFinishedListener<Object> {

    private static NetworkRequestManager sInstance = null;

    private RequestQueue mRequestQueue;

    private SparseArray<Request<?>> mOnGoingRequests;

    public static NetworkRequestManager getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkRequestManager();
        }
        return sInstance;
    }

    public static void setup(Context context) {
        NetworkRequestManager manager = getInstance();
        manager.init(context);
    }

    private void init(Context context) {
        mOnGoingRequests = new SparseArray<>();

        mRequestQueue =  Volley.newRequestQueue(context);
        mRequestQueue.addRequestFinishedListener(this);
    }

    @Override
    public int performRequest(Request<?> request) {
        final int requestId = request.hashCode();
        mOnGoingRequests.put(requestId, request);
        mRequestQueue.add(request);
        return requestId;
    }

    @Override
    public void cancelRequest(int requestId) {
        Request<?> request = mOnGoingRequests.get(requestId);
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
    }

    @Override
    public void onRequestFinished(Request<Object> request) {
        mOnGoingRequests.remove(request.hashCode());
    }
}
