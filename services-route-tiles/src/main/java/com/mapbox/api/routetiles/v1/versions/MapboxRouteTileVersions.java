package com.mapbox.api.routetiles.v1.versions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.routetiles.v1.MapboxRouteTiles;
import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsAdapterFactory;
import com.mapbox.api.routetiles.v1.versions.models.RouteTileVersionsResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;

import retrofit2.Call;

/**
 * The Route Tile Versions API allows the fetching of all available versions of route tiles
 * currently available. It is used in conjunction with the {@link MapboxRouteTiles} API.
 *
 * @since 4.1.0
 */
@AutoValue
public abstract class MapboxRouteTileVersions extends MapboxService<RouteTileVersionsResponse,
  RouteTileVersionsService> {

  protected MapboxRouteTileVersions() {
    super(RouteTileVersionsService.class);
  }

  @Override
  protected GsonBuilder getGsonBuilder() {
    return new GsonBuilder()
      .registerTypeAdapterFactory(RouteTileVersionsAdapterFactory.create());
  }

  @Override
  protected Call<RouteTileVersionsResponse> initializeCall() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      accessToken()
    );
  }

  @Nullable
  abstract String clientAppName();

  @NonNull
  abstract String accessToken();

  @Override
  protected abstract String baseUrl();

  /**
   * Build a new {@link MapboxRouteTileVersions} object.
   *
   * @return a {@link Builder} object for creating this object
   * @since 4.1.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxRouteTileVersions.Builder()
      .baseUrl(Constants.BASE_API_URL);
  }

  /**
   * Returns the builder which created this instance of {@link MapboxRouteTileVersions} and allows for
   * modification and building a new route tile versions request with new information.
   *
   * @return {@link Builder} with the same variables set as this route tile versions object
   * @since 4.1.0
   */
  public abstract Builder toBuilder();

  /**
   * This builder is used to create a new request to the Mapbox Route Tiles API. At a bare minimum,
   * your request must include an access token.
   *
   * @since 4.1.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

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

    abstract MapboxRouteTileVersions autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, and creates a new {@link MapboxRouteTileVersions} object with the values
     * provided.
     *
     * @return a new instance of Mapbox Route Tiles Version
     * @throws ServicesException when a provided parameter is detected to be incorrect
     * @since 4.1.0
     */
    public MapboxRouteTileVersions build() {
      MapboxRouteTileVersions mapboxRouteTileVersions = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(mapboxRouteTileVersions.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }

      return mapboxRouteTileVersions;
    }
  }
}
