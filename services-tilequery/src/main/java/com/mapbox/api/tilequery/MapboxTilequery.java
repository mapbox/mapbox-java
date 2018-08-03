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
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AutoValue
public abstract class MapboxTilequery extends MapboxService<FeatureCollection, TilequeryService> {
  private Call<List<FeatureCollection>> batchCall;

  public MapboxTilequery() {
    super(TilequeryService.class);
  }

  @Override
  protected GsonBuilder getGsonBuilder() {
    return new GsonBuilder();
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

  public Response<List<FeatureCollection>> executeBatchCall() throws IOException {
    return getBatchCall().execute();
  }

  public void enqueueBatchCall(Callback<List<FeatureCollection>> callback) {
    getBatchCall().enqueue(callback);
  }

  public void cancelBatchCall() {
    getBatchCall().cancel();
  }

  public Call<List<FeatureCollection>> cloneBatchCall() {
    return getBatchCall().clone();
  }

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
  abstract @TilequeryCriteria.TilequeryGeometry
  String geometry();

  @Nullable
  abstract String layers();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder baseUrl(@NonNull String baseUrl);

    public abstract Builder accessToken(@NonNull String accessToken);

    public abstract Builder mapIds(String mapIds);

    public Builder query(@NonNull Point point) {
      query(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude())));
      return this;
    }

    public abstract Builder query(@NonNull String query);

    public abstract Builder radius(@Nullable Integer radius);

    public abstract Builder limit(@Nullable Integer limit);

    public abstract Builder dedupe(@Nullable Boolean dedupe);

    public abstract Builder geometry(@Nullable @TilequeryCriteria.TilequeryGeometry String geometry);

    public abstract Builder layers(@Nullable String layers);

    public abstract MapboxTilequery autoBuild();

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
