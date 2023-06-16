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
 * The {@link V6MatchCode} object in the Geocoding API helps you understand
 * how the resulting address feature aligns with the query submitted.
 * Available only for address-type features, the {@link V6MatchCode} provides a breakdown
 * of how each element of the result matches with the query, plus an overall Confidence score,
 * based on how well it matches. This can help you make decisions about what results to keep
 * or throw out based on your application's tolerance for fuzzy matching on the query.
 * <p>
 * Smart Address Match is available for all forward geocoding requests that return
 * an address type feature. It works best when using Structured Input forward queries,
 * as the request components must be typed explicitly.
 *
 * @see <a href="https://docs.mapbox.com/api/search/geocoding-v6/#smart-address-match">Smart Address Match</a>
 */
@AutoValue
public abstract class V6MatchCode extends V6Object {

  /**
   * Match code for address number.
   *
   * @return match code for address number
   */
  @Nullable
  @SerializedName("address_number")
  public abstract String addressNumber();

  /**
   * Match code for street.
   *
   * @return match code for street
   */
  @Nullable
  @SerializedName("street")
  public abstract String street();

  /**
   * Match code for locality.
   *
   * @return match code for locality
   */
  @Nullable
  @SerializedName("locality")
  public abstract String locality();

  /**
   * Match code for place.
   *
   * @return match code for place
   */
  @Nullable
  @SerializedName("place")
  public abstract String place();

  /**
   * Match code for postcode.
   *
   * @return match code for postcode
   */
  @Nullable
  @SerializedName("postcode")
  public abstract String postcode();

  /**
   * Match code for region.
   *
   * @return match code for region
   */
  @Nullable
  @SerializedName("region")
  public abstract String region();

  /**
   * Match code for country.
   *
   * @return match code for country
   */
  @Nullable
  @SerializedName("country")
  public abstract String country();

  /**
   * Confidence score, which indicates how well the result matches the input query.
   *
   * @return confidence score
   */
  @Nullable
  @SerializedName("confidence")
  public abstract String confidence();

  /**
   * Create a V6MatchCode object from JSON.
   *
   * @param json string of JSON making up a carmen context
   * @return this class using the defined information in the provided JSON string
   */
  @SuppressWarnings("unused")
  public static V6MatchCode fromJson(@NonNull String json) {
    final Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(V6GeocodingAdapterFactory.create())
      .create();
    return gson.fromJson(json, V6MatchCode.class);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<V6MatchCode> typeAdapter(Gson gson) {
    return new AutoValue_V6MatchCode.GsonTypeAdapter(gson);
  }

  @AutoValue.Builder
  abstract static class Builder extends BaseBuilder<Builder> {

    abstract Builder addressNumber(String addressNumber);

    abstract Builder street(String street);

    abstract Builder locality(String locality);

    abstract Builder place(String place);

    abstract Builder postcode(String postcode);

    abstract Builder region(String region);

    abstract Builder country(String country);

    abstract Builder confidence(String confidence);

    abstract V6MatchCode build();
  }
}
