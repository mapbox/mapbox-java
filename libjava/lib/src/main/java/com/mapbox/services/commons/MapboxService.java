package com.mapbox.services.commons;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by antonio on 4/7/16.
 */
public abstract class MapboxService<T> {

    private boolean enableDebug = false;

    public abstract Response<T> executeCall() throws IOException;
    public abstract void enqueueCall(Callback<T> callback);
    public abstract void cancelCall();
    public abstract Call<T> cloneCall();

    public abstract Observable<T> getObservable();

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public OkHttpClient getOkHttpClient() {
        if (isEnableDebug()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            return httpClient.build();
        } else {
            return new OkHttpClient();
        }
    }

}
