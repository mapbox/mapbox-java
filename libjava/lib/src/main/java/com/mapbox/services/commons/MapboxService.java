package com.mapbox.services.commons;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

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

    public abstract Observable<T> getObservable();

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
     * @return {@link String}
     * @since 1.0.0
     */
    public static String getHeaderUserAgent() {
        String osName;
        String osVersion;
        String osArch;

        try {
            osName = System.getProperty("os.name");
            osVersion = System.getProperty("os.version");
            osArch = System.getProperty("os.arch");
        } catch (Exception e) {
            return Constants.HEADER_USER_AGENT;
        }

        if (TextUtils.isEmpty(osName) || TextUtils.isEmpty(osVersion) || TextUtils.isEmpty(osArch)) {
            return Constants.HEADER_USER_AGENT;
        } else {
            String osInfo = String.format(Locale.US, "%s/%s (%s)", osName, osVersion, osArch);
            return String.format(Locale.US, "%s %s", Constants.HEADER_USER_AGENT, osInfo);
        }
    }
}
