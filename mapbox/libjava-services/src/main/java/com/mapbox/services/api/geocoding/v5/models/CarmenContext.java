package com.mapbox.services.api.geocoding.v5.models;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Array representing a hierarchy of parents. Each parent includes id, text keys along with
 * (if avaliable) a wikidata, short_code, and Maki key.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#geocoding">Mapbox geocoder documentation</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class CarmenContext implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_CarmenContext.Builder();
  }

  /**
   * ID of the feature of the form {index}.{id} where index is the id/handle of the data-source
   * that contributed the result.
   *
   * @return string containing the ID
   * @since 1.0.0
   */
  @Nullable
  public abstract String id();

  /**
   * A string representing the feature.
   *
   * @return text representing the feature (e.g. "Austin")
   * @since 1.0.0
   */
  @Nullable
  public abstract String text();

  /**
   * The ISO 3166-1 country and ISO 3166-2 region code for the returned feature.
   *
   * @return a String containing the country or region code
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("short_code")
  public abstract String shortCode();

  /**
   * The WikiData identifier for a country, region or place.
   *
   * @return a unique identifier WikiData uses to query and gather more information about this
   *   specific feature
   * @since 1.2.0
   */
  @Nullable
  public abstract String wikidata();

  /**
   * provides the categories that define this features POI if applicable.
   *
   * @return comma-separated list of categories applicable to a poi
   * @since 1.0.0
   */
  @Nullable
  public abstract String category();

  /**
   * Suggested icon mapping from the most current version of the Maki project for a poi feature,
   * based on its category. Note that this doesn't actually return the image but rather the
   * identifier which can be used to download the correct image manually.
   *
   * @return string containing the recommended Maki icon
   * @since 1.2.0
   */
  @Nullable
  public abstract String maki();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<CarmenContext> typeAdapter(Gson gson) {
    return new AutoValue_CarmenContext.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link CarmenFeature}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * ID of the feature of the form {index}.{id} where index is the id/handle of the data-source
     * that contributed the result.
     *
     * @param id string containing the ID
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder id(@Nullable String id);

    /**
     * A string representing the feature.
     *
     * @param text representing the feature (e.g. "Austin")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder text(String text);

    /**
     * The ISO 3166-1 country and ISO 3166-2 region code for the returned feature.
     *
     * @param shortCode a String containing the country or region code
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder shortCode(@Nullable String shortCode);

    /**
     * The WikiData identifier for a country, region or place.
     *
     * @param wikidata a unique identifier WikiData uses to query and gather more information about
     *                 this specific feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder wikidata(@Nullable String wikidata);

    /**
     * provides the categories that define this features POI if applicable.
     *
     * @param category comma-separated list of categories applicable to a poi
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder category(@Nullable String category);

    /**
     * Suggested icon mapping from the most current version of the Maki project for a poi feature,
     * based on its category. Note that this doesn't actually return the image but rather the
     * identifier which can be used to download the correct image manually.
     *
     * @param maki string containing the recommended Maki icon
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder maki(@Nullable String maki);

    /**
     * Build a new {@link CarmenContext} object.
     *
     * @return a new {@link CarmenContext} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract CarmenContext build();

  }
}
