package com.mapbox.api.tilequery;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * The Mapbox Tilequery API allows you to retrieve data about specific features from a
 * vector tileset, based on a given latitude and longitude.
 *
 * @see <a href="https://www.mapbox.com/api-documentation/maps/#tilequery">Tilequery API
 * documentation</a>
 *
 * @since 3.5.0
 */
@AutoValue
public abstract class MapboxTilequery extends MapboxService<FeatureCollection, TilequeryService> {
  private Call<List<FeatureCollection>> batchCall;

  protected MapboxTilequery() {
    super(TilequeryService.class);
  }

  @Override
  protected GsonBuilder getGsonBuilder() {

    return new GsonBuilder()
      .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
      .registerTypeAdapterFactory(GeometryAdapterFactory.create());
  }

  @Override
  protected Call<FeatureCollection> initializeCall() {
    return getService().getCall(
      mapIds(),
      query(),
      accessToken(),
      radius(),
      limit(),
      dedupe(),
      geometry(),
      layers());
  }

  private Call<List<FeatureCollection>> getBatchCall() {
    // No need to recreate it
    if (batchCall != null) {
      return batchCall;
    }

    batchCall = getService().getBatchCall(
      mapIds(),
      query(),
      accessToken(),
      radius(),
      limit(),
      dedupe(),
      geometry(),
      layers());

    return batchCall;
  }

  /**
   * Wrapper method for Retrofit's {@link Call#execute()} call returning a batch response
   * specific to the Tilequery API.
   *
   * @return the Tilequery batch response once the call completes successfully
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 3.5.0
   */
  public Response<List<FeatureCollection>> executeBatchCall() throws IOException {
    return getBatchCall().execute();
  }

  /**
   * Wrapper method for Retrofit's {@link Call#enqueue(Callback)} call returning a batch response
   * specific to the Tilequery batch API. Use this method to make a tilequery request on the Main
   * Thread.
   *
   * @param callback a {@link Callback} which is used once the {@link FeatureCollection} is created.
   * @since 3.5.0
   */
  public void enqueueBatchCall(Callback<List<FeatureCollection>> callback) {
    getBatchCall().enqueue(callback);
  }

  /**
   * Wrapper method for Retrofit's {@link Call#cancel()} call, important to manually cancel call if
   * the user dismisses the calling activity or no longer needs the returned results.
   *
   * @since 3.5.0
   */
  public void cancelBatchCall() {
    getBatchCall().cancel();
  }

  /**
   * Wrapper method for Retrofit's {@link Call#clone()} call, useful for getting call information.
   *
   * @return cloned call
   * @since 3.5.0
   */
  public Call<List<FeatureCollection>> cloneBatchCall() {
    return getBatchCall().clone();
  }

  /**
   * Build a new {@link MapboxTilequery} object with the initial value set for
   * {@link #baseUrl()}.
   *
   * @return a {@link MapboxTilequery.Builder} object for creating this object
   * @since 3.5.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxTilequery.Builder()
      .baseUrl(Constants.BASE_API_URL);
  }

  @NonNull
  @Override
  protected abstract String baseUrl();

  @NonNull
  abstract String accessToken();

  @NonNull
  abstract String mapIds();

  @NonNull
  abstract String query();

  @Nullable
  abstract Integer radius();

  @Nullable
  abstract Integer limit();

  @Nullable
  abstract Boolean dedupe();

  @Nullable
  abstract @TilequeryCriteria.TilequeryGeometry String geometry();

  @Nullable
  abstract String layers();

  /**
   * This builder is used to create a new request to the Mapbox Tilequery API. At a bare minimum,
   * your request must include an access token, a map ID, and a query of some kind. All other
   * fields can be left alone in order to use the default behaviour of the API.
   * <p>
   * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
   * rather than the primitive to allow for unset (null) values.
   * </p>
   *
   * @since 3.5.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * Required to call when this is being built. If no access token provided,
     * {@link ServicesException} will be thrown.
     *
     * @param accessToken Mapbox access token
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * The ID of the map being queried. If you need to composite multiple layers, the Tilequery
     * API endpoint can also support a comma-separated list of map IDs.
     *
     * @param mapIds Map ID(s)
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder mapIds(String mapIds);

    /**
     * The longitude and latitude to be queried.
     *
     * @param point query point
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public Builder query(@NonNull Point point) {
      query(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude())));

      String str = String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude()));
      return this;
    }

    /**
     * The longitude and latitude to be queried.
     *
     * @param query query point
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder query(@NonNull String query);

    /**
     * The approximate distance in meters to query for features. Defaults to  0. Has no upper
     * bound. Required for queries against point and line data. Due to the nature of tile
     * buffering, a query with a large radius made against equally large point or line data may
     * not include all possible features in the results. Queries will use tiles from the
     * maximum zoom of the tileset, and will only include the intersecting tile plus 8
     * surrounding tiles when searching for nearby features.
     *
     * @param radius distance in meters to query for features
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder radius(@Nullable Integer radius);

    /**
     * The number of features between 1 - 50 to return. Defaults to  5.
     *
     * @param limit the number of features
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder limit(@Nullable Integer limit);

    /**
     * Determines whether results will be deduplicated or not. Defaults to true.
     *
     * @param dedupe whether results will be deduplicated
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder dedupe(@Nullable Boolean dedupe);

    /**
     * Queries for a specific geometry type. Options are polygon, linestring, or point.
     *
     * @param geometry polygon, linestring, or point
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder geometry(
            @Nullable @TilequeryCriteria.TilequeryGeometry String geometry);

    /**
     * A comma-separated list of layers to query, rather than querying all layers. If a
     * specified layer does not exist, it is skipped. If no layers exist, returns an
     * empty FeatureCollection.
     *
     * @param layers list of layers to query
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public abstract Builder layers(@Nullable String layers);

    /**
     *
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    abstract MapboxTilequery autoBuild();

    /**
     * Build a new {@link MapboxTilequery} object.
     *
     * @return this builder for chaining options together
     * @since 3.5.0
     */
    public MapboxTilequery build() {
      MapboxTilequery tilequery = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(tilequery.accessToken())) {
        throw new ServicesException("Using Mapbox Services requires setting a valid access token.");
      }

      if (tilequery.query().isEmpty()) {
        throw new ServicesException("A query with latitude and longitude values is required.");
      }

      return tilequery;
    }
  }
}
