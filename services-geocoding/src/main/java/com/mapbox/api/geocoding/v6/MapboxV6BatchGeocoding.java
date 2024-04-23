package com.mapbox.api.geocoding.v6;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.geocoding.v6.models.V6BatchResponse;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.ApiCallHelper;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.geojson.GeometryAdapterFactory;

import java.util.Arrays;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * This class gives you access to Mapbox V6 batch geocoding.
 *
 * The batch geocoding query type allows you to request up to 1000 forward or reverse geocoding
 * queries in a single request.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding/#batch-geocoding">Batch Geocoding</a>
 */
@AutoValue
public abstract class MapboxV6BatchGeocoding extends MapboxV6BaseGeocoding<V6BatchResponse> {

  @NonNull
  abstract List<? extends V6RequestOptions> requestOptions();

  @Override
  protected GsonBuilder getGsonBuilder() {
    return new GsonBuilder()
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .registerTypeAdapterFactory(GeometryAdapterFactory.create());
  }

  @Override
  protected Call<V6BatchResponse> initializeCall() {
    final String optionsJson = V6GeocodingUtils.serialize(requestOptions());

    final RequestBody body = RequestBody.create(
      optionsJson,
      okhttp3.MediaType.parse("application/json; charset=utf-8")
    );

    return getService().batchGeocoding(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      accessToken(),
      permanent(),
      body
    );
  }

  /**
   * Creates a new {@link MapboxV6BatchGeocoding.Builder} object.
   *
   * @param accessToken    a valid <a href="https://docs.mapbox.com/help/getting-started/access-tokens/">Mapbox Access Token</a> used for API request
   * @param requestOptions options specified for this geocoding request
   * @return Builder object.
   * @throws ServicesException if accessToken is in incorrect format
   */
  public static MapboxV6BatchGeocoding.Builder builder(
    @NonNull String accessToken,
    @NonNull V6RequestOptions... requestOptions
  ) {
    return builder(accessToken, Arrays.asList(requestOptions));
  }

  /**
   * Creates a new {@link MapboxV6BatchGeocoding.Builder} object.
   *
   * @param accessToken    a valid <a href="https://docs.mapbox.com/help/getting-started/access-tokens/">Mapbox Access Token</a> used for API request
   * @param requestOptions options specified for this geocoding request
   * @return Builder object.
   * @throws ServicesException if accessToken is in incorrect format
   */
  public static MapboxV6BatchGeocoding.Builder builder(
    @NonNull String accessToken,
    @NonNull List<? extends V6RequestOptions> requestOptions
  ) {
    if (!MapboxUtils.isAccessTokenValid(accessToken)) {
      throw new ServicesException(
        "Using Mapbox Services requires setting a valid access accessToken");
    }
    return new AutoValue_MapboxV6BatchGeocoding.Builder()
      .accessToken(accessToken)
      .requestOptions(requestOptions)
      .baseUrl(Constants.BASE_API_URL);
  }

  /**
   * This builder is used to create a new instance that holds request options
   * for the batch geocoding request.
   */
  @AutoValue.Builder
  public abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder requestOptions(@NonNull List<? extends V6RequestOptions> requestOptions);

    /**
     * Build a new {@link MapboxV6BatchGeocoding} object.
     *
     * @return a new {@link MapboxV6BatchGeocoding} using the provided values in this builder
     */
    public abstract MapboxV6BatchGeocoding build();
  }
}
