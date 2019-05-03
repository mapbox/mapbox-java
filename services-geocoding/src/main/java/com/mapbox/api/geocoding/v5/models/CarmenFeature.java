package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.api.geocoding.v5.GeocodingCriteria.GeocodingTypeCriteria;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The Features key in the geocoding API response contains the majority of information you'll want
 * to use. It extends the {@link GeoJson} object in GeoJSON and adds several additional attribute
 * which further describe the geocoding result.
 * <p>
 * A Geocoding id is a String in the form {@code {type}.{id}} where {@code {type}} is the lowest
 * hierarchy feature in the  place_type field. The  {id} suffix of the feature id is unstable and
 * may change within versions.
 * <p>
 * Note: this class doesn't actually extend Feature due to Feature being final.
 * @since 1.0.0
 */
public class CarmenFeature implements GeoJson, Serializable {

  private static final String TYPE = "Feature";

  private final String type;

  private final BoundingBox bbox;

  private final String id;

  private final Geometry geometry;

  private final JsonObject properties;

  private final String text;
  private final Map<String, String> texts;

  @SerializedName("place_name")
  private final String placeName;
  private final Map<String, String> placeNames;

  private final String language;
  private final List<String> languages;

  @SerializedName("place_type")
  private final List<String> placeType;

  private final String address;

  private final double[] rawCenter;

  private final List<CarmenContext> context;

  private final Double relevance;

  private final String matchingText;

  private final String matchingPlaceName;


