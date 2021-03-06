package com.anddevbg.lawa.networking;

/**
 * Created by adri.stanchev on 12/07/2015.
 */
public interface IRequestManager<T> {

    /**
     *
     * @param request
     * @return request id that can be used to cancel the request.
     */
    int performRequest(T request);

    /**
     *
     * @param requestId
     */
    void cancelRequest(int requestId);
}
