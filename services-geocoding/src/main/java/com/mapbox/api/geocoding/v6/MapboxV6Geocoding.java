package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.geocoding.v6.models.V6Response;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;

import retrofit2.Call;

/**
 * This class gives you access to Mapbox forward and reverse V6 geocoding.
 * <p>
 * Forward geocoding lets you convert location text into geographic coordinates, turning
 * {@code 2 Lincoln Memorial Circle NW} into a {@link Point} with the coordinates
 * {@code -77.050, 38.889}.
 * <p>
 * The reverse geocoding query type allows you to look up a pair of coordinates and returns the
 * geographic features there, including a standardized address or place and full geographic context.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#forward-geocoding">Forward Geocoding</a>
 * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#reverse-geocoding">Reverse Geocoding</a>
 */
@AutoValue
public abstract class MapboxV6Geocoding extends MapboxV6BaseGeocoding<V6Response> {

  @NonNull
  abstract V6RequestOptions requestOptions();

  @Override
  protected GsonBuilder getGsonBuilder() {
    return new GsonBuilder()
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .registerTypeAdapterFactory(GeometryAdapterFactory.create());
  }

  @Override
  protected Call<V6Response> initializeCall() {
    final V6RequestOptions requestOptions = requestOptions();
    if (requestOptions instanceof V6ReverseGeocodingRequestOptions) {
      return reverseGeocodingCall((V6ReverseGeocodingRequestOptions) requestOptions);
    } else if (requestOptions instanceof V6ForwardGeocodingRequestOptions) {
      final V6ForwardGeocodingRequestOptions forwardOptions =
        (V6ForwardGeocodingRequestOptions) requestOptions;
      if (forwardOptions.query() != null) {
        return forwardGeocodingCall(forwardOptions, forwardOptions.query());
      } else {
        return structuredInputCall(forwardOptions);
      }
    } else {
      throw new ServicesException("Unsupported type of request options: " + requestOptions
        .getClass());
    }
  }

  @NonNull
  private Call<V6Response> forwardGeocodingCall(
    @NonNull V6ForwardGeocodingRequestOptions options,
    @NonNull String query
  ) {
    return getService().forwardGeocoding(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      query,
      accessToken(),
      permanent(),
      options.autocomplete(),
      options.apiFormattedBbox(),
      options.country(),
      options.language(),
      options.limit(),
      options.proximity(),
      options.apiFormattedTypes(),
      options.worldview()
    );
  }

  @NonNull
  private Call<V6Response> structuredInputCall(
    @NonNull V6ForwardGeocodingRequestOptions options
  ) {
    return getService().structureInputForwardGeocoding(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      accessToken(),
      options.addressLine1(),
      options.addressNumber(),
      options.street(),
      options.block(),
      options.place(),
      options.region(),
      options.postcode(),
      options.locality(),
      options.neighborhood(),
      permanent(),
      options.autocomplete(),
      options.apiFormattedBbox(),
      options.country(),
      options.language(),
      options.limit(),
      options.proximity(),
      options.apiFormattedTypes(),
      options.worldview()
    );
  }

  @NonNull
  private Call<V6Response> reverseGeocodingCall(
    @NonNull V6ReverseGeocodingRequestOptions options
  ) {
    return getService().reverseGeocoding(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      accessToken(),
      options.longitude(),
      options.latitude(),
      permanent(),
      options.country(),
      options.language(),
      options.limit(),
      options.apiFormattedTypes(),
      options.worldview()
    );
  }

  /**
   * Creates a new {@link MapboxV6Geocoding.Builder} object.
   *
   * @param accessToken    a valid <a href="https://docs.mapbox.com/help/getting-started/access-tokens/">Mapbox Access Token</a> used for API request
   * @param requestOptions options specified for this geocoding request
   * @return Builder object.
   * @throws ServicesException if accessToken is in incorrect format
   *                           or passed requestOptions have unsupported type
   */
  public static MapboxV6Geocoding.Builder builder(
    @NonNull String accessToken,
    @NonNull V6RequestOptions requestOptions
  ) {
    if (!MapboxUtils.isAccessTokenValid(accessToken)) {
      throw new ServicesException(
        "Using Mapbox Services requires setting a valid access accessToken");
    }

    if (
      !(requestOptions instanceof V6ForwardGeocodingRequestOptions)
        && !(requestOptions instanceof V6ReverseGeocodingRequestOptions)
    ) {
      throw new ServicesException(
        "Unsupported type of request options: " + requestOptions.getClass()
      );
    }

    return new AutoValue_MapboxV6Geocoding.Builder()
      .accessToken(accessToken)
      .baseUrl(Constants.BASE_API_URL)
      .requestOptions(requestOptions);
  }

  /**
   * This builder is used to create a new instance that holds request options
   * for the forward geocoding request.
   */
  @AutoValue.Builder
  public abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder requestOptions(@NonNull V6RequestOptions requestOptions);

    /**
     * Build a new {@link MapboxV6Geocoding} object.
     *
     * @return a new {@link MapboxV6Geocoding} using the provided values in this builder
     */
    public abstract MapboxV6Geocoding build();
  }
}
