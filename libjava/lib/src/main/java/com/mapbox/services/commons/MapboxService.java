package com.mapbox.services.commons;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by antonio on 4/7/16.
 */
public interface MapboxService<T> {

    Response<T> executeCall() throws IOException;
    void enqueueCall(Callback<T> callback);
    void cancelCall();
    Call<T> cloneCall();

    Observable<T> getObservable();

}
