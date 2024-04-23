package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.core.MapboxService;

/**
 * Base class for entry points to Mapbox V6 geocoding: forward geocoding, reverse geocoding
 * and batch geocoding. See derived classes for more information.
 * @param <T> response type.
 */
public abstract class MapboxV6BaseGeocoding<T> extends MapboxService<T, V6GeocodingService> {

  @NonNull
  protected abstract String accessToken();

  @Nullable
  protected abstract Boolean permanent();

  @Nullable
  protected abstract String clientAppName();

  @NonNull
  @Override
  protected abstract String baseUrl();

  protected MapboxV6BaseGeocoding() {
    super(V6GeocodingService.class);
  }

  /**
   * Base class for Mapbox V6 Geocoding Builders.
   * @param <T> type of Builder
   */
  public abstract static class BaseBuilder<T> {

    protected abstract T accessToken(@NonNull String accessToken);

    /**
     * Specify whether you intend to store the results of the query. Backend default is false.
     *
     * @param permanent specify whether you intend to store the results
     * @return this builder for chaining options together
     *
     * @see <a href="https://docs.mapbox.com/api/search/geocoding/#storing-geocoding-results">Storing Geocoding Results</a>
     */
    public abstract T permanent(@NonNull Boolean permanent);

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     */
    public abstract T clientAppName(@NonNull String clientAppName);

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     */
    public abstract T baseUrl(@NonNull String baseUrl);
  }
}
