package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * The Features key in the geocoding API response contains the majority of information you'll want
 * to use. It extends the {@link GeoJson} object in GeoJSON and adds several additional attribute
 * which further describe the geocoding result.
 * <p>
 * A Geocoding id is a String in the form {@code {type}.{id}} where {@code {type}} is the lowest
 * hierarchy feature in the  place_type field. The  {id} suffix of the feature id is unstable and
 * may change within versions.
 * <p>
 * Note: this class doesn't actually extend Feature due to the inherit rule in AutoValue (see link
 * below).
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/search/#geocoding">Mapbox geocoder documentation</a>
 * @see <a href='geojson.org/geojson-spec.html#feature-objects'>Official GeoJson Feature Specifications</a>
 * @see <a href="https://github.com/google/auto/blob/master/value/userguide/howto.md#inherit">AutoValue inherit rule</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class CarmenFeature implements GeoJson, Serializable {

  private static final String TYPE = "Feature";

  /**
   * Create a CarmenFeature object from JSON.
   *
   * @param json string of JSON making up a carmen feature
   * @return this class using the defined information in the provided JSON string
   * @since 2.0.0
   */
  @NonNull
  public static CarmenFeature fromJson(@NonNull String json) {

    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter())
      .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
      .create();
    CarmenFeature feature = gson.fromJson(json, CarmenFeature.class);
    // Even thought properties are Nullable,
    // Feature object will be created with properties set to an empty object,
    return feature.properties() == null
      ? feature.toBuilder().properties(new JsonObject()).build()
      : feature;
  }

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  @NonNull
  public static Builder builder() {
    return new AutoValue_CarmenFeature.Builder()
      .type(TYPE)
      .properties(new JsonObject());
  }

  //
  // Feature specific attributes
  //
  // Note that CarmenFeature cannot extend Feature due to AutoValue limitations

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link Feature}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code Feature}
   * @since 1.0.0
   */
  @NonNull
  @SerializedName("type")
  @Override
  public abstract String type();

  /**
   * A {@link CarmenFeature} might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link Geometry}. The value of the bbox member MUST be a list of
   * size 2*n where n is the number of dimensions represented in the contained feature geometries,
   * with all axes of the most southwesterly point followed by all axes of the more northeasterly
   * point. The axes order of a bbox follows the axes order of geometries.
   *
   * @return a {@link BoundingBox} object containing the information
   * @since 3.0.0
   */
  @Nullable
  @Override
  public abstract BoundingBox bbox();

  /**
   * A feature may have a commonly used identifier which is either a unique String or number.
   *
   * @return a String containing this features unique identification or null if one wasn't given
   *   during creation.
   * @since 1.0.0
   */
  @Nullable
  public abstract String id();

  /**
   * The geometry which makes up this feature. A Geometry object represents points, curves, and
   * surfaces in coordinate space. One of the seven geometries provided inside this library can be
   * passed in through one of the static factory methods.
   *
   * @return a single defined {@link Geometry} which makes this feature spatially aware
   * @since 1.0.0
   */
  @Nullable
  public abstract Geometry geometry();

  /**
   * This contains the JSON object which holds the feature properties. The value of the properties
   * member is a {@link JsonObject} and might be empty if no properties are provided.
   *
   * @return a {@link JsonObject} which holds this features current properties
   * @since 1.0.0
   */
  @Nullable
  public abstract JsonObject properties();

  //
  // CarmenFeature specific attributes
  //

  /**
   * A string representing the feature in the requested language, if specified.
   *
   * @return text representing the feature (e.g. "Austin")
   * @since 1.0.0
   */
  @Nullable
  public abstract String text();

  /**
   * A string representing the feature in the requested language, if specified, and its full result
   * hierarchy.
   *
   * @return human-readable text representing the full result hierarchy (e.g. "Austin, Texas,
   *   United States")
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("place_name")
  public abstract String placeName();

  /**
   * A list of feature types describing the feature. Options are one of the following types defined
   * in the {@link GeocodingTypeCriteria}. Most features have only one type, but if the feature has
   * multiple types, (for example, Vatican City is a country, region, and place), all applicable
   * types will be provided in the list.
   *
   * @return a list containing the place type
   * @since 1.0.0
   */
  @Nullable
  @SerializedName("place_type")
  public abstract List<String> placeType();

  /**
   * A string of the house number for the returned {@code address} feature. Note that unlike the
   * address property for {@code poi} features, this property is outside the  properties object.
   *
   * @return while the string content isn't guaranteed, and might return null, in many cases, this
   *   will be the house number
   * @since 1.0.0
   */
  @Nullable
  public abstract String address();

  /**
   * A {@link Point} object which represents the center point inside the {@link #bbox()} if one is
   * provided.
   *
   * @return a GeoJson {@link Point} which defines the center location of this feature
   * @since 1.0.0
   */
  @Nullable
  public Point center() {
    // Store locally since rawCenter() is mutable
    double[] center = rawCenter();
    if (center != null && center.length == 2) {
      return Point.fromLngLat(center[0], center[1]);
    }
    return null;
  }

  // No public access thus, we lessen enforcement on mutability here.
  @Nullable
  @SerializedName("center")
  @SuppressWarnings("mutable")
  abstract double[] rawCenter();

  /**
   * A list representing the hierarchy of encompassing parent features. This is where you can find
   * telephone, address, and other information pertaining to this feature.
   *
   * @return a list made up of {@link CarmenContext} which might contain additional information
   *   about this specific feature
   * @since 1.0.0
   */
  @Nullable
  public abstract List<CarmenContext> context();

  /**
   * A numerical score from 0 (least relevant) to 0.99 (most relevant) measuring how well each
   * returned feature matches the query. You can use this property to remove results which don't
   * fully match the query.
   *
   * @return the relevant score between 0 and 1
   * @since 1.0.0
   */
  @Nullable
  public abstract Double relevance();

  /**
   * A string analogous to the {@link #text()} field that more closely matches the query than
   * results in the specified language. For example, querying &quot;K&#246;ln, Germany&quot; with
   * language set to English might return a feature with the {@link #text()} &quot;Cologne&quot;
   * and this would be &quot;K&#246;ln&quot;.
   *
   * @return a string containing the matching text
   * @since 2.2.0
   */
  @Nullable
  @SerializedName("matching_text")
  public abstract String matchingText();

  /**
   * A string analogous to the {@link #placeName()} field that more closely matches the query than
   * results in the specified language. For example, querying "K&#246;ln, Germany" with language
   * set to English might return a feature with the {@link #placeName()} "Cologne, Germany"
   * and this would return "K&#246;ln, North Rhine-Westphalia, Germany".
   *
   * @return a string containing the matching place name
   * @since 2.2.0
   */
  @Nullable
  @SerializedName("matching_place_name")
  public abstract String matchingPlaceName();

  /**
   * A string of the IETF language tag of the query's primary language.
   *
   * @return string containing the query's primary language
   * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag Wikipedia page</a>
   * @since 2.2.0
   */
  @Nullable
  public abstract String language();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<CarmenFeature> typeAdapter(Gson gson) {
    return new AutoValue_CarmenFeature.GsonTypeAdapter(gson);
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a JSON
   * string.
   *
   * @return a JSON string which represents this CarmenFeature
   * @since 3.0.0
   */
  @Override
  @SuppressWarnings("unused")
  public String toJson() {

    Gson gson = new GsonBuilder()
      .registerTypeAdapterFactory(GeometryAdapterFactory.create())
      .registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter())
      .registerTypeAdapterFactory(GeocodingAdapterFactory.create())
      .create();

    // Empty properties -> should not appear in json string
    CarmenFeature feature = this;
    if (properties() != null && properties().size() == 0) {
      feature = toBuilder().properties(null).build();
    }

    return gson.toJson(feature, CarmenFeature.class);
  }

  /**
   * Convert current instance values into another Builder to quickly change one or more values.
   *
   * @return a new instance of {@link CarmenFeature} using the newly defined values
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public abstract Builder toBuilder();

  /**
   * This builder can be used to set the values describing the {@link CarmenFeature}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  @SuppressWarnings("unused")
  public abstract static class Builder {

    // Type will always be set to "Feature"
    abstract Builder type(@NonNull String type);

    /**
     * A Feature might have a member named {@code bbox} to include information on the coordinate
     * range for it's {@link Feature}s. The value of the bbox member MUST be a list of size 2*n
     * where n is the number of dimensions represented in the contained feature geometries, with all
     * axes of the most southwesterly point followed by all axes of the more northeasterly point.
     * The axes order of a bbox follows the axes order of geometries.
     *
     * @param bbox a list of double coordinate values describing a bounding box
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bbox(@Nullable BoundingBox bbox);

    /**
     * A feature may have a commonly used identifier which is either a unique String or number.
     *
     * @param id a String containing this features unique identification or null if one wasn't given
     *           during creation.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder id(@Nullable String id);

    /**
     * The geometry which makes up this feature. A Geometry object represents points, curves, and
     * surfaces in coordinate space. One of the seven geometries provided inside this library can be
     * passed in through one of the static factory methods.
     *
     * @param geometry a single defined {@link Geometry} which makes this feature spatially aware
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder geometry(@Nullable Geometry geometry);

    /**
     * This contains the JSON object which holds the feature properties. The value of the properties
     * member is a {@link JsonObject} and might be empty if no properties are provided.
     *
     * @param properties a {@link JsonObject} which holds this features current properties
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder properties(@Nullable JsonObject properties);

    /**
     * A string representing the feature in the requested language.
     *
     * @param text text representing the feature (e.g. "Austin")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder text(@Nullable String text);

    /**
     * A string representing the feature in the requested language, if specified, and its full
     * result hierarchy.
     *
     * @param placeName human-readable text representing the full result hierarchy (e.g. "Austin,
     *                  Texas, United States")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder placeName(@Nullable String placeName);

    /**
     * A list of feature types describing the feature. Options are one of the following types
     * defined in the {@link GeocodingTypeCriteria}. Most features have only one type, but if the
     * feature has multiple types, (for example, Vatican City is a country, region, and place), all
     * applicable types will be provided in the list.
     *
     * @param placeType a list containing the place type
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder placeType(@Nullable List<String> placeType);

    /**
     * A string of the house number for the returned {@code address} feature. Note that unlike the
     * address property for {@code poi} features, this property is outside the  properties object.
     *
     * @param address while the string content isn't guaranteed, and might return null, in many
     *                cases, this will be the house number
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder address(@Nullable String address);

    /**
     * A {@link Point} object which represents the center point inside the {@link #bbox()} if one is
     * provided.
     *
     * @param center a GeoJson {@link Point} which defines the center location of this feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rawCenter(@Nullable double[] center);

    /**
     * A list representing the hierarchy of encompassing parent features. This is where you can find
     * telephone, address, and other information pertaining to this feature.
     *
     * @param contexts a list made up of {@link CarmenContext} which might contain additional
     *                 information about this specific feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder context(@Nullable List<CarmenContext> contexts);

    /**
     * A numerical score from 0 (least relevant) to 0.99 (most relevant) measuring how well each
     * returned feature matches the query. You can use this property to remove results which don't
     * fully match the query.
     *
     * @param relevance the relevant score between 0 and 1
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder relevance(@Nullable Double relevance);

    /**
     * A string analogous to the {@link #text()} field that more closely matches the query than
     * results in the specified language. For example, querying "K&#246;ln, Germany" with language
     * set to English might return a feature with the {@link #text()} "Cologne" and this
     * would be "K&#246;ln".
     *
     * @param matchingText a string containing the matching text
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder matchingText(@Nullable String matchingText);

    /**
     * A string analogous to the {@link #placeName()} field that more closely matches the query than
     * results in the specified language. For example, querying "K&#246;ln, Germany" with language
     * set to English might return a feature with the {@link #placeName()} "Cologne, Germany"
     * and this would return "K&#246;ln, North Rhine-Westphalia, Germany".
     *
     * @param matchingPlaceName a string containing the matching place name
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder matchingPlaceName(@Nullable String matchingPlaceName);

    /**
     * A string of the IETF language tag of the query's primary language.
     *
     * @param language string containing the query's primary language
     * @return this builder for chaining options together
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag Wikipedia page</a>
     * @since 2.2.0
     */
    public abstract Builder language(@Nullable String language);

    /**
     * Build a new {@link CarmenFeature} object.
     *
     * @return a new {@link CarmenFeature} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract CarmenFeature build();
  }
}
