package com.mapbox.services.api;

import com.mapbox.services.Constants;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

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

  private final static Logger logger = Logger.getLogger(MapboxService.class.getSimpleName());

  private boolean enableDebug = false;
  private OkHttpClient okHttpClient = null;
  private okhttp3.Call.Factory callFactory = null;

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

  /**
   * Computes a full user agent header of the form: MapboxJava/1.2.0 Mac OS X/10.11.5 (x86_64)
   *
   * @param clientAppName Application Name
   * @return {@link String}
   * @since 1.0.0
   */
  public static String getHeaderUserAgent(String clientAppName) {
    String userAgent;

    try {
      String osName = System.getProperty("os.name");
      String osVersion = System.getProperty("os.version");
      String osArch = System.getProperty("os.arch");

      if (isEmpty(osName) || isEmpty(osVersion) || isEmpty(osArch)) {
        userAgent = Constants.HEADER_USER_AGENT;
      } else {
        String baseUa = String.format(
          Locale.US, "%s %s/%s (%s)", Constants.HEADER_USER_AGENT, osName, osVersion, osArch);
        userAgent = isEmpty(clientAppName) ? baseUa : String.format(Locale.US, "%s %s", clientAppName, baseUa);
      }

    } catch (Exception exception) {
      userAgent = Constants.HEADER_USER_AGENT;
    }

    // Print userAgent and version number
    String libVersion  = Constants.getVersion();


      //get current date and time in format 2016/11/16 12:08:43
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();

    logger.fine(String.format(Locale.US, "%s UserAgent - %s, JavaSDKVersion - %s ",dateFormat.format(date), userAgent, libVersion));

    return userAgent;
  }
}