  CarmenFeature(
          String type,
          @Nullable BoundingBox bbox,
          @Nullable String id,
          @Nullable Geometry geometry,
          @Nullable JsonObject properties,
          @Nullable String text,
          @Nullable Map<String, String> texts,
          @Nullable String placeName,
          @Nullable Map<String, String> placeNames,
          @Nullable List<String> placeType,
          @Nullable String address,
          @Nullable double[] rawCenter,
          @Nullable List<CarmenContext> context,
          @Nullable Double relevance,
          @Nullable String matchingText,
          @Nullable String matchingPlaceName,
          @Nullable String language,
          @Nullable List<String> languages) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    this.id = id;
    this.geometry = geometry;
    this.properties = properties;
    this.text = text;
    this.texts = texts;
    this.placeName = placeName;
    this.placeNames = placeNames;
    this.placeType = placeType;
    this.address = address;
    this.rawCenter = rawCenter;
    this.context = context;
    this.relevance = relevance;
    this.matchingText = matchingText;
    this.matchingPlaceName = matchingPlaceName;
    this.language = language;
    this.languages = languages;
  }

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
    return new Builder()
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
  @Override
  public String type() {
    return type;
  }

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
  public BoundingBox bbox()  {
    return bbox;
  }

  /**
   * A feature may have a commonly used identifier which is either a unique String or number.
   *
   * @return a String containing this features unique identification or null if one wasn't given
   *   during creation.
   * @since 1.0.0
   */
  @Nullable
  public String id()  {
    return id;
  }

  /**
   * The geometry which makes up this feature. A Geometry object represents points, curves, and
   * surfaces in coordinate space. One of the seven geometries provided inside this library can be
   * passed in through one of the static factory methods.
   *
   * @return a single defined {@link Geometry} which makes this feature spatially aware
   * @since 1.0.0
   */
  @Nullable
  public Geometry geometry()  {
    return geometry;
  }

  /**
   * This contains the JSON object which holds the feature properties. The value of the properties
   * member is a {@link JsonObject} and might be empty if no properties are provided.
   *
   * @return a {@link JsonObject} which holds this features current properties
   * @since 1.0.0
   */
  @Nullable
  public JsonObject properties()  {
    return properties;
  }

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
  public String text()  {
    return text;
  }

  /**
   * A map representing language tag to text string mapping.
   * @return map representing language tag to text string mapping
   * @since 4.9.0
   */
  @Nullable
  public Map<String, String> texts()  {
    return texts;
  }

  /**
   * A string representing the feature in the requested language, if specified, and its full result
   * hierarchy.
   *
   * @return human-readable text representing the full result hierarchy (e.g. "Austin, Texas,
   *   United States")
   * @since 1.0.0
   */
  @Nullable
  public String placeName()  {
    return placeName;
  }

  /**
   * A map representing language tag to place name string mapping.
   * @return map representing language tag to place name string mapping
   * @since 4.9.0
   */
  @Nullable
  public Map<String, String> placeNames()  {
    return placeNames;
  }

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
  public List<String> placeType()  {
    return placeType;
  }

  /**
   * A string of the house number for the returned {@code address} feature. Note that unlike the
   * address property for {@code poi} features, this property is outside the  properties object.
   *
   * @return while the string content isn't guaranteed, and might return null, in many cases, this
   *   will be the house number
   * @since 1.0.0
   */
  @Nullable
  public String address()  {
    return address;
  }

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
  double[] rawCenter()  {
    return rawCenter;
  }

  /**
   * A list representing the hierarchy of encompassing parent features. This is where you can find
   * telephone, address, and other information pertaining to this feature.
   *
   * @return a list made up of {@link CarmenContext} which might contain additional information
   *   about this specific feature
   * @since 1.0.0
   */
  @Nullable
  public  List<CarmenContext> context()  {
    return context;
  }

  /**
   * A numerical score from 0 (least relevant) to 0.99 (most relevant) measuring how well each
   * returned feature matches the query. You can use this property to remove results which don't
   * fully match the query.
   *
   * @return the relevant score between 0 and 1
   * @since 1.0.0
   */
  @Nullable
  public Double relevance()  {
    return relevance;
  }

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
  public String matchingText()  {
    return matchingText;
  }

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
  public String matchingPlaceName()  {
    return matchingPlaceName;
  }

  /**
   * A string of the IETF language tag of the query's primary language.
   *
   * @return string containing the query's primary language
   * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag Wikipedia page</a>
   * @since 2.2.0
   */
  @Nullable
  public String language()  {
    return language;
  }

  /**
   * This field is only returned when multiple languages are requested using the language
   * parameter, and will be present for each requested language.
   * @return List of requested language tags.
   * @since 4.9.0
   */
  @Nullable
  public List<String> languages() {
    return languages;
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<CarmenFeature> typeAdapter(Gson gson) {
    return new GsonTypeAdapter(gson);
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
  public Builder toBuilder()  {
    return new Builder(this);
  }

  @Override
  public String toString() {
    return "CarmenFeature{"
            + "type='" + type + '\''
            + ", bbox=" + bbox
            + ", id='" + id + '\''
            + ", geometry=" + geometry
            + ", properties=" + properties
            + ", text='" + text + '\''
            + ", texts=" + texts
            + ", placeName='" + placeName + '\''
            + ", placeNames=" + placeNames
            + ", language='" + language + '\''
            + ", languages=" + languages
            + ", placeType=" + placeType
            + ", address='" + address + '\''
            + ", rawCenter=" + Arrays.toString(rawCenter)
            + ", context=" + context
            + ", relevance=" + relevance
            + ", matchingText='" + matchingText + '\''
            + ", matchingPlaceName='" + matchingPlaceName + '\''
            + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof CarmenFeature)) {
      return false;
    }
    CarmenFeature feature = (CarmenFeature) obj;
    return Objects.equals(type, feature.type)
            && Objects.equals(bbox, feature.bbox)
            && Objects.equals(id, feature.id)
            && Objects.equals(geometry, feature.geometry)
            && Objects.equals(properties, feature.properties)
            && Objects.equals(text, feature.text)
            && Objects.equals(texts, feature.texts)
            && Objects.equals(placeName, feature.placeName)
            && Objects.equals(placeNames, feature.placeNames)
            && Objects.equals(language, feature.language)
            && Objects.equals(languages, feature.languages)
            && Objects.equals(placeType, feature.placeType)
            && Objects.equals(address, feature.address)
            && Arrays.equals(rawCenter, feature.rawCenter)
            && Objects.equals(context, feature.context)
            && Objects.equals(relevance, feature.relevance)
            && Objects.equals(matchingText, feature.matchingText)
            && Objects.equals(matchingPlaceName, feature.matchingPlaceName);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(type, bbox, id, geometry, properties, text, texts, placeName,
            placeNames, language, languages, placeType, address, context, relevance,
            matchingText, matchingPlaceName);
    result = 31 * result + Arrays.hashCode(rawCenter);
    return result;
  }

  /**
   * This builder can be used to set the values describing the {@link CarmenFeature}.
   *
   * @since 3.0.0
   */
  @SuppressWarnings("unused")
  public static class Builder {

    private String type;

    private BoundingBox bbox;

    private String id;

    private Geometry geometry;

    private JsonObject properties;

    private String text;
    private Map<String, String> texts;

    private String placeName;
    private Map<String, String> placeNames;

    private List<String> placeType;

    private String address;

    private double[] rawCenter;

    private List<CarmenContext> context;

    private Double relevance;

    private String matchingText;

    private String matchingPlaceName;

    private String language;
    private List<String> languages;

    Builder() {
    }

    private Builder(CarmenFeature source) {
      this.type = source.type();
      this.bbox = source.bbox();
      this.id = source.id();
      this.geometry = source.geometry();
      this.properties = source.properties();
      this.text = source.text();
      this.texts = source.texts();
      this.placeName = source.placeName();
      this.placeNames = source.placeNames();
      this.language = source.language();
      this.languages = source.languages();
      this.placeType = source.placeType();
      this.address = source.address();
      this.rawCenter = source.rawCenter();
      this.context = source.context();
      this.relevance = source.relevance();
      this.matchingText = source.matchingText();
      this.matchingPlaceName = source.matchingPlaceName();
    }

    // Type will always be set to "Feature"
    Builder type(@NonNull String type) {
      if (type == null) {
        throw new NullPointerException("Null type");
      }
      this.type = type;
      return this;
    }

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
    public Builder bbox(@Nullable BoundingBox bbox) {
      this.bbox = bbox;
      return this;
    }

    /**
     * A feature may have a commonly used identifier which is either a unique String or number.
     *
     * @param id a String containing this features unique identification or null if one wasn't given
     *           during creation.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder id(@Nullable String id) {
      this.id = id;
      return this;
    }

    /**
     * The geometry which makes up this feature. A Geometry object represents points, curves, and
     * surfaces in coordinate space. One of the seven geometries provided inside this library can be
     * passed in through one of the static factory methods.
     *
     * @param geometry a single defined {@link Geometry} which makes this feature spatially aware
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder geometry(@Nullable Geometry geometry) {
      this.geometry = geometry;
      return this;
    }

    /**
     * This contains the JSON object which holds the feature properties. The value of the properties
     * member is a {@link JsonObject} and might be empty if no properties are provided.
     *
     * @param properties a {@link JsonObject} which holds this features current properties
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder properties(@Nullable JsonObject properties) {
      this.properties = properties;
      return this;
    }

    /**
     * A string representing the feature in the requested language.
     *
     * @param text text representing the feature (e.g. "Austin")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder text(@Nullable String text) {
      this.text = text;
      return this;
    }

    /**
     * A string analogous to the text field that matches the query in the requested language.
     * This field is only returned when multiple languages are requested using the language
     * parameter, and will be present for each requested language.
     *
     * @param texts map with language tag to text string mapping
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder texts(@Nullable Map<String, String> texts) {
      this.texts = texts;
      return this;
    }

    /**
     * A string representing the feature in the requested language, if specified, and its full
     * result hierarchy.
     *
     * @param placeName human-readable text representing the full result hierarchy (e.g. "Austin,
     *                  Texas, United States")
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder placeName(@Nullable String placeName) {
      this.placeName = placeName;
      return this;
    }

    /**
     * A string analogous to the place_name field that matches the query in the requested language.
     * This field is only returned when multiple languages are requested using the language
     * parameter, and will be present for each requested language.
     * @param placeNames map with language tag to place name string mapping
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder placeNames(@Nullable Map<String, String> placeNames) {
      this.placeNames = placeNames;
      return this;
    }

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
    public Builder placeType(@Nullable List<String> placeType) {
      this.placeType = placeType;
      return this;
    }

    /**
     * A string of the house number for the returned {@code address} feature. Note that unlike the
     * address property for {@code poi} features, this property is outside the  properties object.
     *
     * @param address while the string content isn't guaranteed, and might return null, in many
     *                cases, this will be the house number
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder address(@Nullable String address) {
      this.address = address;
      return this;
    }

    /**
     * A {@link Point} object which represents the center point inside the {@link #bbox()} if one is
     * provided.
     *
     * @param rawCenter a GeoJson {@link Point} which defines the center location of this feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder rawCenter(@Nullable double[] rawCenter) {
      this.rawCenter = rawCenter;
      return this;
    }

    /**
     * A list representing the hierarchy of encompassing parent features. This is where you can find
     * telephone, address, and other information pertaining to this feature.
     *
     * @param context a list made up of {@link CarmenContext} which might contain additional
     *                 information about this specific feature
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder context(@Nullable List<CarmenContext> context) {
      this.context = context;
      return this;
    }

    /**
     * A numerical score from 0 (least relevant) to 0.99 (most relevant) measuring how well each
     * returned feature matches the query. You can use this property to remove results which don't
     * fully match the query.
     *
     * @param relevance the relevant score between 0 and 1
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder relevance(@Nullable Double relevance) {
      this.relevance = relevance;
      return this;
    }

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
    public Builder matchingText(@Nullable String matchingText) {
      this.matchingText = matchingText;
      return this;
    }

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
    public Builder matchingPlaceName(@Nullable String matchingPlaceName) {
      this.matchingPlaceName = matchingPlaceName;
      return this;
    }

    /**
     * The language for the query.
     *
     * @param language string containing the query's primary language
     * @return this builder for chaining options together
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag Wikipedia page</a>
     * @since 2.2.0
     */
    public Builder language(@Nullable String language) {
      this.language = language;
      return this;
    }

    /**
     * This field is only returned when multiple languages are requested using
     * the language parameter, and will be present for each requested language.
     *
     * @param languages list of strings containing language tags
     * @return this builder for chaining options together
     * @since 4.9.0
     */
    public Builder languages(@Nullable List<String> languages) {
      this.languages = languages;
      return this;
    }

    /**
     * Build a new {@link CarmenFeature} object.
     *
     * @return a new {@link CarmenFeature} using the provided values in this builder
     * @since 3.0.0
     */
    public CarmenFeature build() {
      String missing = "";
      if (this.type == null) {
        missing += " type";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new CarmenFeature(
              this.type,
              this.bbox,
              this.id,
              this.geometry,
              this.properties,
              this.text,
              this.texts,
              this.placeName,
              this.placeNames,
              this.placeType,
              this.address,
              this.rawCenter,
              this.context,
              this.relevance,
              this.matchingText,
              this.matchingPlaceName,
              this.language,
              this.languages);
    }
  }

  /**
   * @since 4.9.0
   */
  private static final class GsonTypeAdapter extends GeocodingTypeAdapter<CarmenFeature> {

    private volatile TypeAdapter<BoundingBox> boundingBoxAdapter;

    private volatile TypeAdapter<Geometry> geometryAdapter;

    private volatile TypeAdapter<JsonObject> jsonObjectAdapter;

    private volatile TypeAdapter<List<String>> listOfStringAdapter;

    private volatile TypeAdapter<double[]> arrayDoubleAdapter;

    private volatile TypeAdapter<List<CarmenContext>> listOfCarmenContextAdapter;

    private volatile TypeAdapter<Double> doubleAdapter;

    private GsonTypeAdapter(Gson gson) {
      super(gson);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, CarmenFeature object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }

      jsonWriter.beginObject();
      writeString("type", object.type(), jsonWriter);

      jsonWriter.name("bbox");
      if (object.bbox() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<BoundingBox> boundingBoxAdapter = this.boundingBoxAdapter;
        if (boundingBoxAdapter == null) {
          boundingBoxAdapter = gson.getAdapter(BoundingBox.class);
          this.boundingBoxAdapter = boundingBoxAdapter;
        }
        boundingBoxAdapter.write(jsonWriter, object.bbox());
      }

      writeString("id", object.id(), jsonWriter);

      jsonWriter.name("geometry");
      if (object.geometry() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Geometry> geometryAdapter = this.geometryAdapter;
        if (geometryAdapter == null) {
          geometryAdapter = gson.getAdapter(Geometry.class);
          this.geometryAdapter = geometryAdapter;
        }
        geometryAdapter.write(jsonWriter, object.geometry());
      }

      jsonWriter.name("properties");
      if (object.properties() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<JsonObject> jsonObjectAdapter = this.jsonObjectAdapter;
        if (jsonObjectAdapter == null) {
          jsonObjectAdapter = gson.getAdapter(JsonObject.class);
          this.jsonObjectAdapter = jsonObjectAdapter;
        }
        jsonObjectAdapter.write(jsonWriter, object.properties());
      }

      writeString("text", object.text(), jsonWriter);
      writeLocalizedMap("text", object.texts(), jsonWriter);

      writeString("place_name", object.placeName(), jsonWriter);
      writeLocalizedMap("place_name", object.placeNames(), jsonWriter);

      jsonWriter.name("place_type");
      if (object.placeType() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<String>> listOfStringAdapter = this.listOfStringAdapter;
        if (listOfStringAdapter == null) {
          listOfStringAdapter =
            (TypeAdapter<List<String>>) gson.getAdapter(TypeToken.getParameterized(List.class,
              String.class));
          this.listOfStringAdapter = listOfStringAdapter;
        }
        listOfStringAdapter.write(jsonWriter, object.placeType());
      }

      writeString("address", object.address(), jsonWriter);

      jsonWriter.name("center");
      if (object.rawCenter() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<double[]> arrayDoubleAdapter = this.arrayDoubleAdapter;
        if (arrayDoubleAdapter == null) {
          arrayDoubleAdapter = gson.getAdapter(double[].class);
          this.arrayDoubleAdapter = arrayDoubleAdapter;
        }
        arrayDoubleAdapter.write(jsonWriter, object.rawCenter());
      }

      jsonWriter.name("context");
      if (object.context() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<CarmenContext>> listOfCarmenContextAdapter =
          this.listOfCarmenContextAdapter;
        if (listOfCarmenContextAdapter == null) {
          listOfCarmenContextAdapter =
            (TypeAdapter<List<CarmenContext>>) gson
              .getAdapter(TypeToken.getParameterized(List.class, CarmenContext.class));
          this.listOfCarmenContextAdapter = listOfCarmenContextAdapter;
        }
        listOfCarmenContextAdapter.write(jsonWriter, object.context());
      }

      jsonWriter.name("relevance");
      if (object.relevance() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<Double> doubleAdapter = this.doubleAdapter;
        if (doubleAdapter == null) {
          doubleAdapter = gson.getAdapter(Double.class);
          this.doubleAdapter = doubleAdapter;
        }
        doubleAdapter.write(jsonWriter, object.relevance());
      }

      writeString("matching_text", object.matchingText(), jsonWriter);
      writeString("matching_place_name", object.matchingPlaceName(), jsonWriter);

      writeString("language", object.language(), jsonWriter);
      writeLanguages(object.languages(), jsonWriter);

      jsonWriter.endObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CarmenFeature read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      String id = null;
      Geometry geometry = null;
      JsonObject properties = null;
      String text = null;
      Map<String, String> texts = null;
      String placeName = null;
      Map<String, String> placeNames = null;
      List<String> placeType = null;
      String address = null;
      double[] rawCenter = null;
      List<CarmenContext> context = null;
      Double relevance = null;
      String matchingText = null;
      String matchingPlaceName = null;
      String language = null;
      List<String> languages = null;
      while (jsonReader.hasNext()) {
        String name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (name) {
          case "type":
            type = readString(jsonReader);
            break;

          case "bbox":
            TypeAdapter<BoundingBox> boundingBoxAdapter = this.boundingBoxAdapter;
            if (boundingBoxAdapter == null) {
              boundingBoxAdapter = gson.getAdapter(BoundingBox.class);
              this.boundingBoxAdapter = boundingBoxAdapter;
            }
            bbox = boundingBoxAdapter.read(jsonReader);
            break;

          case "id":
            id = readString(jsonReader);
            break;

          case "geometry":
            TypeAdapter<Geometry> geometryAdapter = this.geometryAdapter;
            if (geometryAdapter == null) {
              geometryAdapter = gson.getAdapter(Geometry.class);
              this.geometryAdapter = geometryAdapter;
            }
            geometry = geometryAdapter.read(jsonReader);
            break;

          case "properties":
            TypeAdapter<JsonObject> jsonObjectAdapter = this.jsonObjectAdapter;
            if (jsonObjectAdapter == null) {
              jsonObjectAdapter = gson.getAdapter(JsonObject.class);
              this.jsonObjectAdapter = jsonObjectAdapter;

            }
            properties = jsonObjectAdapter.read(jsonReader);
            break;

          case "text":
            text = readString(jsonReader);
            break;

          case "place_name":
            placeName = readString(jsonReader);
            break;

          case "place_type":
            TypeAdapter<List<String>> listOfStringAdapter = this.listOfStringAdapter;
            if (listOfStringAdapter == null) {
              listOfStringAdapter = (TypeAdapter<List<String>>) gson
                .getAdapter(TypeToken.getParameterized(List.class, String.class));
              this.listOfStringAdapter = listOfStringAdapter;
            }
            placeType = listOfStringAdapter.read(jsonReader);
            break;

          case "address":
            address = readString(jsonReader);
            break;

          case "center":
            TypeAdapter<double[]> arrayDoubleAdapter = this.arrayDoubleAdapter;
            if (arrayDoubleAdapter == null) {
              arrayDoubleAdapter = gson.getAdapter(double[].class);
              this.arrayDoubleAdapter = arrayDoubleAdapter;
            }
            rawCenter = arrayDoubleAdapter.read(jsonReader);
            break;

          case "context":
            TypeAdapter<List<CarmenContext>> listOfCarmenContextAdapter =
                    this.listOfCarmenContextAdapter;
            if (listOfCarmenContextAdapter == null) {
              listOfCarmenContextAdapter = (TypeAdapter<List<CarmenContext>>) gson
                .getAdapter(TypeToken.getParameterized(List.class, CarmenContext.class));
              this.listOfCarmenContextAdapter = listOfCarmenContextAdapter;
            }
            context = listOfCarmenContextAdapter.read(jsonReader);
            break;

          case "relevance":
            TypeAdapter<Double> doubleAdapter = this.doubleAdapter;
            if (doubleAdapter == null) {
              this.doubleAdapter = doubleAdapter;
              doubleAdapter = gson.getAdapter(Double.class);
            }
            relevance = doubleAdapter.read(jsonReader);
            break;

          case "matching_text":
            matchingText = readString(jsonReader);
            break;

          case "matching_place_name":
            matchingPlaceName = readString(jsonReader);
            break;

          case "language":
            language = readString(jsonReader);
            break;

          default:
            if (name.startsWith("language_")) {
              languages = readLanguages(languages, name.substring(9), jsonReader);

            } else if (name.startsWith("place_name_")) {
              placeNames = readLocalizedMap(placeNames, name.substring(11), jsonReader);

            } else if (name.startsWith("text_")) {
              texts = readLocalizedMap(texts, name.substring(5), jsonReader);

            } else {
              jsonReader.skipValue();
            }
        }
      }
      jsonReader.endObject();
      return new CarmenFeature(type, bbox, id, geometry, properties,
              text, texts, placeName, placeNames, placeType, address, rawCenter,
              context, relevance, matchingText, matchingPlaceName, language, languages);
    }
  }
}
