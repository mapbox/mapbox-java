package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v6.V6GeocodingAdapterFactory;

/**
 * A type which contains the majority of information returned by the Geocoding V6 API.
 */
@AutoValue
public abstract class V6Properties extends V6JsonObject {

  /**
   * Feature id.
   *
   * @return feature id
   */
  @NonNull
  @SerializedName("mapbox_id")
  public abstract String mapboxId();

  /**
   * A string describing the type of the feature.
   *
   * @return a string describing the type of the feature
   */
  @NonNull
  @V6FeatureType.FeatureType
  @SerializedName("feature_type")
  public abstract String featureType();

  /**
   * Formatted name.
   *
   * @return formatted name
   */
  @Nullable
  @SerializedName("name")
  public abstract String name();

  /**
   * Formatted string of result context: place region country postcode.
   * The part of the result which comes after {@link V6Properties#name()}.
   *
   * @return formatted string of result context: place region country postcode
   */
  @Nullable
  @SerializedName("place_formatted")
  public abstract String placeFormatted();

  /**
   * An object representing the hierarchy of encompassing parent features.
   * Addresses will also include an address context object,
   * which contains the address street and address number properties broken out separately.
   *
   * @return an object representing the hierarchy of encompassing parent features
   */
  @Nullable
  @SerializedName("context")
  public abstract V6Context context();

  /**
   * Object containing coordinate parameters (lat, long) and accuracy.
   *
   * @return object containing coordinate parameters (lat, long) and accuracy
   */
  @Nullable
  @SerializedName("coordinates")
  public abstract V6Coordinates coordinates();

  /**
   * Additional metadata indicating how the result components match to the input query.
   *
   * @return additional metadata indicating how the result components match to the input query
   * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#smart-address-match">Smart Address Match</a>
   */
  @Nullable
  @SerializedName("match_code")
  public abstract V6MatchCode matchCode();

  /**
   * Create a V6Property object from JSON.
   *
   * @param json string of JSON making up a carmen context
   * @return this class using the defined information in the provided JSON string
   */
  @SuppressWarnings("unused")
  public static V6Properties fromJson(@NonNull String json) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.fromJson(json, V6Properties.class);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6Properties> typeAdapter(Gson gson) {
    return new AutoValue_V6Properties.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder mapboxId(String mapboxId);

    abstract Builder featureType(String featureType);

    abstract Builder name(String name);

    abstract Builder placeFormatted(String placeFormatted);

    abstract Builder context(V6Context context);

    abstract Builder coordinates(V6Coordinates coordinates);

    abstract Builder matchCode(V6MatchCode matchCode);

    abstract V6Properties build();
  }
}
