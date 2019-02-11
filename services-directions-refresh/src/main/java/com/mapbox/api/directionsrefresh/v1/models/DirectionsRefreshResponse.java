package com.mapbox.api.directionsrefresh.v1.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.models.DirectionsJsonObject;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

@AutoValue
public abstract class DirectionsRefreshResponse extends DirectionsJsonObject {

  /**
   * String indicating the state of the response. This is a separate code than the HTTP status code.
   * On normal valid responses, the value will be Ok. The possible responses are listed below:
   * <ul>
   * <li><strong>Ok</strong>:200 Normal success case</li>
   * <li><strong>NoRoute</strong>: 200 There was no route found for the given coordinates. Check
   * for impossible routes (e.g. routes over oceans without ferry connections).</li>
   * <li><strong>NoSegment</strong>: 200 No road segment could be matched for coordinates. Check for
   * coordinates too far away from a road.</li>
   * <li><strong>ProfileNotFound</strong>: 404 Use a valid profile as described above</li>
   * <li><strong>InvalidInput</strong>: 422</li>
   * </ul>
   *
   * @return a string with one of the given values described in the list above
   * @since 4.4.0
   */
  @NonNull
  public abstract String code();

  /**
   * Optionally shows up in a response if an error or something unexpected occurred.
   *
   * @return a string containing the message
   * @since 4.4.0
   */
  @Nullable
  public abstract String message();

  @Nullable
  public abstract DirectionsRoute route();

  @NonNull
  public static Builder builder() {
    return new AutoValue_DirectionsRefreshResponse.Builder();
  }

  public static TypeAdapter<DirectionsRefreshResponse> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsRefreshResponse.GsonTypeAdapter(gson);
  }

  public static DirectionsRefreshResponse fromJson(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create())
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gsonBuilder.create().fromJson(json, DirectionsRefreshResponse.class);
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder code(String code);

    public abstract Builder message(String message);

    public abstract Builder route(DirectionsRoute directionsRoute);

    public abstract DirectionsRefreshResponse build();
  }
}
