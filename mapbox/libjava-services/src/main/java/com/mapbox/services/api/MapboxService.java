package com.mapbox.services.api;

import com.mapbox.services.Constants;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.services.commons.utils.TextUtils.isEmpty;

/**
 * Mapbox specific services used internally within the SDK.
 *
 * @param <T> Type parameter.
 * @since 1.0.0
 */
public abstract class MapboxService<T> {

  private boolean enableDebug = false;

  public abstract Response<T> executeCall() throws IOException;

  public abstract void enqueueCall(Callback<T> callback);

  public abstract void cancelCall();

  public abstract Call<T> cloneCall();

  public boolean isEnableDebug() {
    return enableDebug;
  }

  public void setEnableDebug(boolean enableDebug) {
    this.enableDebug = enableDebug;
  }

  /**
   * Used Internally.
   *
   * @return OkHttpClient
   * @since 1.0.0
   */
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

  /**
   * Computes a full user agent header of the form: MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)
   *
   * @param clientAppName Application Name
   * @return {@link String}
   * @since 1.0.0
   */
  public static String getHeaderUserAgent(String clientAppName) {
    try {
      String osName = System.getProperty("os.name");
      String osVersion = System.getProperty("os.version");
      String osArch = System.getProperty("os.arch");

      if (isEmpty(osName) || isEmpty(osVersion) || isEmpty(osArch)) {
        return Constants.HEADER_USER_AGENT;
      } else {
        String baseUa = String.format(
          Locale.US, "%s %s/%s (%s)", Constants.HEADER_USER_AGENT, osName, osVersion, osArch);
        return isEmpty(clientAppName) ? baseUa : String.format(Locale.US, "%s %s", clientAppName, baseUa);
      }

    } catch (Exception exception) {
      return Constants.HEADER_USER_AGENT;
    }
  }
}
