package com.mapbox.core;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * Mapbox specific services used internally within the SDK.
 *
 * @param <T> Type parameter.
 * @since 1.0.0
 */
public abstract class MapboxService<T> {

  private boolean enableDebug;
  private OkHttpClient okHttpClient;
  private okhttp3.Call.Factory callFactory;

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a response specific to the
   * API implementing this class.
   *
   * @return the response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred
   * @since 1.0.0
   */
  public abstract Response<T> executeCall() throws IOException;

  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a response specific
   * to the API implementing this class. Use this method to make a request on the Main Thread.
   *
   * @param callback a {@link Callback} which is used once the API response is created.
   * @since 1.0.0
   */
  public abstract void enqueueCall(Callback<T> callback);

  /**
   * Wrapper method for Retrofits {@link Call#cancel()} call, important to manually cancel call if
   * the user dismisses the calling activity or no longer needs the returned results.
   *
   * @since 1.0.0
   */
  public abstract void cancelCall();

  /**
   * Wrapper method for Retrofits {@link Call#clone()} call, useful for getting call information.
   *
   * @return cloned call
   * @since 1.0.0
   */
  public abstract Call<T> cloneCall();

  public boolean isEnableDebug() {
    return enableDebug;
  }

  /**
   * Enable for more verbose log output while making request.
   *
   * @param enableDebug true if you'd like Okhttp to log
   * @since 3.0.0
   */
  public void enableDebug(boolean enableDebug) {
    this.enableDebug = enableDebug;
  }

  /**
   * Gets the call factory for creating {@link Call} instances.
   *
   * @return the call factory, or the default OkHttp client if it's null.
   * @since 2.0.0
   */
  public okhttp3.Call.Factory getCallFactory() {
    return callFactory;
  }

  /**
   * Specify a custom call factory for creating {@link Call} instances.
   *
   * @param callFactory implementation
   * @since 2.0.0
   */
  public void setCallFactory(okhttp3.Call.Factory callFactory) {
    this.callFactory = callFactory;
  }

  /**
   * Used Internally.
   *
   * @return OkHttpClient
   * @since 1.0.0
   */
  public OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      if (isEnableDebug()) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        okHttpClient = httpClient.build();
      } else {
        okHttpClient = new OkHttpClient();
      }
    }

    return okHttpClient;
  }
}
