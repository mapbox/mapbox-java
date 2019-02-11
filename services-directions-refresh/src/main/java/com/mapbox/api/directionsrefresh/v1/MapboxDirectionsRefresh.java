package com.mapbox.api.directionsrefresh.v1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshAdapterFactory;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.utils.ApiCallHelper;

import retrofit2.Call;

/**
 * The directions refresh API allows a route to be refreshed via it's annotations. The
 * refreshEnabled parameter would have had to have been specified as true in the original
 * directions request for a refresh to be possible.
 *
 * @since 4.4.0
 */
@AutoValue
public abstract class MapboxDirectionsRefresh extends MapboxService<DirectionsRefreshResponse,
  DirectionsRefreshService> {

  protected MapboxDirectionsRefresh() {
    super(DirectionsRefreshService.class);
  }

  @Override
  protected Call<DirectionsRefreshResponse> initializeCall() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      requestId(),
      routeIndex(),
      legIndex(),
      accessToken()
    );
  }

  abstract String requestId();

  abstract String routeIndex();

  abstract String legIndex();

  abstract String accessToken();

  @Nullable
  abstract String clientAppName();

  @Override
  protected GsonBuilder getGsonBuilder() {
    return super.getGsonBuilder()
      .registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create())
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
  }

  /**
   * Build a new {@link MapboxDirectionsRefresh} builder with default initial values
   *
   * @return a {@link Builder} for creating a default {@link MapboxDirectionsRefresh}
   * @since 4.4.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxDirectionsRefresh.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .routeIndex(0)
      .legIndex(0);
  }

  /**
   * This builder is used to build a new request to the Mapbox Directions Refresh API. A request
   * requires an access token and a request id.
   *
   * @since 4.4.0
   */
  @AutoValue.Builder
  public abstract static class Builder {
    private String[] annotations;

    /**
     * Specified here is the uuid of the initial directions request. The original request must
     * have specified enableRefresh.
     *
     * @param requestId id of the original directions request. This is found in the
     * {@link com.mapbox.api.directions.v5.models.RouteOptions} object.
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder requestId(String requestId);

    /**
     * Index of original route in response.
     *
     * @param routeIndex index of route in response
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder routeIndex(@NonNull String routeIndex);

    /**
     * Index of leg of which to start. The response will include the annotations of the specified
     * leg through the end of the list of legs.
     *
     * @param legIndex index of leg
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder legIndex(@NonNull String legIndex);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link com.mapbox.core.exceptions.ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token
     * @since 4.4.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 4.4.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as endpoint
     * @return this builder
     * @since 4.4.0
     */
    public abstract Builder baseUrl(String baseUrl);

    /**
     * Returns an instance of {@link MapboxDirectionsRefresh} for interacting with the endpoint
     * with the specified values.
     *
     * @return instance of {@link MapboxDirectionsRefresh} with specified attributes
     * @since 4.4.0
     */
    public abstract MapboxDirectionsRefresh build();
  }
}
