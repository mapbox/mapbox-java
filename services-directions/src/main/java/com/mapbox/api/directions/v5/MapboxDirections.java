package com.mapbox.api.directions.v5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.utils.FormatUtils;
import com.mapbox.core.MapboxService;
import com.mapbox.core.utils.ApiCallHelper;

import java.io.IOException;

import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route can be
 * returned with geometries, turn-by-turn instructions, and much more. The Mapbox Directions API
 * supports routing for driving cars (including live traffic), riding bicycles and walking.
 * <p>
 * Requested routes can include as much as 25 coordinates anywhere on earth (except the traffic
 * profile which support up to 3 coordinates, contact Mapbox Support if you'd like to extend this limit).
 * </p>
 * <p>
 * Requesting a route at a bare minimal must include, a Mapbox access token, destination, and an
 * origin.
 * </p>
 *
 * @see <a href="https://www.mapbox.com/android-docs/java-sdk/overview/directions/">Android
 * Directions documentation</a>
 * @see <a href="https://www.mapbox.com/api-documentation/navigation/#directions">Directions API
 * documentation</a>
 */
@AutoValue
public abstract class MapboxDirections extends
  MapboxService<DirectionsResponse, DirectionsService> {

  protected MapboxDirections() {
    super(DirectionsService.class);
  }

  @Override
  protected String baseUrl() {
    return routeOptions().baseUrl();
  }

  @Override
  protected Call<DirectionsResponse> initializeCall() {
    if (usePostMethod() == null) {
      return callForUrlLength();
    }

    if (usePostMethod()) {
      return post();
    }

    return get();
  }

  private Call<DirectionsResponse> callForUrlLength() {
    Call<DirectionsResponse> get = get();
    if (get.request().url().toString().length() < MAX_URL_SIZE) {
      return get;
    }
    return post();
  }

  private Call<DirectionsResponse> get() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      routeOptions().user(),
      routeOptions().profile(),
      FormatUtils.formatCoordinates(routeOptions().coordinates()),
      routeOptions().accessToken(),
      routeOptions().alternatives(),
      routeOptions().geometries(),
      routeOptions().overview(),
      FormatUtils.formatRadiuses(routeOptions().radiuses()),
      routeOptions().steps(),
      FormatUtils.formatBearings(routeOptions().bearings()),
      routeOptions().continueStraight(),
      FormatUtils.formatAnnotations(routeOptions().annotations()),
      routeOptions().language(),
      routeOptions().roundaboutExits(),
      routeOptions().voiceInstructions(),
      routeOptions().bannerInstructions(),
      routeOptions().voiceUnits(),
      routeOptions().exclude(),
      FormatUtils.formatApproaches(routeOptions().approaches()),
      FormatUtils.formatWaypointIndices(routeOptions().waypointIndices()),
      FormatUtils.formatWaypointNames(routeOptions().waypointNames()),
      FormatUtils.formatWaypointTargets(routeOptions().waypointTargets()),
      routeOptions().enableRefresh(),
      routeOptions().walkingSpeed(),
      routeOptions().walkwayBias(),
      routeOptions().alleyBias(),
      FormatUtils.formatSnappingIncludeClosures(routeOptions().snappingIncludeClosures()),
      routeOptions().arriveBy(),
      routeOptions().departAt()
    );
  }

  private Call<DirectionsResponse> post() {
    return getService().postCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      routeOptions().user(),
      routeOptions().profile(),
      FormatUtils.formatCoordinates(routeOptions().coordinates()),
      routeOptions().accessToken(),
      routeOptions().alternatives(),
      routeOptions().geometries(),
      routeOptions().overview(),
      FormatUtils.formatRadiuses(routeOptions().radiuses()),
      routeOptions().steps(),
      FormatUtils.formatBearings(routeOptions().bearings()),
      routeOptions().continueStraight(),
      FormatUtils.formatAnnotations(routeOptions().annotations()),
      routeOptions().language(),
      routeOptions().roundaboutExits(),
      routeOptions().voiceInstructions(),
      routeOptions().bannerInstructions(),
      routeOptions().voiceUnits(),
      routeOptions().exclude(),
      FormatUtils.formatApproaches(routeOptions().approaches()),
      FormatUtils.formatWaypointIndices(routeOptions().waypointIndices()),
      FormatUtils.formatWaypointNames(routeOptions().waypointNames()),
      FormatUtils.formatWaypointTargets(routeOptions().waypointTargets()),
      routeOptions().enableRefresh(),
      routeOptions().walkingSpeed(),
      routeOptions().walkwayBias(),
      routeOptions().alleyBias(),
      FormatUtils.formatSnappingIncludeClosures(routeOptions().snappingIncludeClosures()),
      routeOptions().arriveBy(),
      routeOptions().departAt()
    );
  }

  @Override
  protected GsonBuilder getGsonBuilder() {
    return super.getGsonBuilder()
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
  }

  /**
   * Wrapper method for Retrofits {@link Call#execute()} call returning a response specific to the
   * Directions API synchronously.
   *
   * @return the Directions v5 response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred
   */
  @Override
  public Response<DirectionsResponse> executeCall() throws IOException {
    Response<DirectionsResponse> response = super.executeCall();
    DirectionsResponseFactory factory = new DirectionsResponseFactory(this);
    return factory.generate(response);
  }

  /**
   * Wrapper method for Retrofits {@link Call#enqueue(Callback)} call returning a response specific
   * to the Directions API.
   *
   * @param callback a {@link Callback} which is used once the {@link DirectionsResponse} is
   *                 created.
   * @since 1.0.0
   */
  @Override
  public void enqueueCall(final Callback<DirectionsResponse> callback) {
    getCall().enqueue(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        DirectionsResponseFactory factory = new DirectionsResponseFactory(MapboxDirections.this);
        Response<DirectionsResponse> generatedResponse = factory.generate(response);
        callback.onResponse(call, generatedResponse);
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        callback.onFailure(call, throwable);
      }
    });
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
      EventListener eventListener = eventListener();
      if (eventListener != null) {
        httpClient.eventListener(eventListener);
      }

      okHttpClient = httpClient.build();
    }
    return okHttpClient;
  }

  @NonNull
  abstract RouteOptions routeOptions();

  @Nullable
  abstract String clientAppName();

  @Nullable
  abstract Interceptor interceptor();

  @Nullable
  abstract Interceptor networkInterceptor();

  @Nullable
  abstract EventListener eventListener();

  @Nullable
  abstract Boolean usePostMethod();

  /**
   * Build a new {@link MapboxDirections} object.
   *
   * @return a {@link Builder} object for creating this object
   */
  public static Builder builder() {
    return new AutoValue_MapboxDirections.Builder();
  }

  /**
   * Returns the builder which created this instance of {@link MapboxDirections} and allows for
   * modification and building a new directions request with new information.
   *
   * @return {@link MapboxDirections.Builder} with the same variables set as this directions object
   * @since 3.0.0
   */
  public abstract Builder toBuilder();

  /**
   * Builder class used to create a new instance of {@link MapboxDirections}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    @NonNull
    public abstract Builder routeOptions(@NonNull RouteOptions routeOptions);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 1.0.0
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

    /**
     * Adds an optional event listener to set in the OkHttp client.
     *
     * @param eventListener to set for OkHttp
     * @return this builder for chaining options together
     */
    public abstract Builder eventListener(EventListener eventListener);

    /**
     * Use POST method to request data.
     * The default is to use GET.
     *
     * @return this builder for chaining options together
     */
    public abstract Builder usePostMethod(@NonNull Boolean usePost);

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, and creates a new {@link MapboxDirections} object with the values provided.
     *
     * @return a new instance of Mapbox Directions
     */
    public abstract MapboxDirections build();
  }
}
