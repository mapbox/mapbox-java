package com.mapbox.api.routetiles.v1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mapbox.api.routetiles.v1.versions.MapboxRouteTileVersions;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.geojson.BoundingBox;

import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;

/**
 * The Route Tiles API allows the download of route tiles for the purpose of offline
 * routing. To get a list of the versions, use the
 * {@link MapboxRouteTileVersions} API.
 *
 * @since 4.1.0
 */
@AutoValue
public abstract class MapboxRouteTiles extends MapboxService<ResponseBody, RouteTilesService> {

  protected MapboxRouteTiles() {
    super(RouteTilesService.class);
  }

  @Override
  protected Call<ResponseBody> initializeCall() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      formatBoundingBox(boundingBox()),
      version(),
      accessToken()
    );
  }

  @Override
  protected synchronized OkHttpClient getOkHttpClient() {
    if (okHttpClient == null) {
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
      if (isEnableDebug()) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
      }
      Interceptor interceptor = interceptor();
      if (interceptor != null) {
        httpClient.addInterceptor(interceptor);
      }
      Interceptor networkInterceptor = networkInterceptor();
      if (networkInterceptor != null) {
        httpClient.addNetworkInterceptor(networkInterceptor);
      }

      okHttpClient = httpClient.build();
    }
    return okHttpClient;
  }

  @Nullable
  abstract String clientAppName();

  @NonNull
  abstract BoundingBox boundingBox();

  @NonNull
  abstract String version();

  @NonNull
  abstract String accessToken();

  @Nullable
  abstract Interceptor interceptor();

  @Nullable
  abstract Interceptor networkInterceptor();

  @Override
  protected abstract String baseUrl();

  /**
   * Build a new {@link MapboxRouteTiles} object.
   *
   * @return a {@link Builder} object for creating this object
   * @since 4.1.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxRouteTiles.Builder()
      .baseUrl(Constants.BASE_API_URL);
  }

  /**
   * Returns the builder which created this instance of {@link MapboxRouteTiles} and allows for
   * modification and building a new route tiles request with new information.
   *
   * @return {@link Builder} with the same variables set as this route tiles object
   * @since 4.1.0
   */
  public abstract Builder toBuilder();

  /**
   * This builder is used to create a new request to the Mapbox Route Tiles API. At a bare minimum,
   * your request must include an access token, a {@link BoundingBox}, and a version.
   *
   * @since 4.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The bounding box of which to download map route tiles.
     *
     * @param boundingBox of which to download map route tiles
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    public abstract Builder boundingBox(@NonNull BoundingBox boundingBox);

    /**
     * The version of map tiles being requested. To get a list of the versions, use the
     * {@link MapboxRouteTileVersions} API.
     *
     * @param version of which to download
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    public abstract Builder version(@NonNull String version);


    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Route Tiles API
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);

    /**
     * Adds an optional interceptor to set in the OkHttp client.
     *
     * @param interceptor to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder interceptor(Interceptor interceptor);

    /**
     * Adds an optional network interceptor to set in the OkHttp client.
     *
     * @param interceptor to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder networkInterceptor(Interceptor interceptor);

    abstract MapboxRouteTiles autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, and creates a new {@link MapboxRouteTiles} object with the values provided.
     *
     * @return a new instance of Mapbox Route Tiles
     * @throws ServicesException when a provided parameter is detected to be incorrect
     * @since 4.1.0
     */
    public MapboxRouteTiles build() {
      MapboxRouteTiles mapboxRouteTiles = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(mapboxRouteTiles.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }

      return mapboxRouteTiles;
    }
  }

  private String formatBoundingBox(BoundingBox boundingBox) {
    return String.format(Locale.US,
      "%f,%f;%f,%f",
      boundingBox.west(),
      boundingBox.south(),
      boundingBox.east(),
      boundingBox.north()
    );
  }
}
