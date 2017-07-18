package com.mapbox.services.api.geocoding.v5.models;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.geocoding.v5.gson.CarmenGeometryDeserializer;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.models.Position;

import java.util.List;

/**
 * The Features key in the geocoding API response contains the majority of information you'll want
 * to use.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#geocoding">Mapbox geocoder documentation</a>
 * @see <a href='geojson.org/geojson-spec.html#feature-objects'>Official GeoJSON Feature Specifications</a>
 * @since 1.0.0
 */
public class CarmenFeature extends Feature {

  private String text;
  @SerializedName("place_name")
  private String placeName;
  @SerializedName("place_type")
  private String[] placeType;
  private double[] bbox;
  private String address;
  private double[] center;
  private List<CarmenContext> context;
  private double relevance;
  @SerializedName("matching_text")
  private String matchingText;
  @SerializedName("matching_place_name")
  private String matchingPlaceName;
  private String language;

  /**
   * Empty constructor
   */
  public CarmenFeature() {
    super(null, null, null);
  }

  /**
   * Private constructor.
   */
  private CarmenFeature(Geometry geometry, JsonObject properties, String id) {
    super(geometry, properties, id);
  }

  /**
   * A string representing the feature in the requested language, if specified.
   *
   * @return text representing the feature (e.g. "Austin")
   * @since 1.0.0
   */
  public String getText() {
    return text;
  }

  /**
   * Set a string representing the feature in the requested language, if specified.
   *
   * @param text text representing the feature (e.g. "Austin")
   * @since 2.0.0
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * A string representing the feature in the requested language, if specified, and its full result hierarchy.
   *
   * @return Human-readable text representing the full result hierarchy (e.g. "Austin, Texas, United States").
   * @since 1.0.0
   */
  public String getPlaceName() {
    return placeName;
  }

  /**
   * Set a string representing the feature in the requested language, if specified, and its full result hierarchy.
   *
   * @param placeName human-readable text representing the full result hierarchy
   * @since 2.0.0
   */
  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  /**
   * An array bounding box in the form {@code [minX, minY, maxX, maxY]}.
   *
   * @return Array bounding box of the form [minx, miny, maxx, maxy].
   * @since 1.0.0
   */
  public double[] getBbox() {
    return bbox;
  }

  /**
   * Set an array bounding box in the form {@code [minX, minY, maxX, maxY]}.
   *
   * @param bbox array bounding box of the form [minx, miny, maxx, maxy]
   * @since 2.0.0
   */
  public void setBbox(double[] bbox) {
    this.bbox = bbox;
  }

  /**
   * A string of the house number for the returned {@code address} feature. Note that unlike the address property for
   * {@code poi} features, this property is outside the  properties object.
   *
   * @return Where applicable. While the string content isn't guaranteed, in many cases, this will be the house number.
   * If the response doesn't contain an address this will be null.
   * @since 1.0.0
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set a string of the house number for the returned {@code address} feature.
   *
   * @param address Where applicable. While the string content isn't guaranteed, in many cases, this will be the house
   *                number. If the response doesn't contain an address this will be null.
   * @since 2.0.0
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * An array in the form [longitude, latitude] at the center of the specified {@code bbox}.
   *
   * @return Array of the form [lon, lat].
   * @since 1.0.0
   */
  public double[] getCenter() {
    return center;
  }

  /**
   * Set an array in the form [longitude, latitude] at the center of the specified {@code bbox}.
   *
   * @param center array of the form [lon, lat]
   * @since 2.0.0
   */
  public void setCenter(double[] center) {
    this.center = center;
  }

  /**
   * An array representing the hierarchy of encompassing parent features. Each parent feature may include address,
   * category, tel, maki, landmark, wikidata, or short code.
   *
   * @return Array representing a hierarchy of parents. Each parent includes id, text keys.
   * @since 1.0.0
   */
  public List<CarmenContext> getContext() {
    return context;
  }

  /**
   * Set an array representing the hierarchy of encompassing parent features. Each parent feature may include address,
   * category, tel, maki, landmark, wikidata, or short code.
   *
   * @param context Array representing a hierarchy of parents. Each parent includes id, text keys.
   * @since 2.0.0
   */
  public void setContext(List<CarmenContext> context) {
    this.context = context;
  }

  /**
   * You can use the relevance property to remove rough results if you require a response that
   * matches your whole query.
   *
   * @return double value between 0 and 1.
   * @since 1.0.0
   */
  public double getRelevance() {
    return relevance;
  }

  /**
   * Set the relevance property to remove rough results if you require a response that matches your whole query.
   *
   * @param relevance double value between 0 and 1
   * @since 2.0.0
   */
  public void setRelevance(double relevance) {
    this.relevance = relevance;
  }

  /**
   * A string of the IETF language tag of the query's primary language.
   *
   * @return string containing the query's primary language
   * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag Wikipedia page</a>
   * @since 2.2.0
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Set a string of the language tag of the query;s primary language.
   *
   * @param language locale object converted to a string
   * @since 2.2.0
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * A string analogous to the {@link CarmenFeature#getPlaceName()} field that more closely matches the query than
   * results in the specified language. For example, querying "Köln, Germany" with language set to English might
   * return a feature with the  {@code placeName} "Cologne, Germany" and a matching_place_name of
   * "Köln, North Rhine-Westphalia, Germany".
   *
   * @return a string containing the matching place name
   * @since 2.2.0
   */
  public String getMatchingPlaceName() {
    return matchingPlaceName;
  }

  /**
   * Set a string analogous to the {@link CarmenFeature#getPlaceName()} field that more closely matches the query than
   * results in the specified language.
   *
   * @param matchingPlaceName a string containing the matching place name
   * @since 2.2.0
   */
  public void setMatchingPlaceName(String matchingPlaceName) {
    this.matchingPlaceName = matchingPlaceName;
  }

  /**
   * A string analogous to the {@code text} field that more closely matches the query than results in the specified
   * language. For example, querying "Köln, Germany" with language set to English might return a feature with the
   * {@code text} "Cologne" and the {@code matching_text} "Köln".
   *
   * @return a string containing the matching text
   * @since 2.2.0
   */
  public String getMatchingText() {
    return matchingText;
  }

  /**
   * A string analogous to the {@code text} field that more closely matches the query than results in the specified
   * language
   *
   * @param matchingText a string containing the matching text
   * @since 2.2.0
   */
  public void setMatchingText(String matchingText) {
    this.matchingText = matchingText;
  }

  /**
   * An array of feature types describing the feature. Options are country, region, postcode, district, place,
   * locality, neighborhood, address, poi, and poi.landmark. Most features have only one type, but if the feature has
   * multiple types, (for example, Vatican City is a country, region, and place), all applicable types will be listed
   * in the array.
   *
   * @return a {@code String} array containing the place types
   * @since 2.2.0
   */
  public String[] getPlaceType() {
    return placeType;
  }

  /**
   * An array of feature types describing the feature.
   *
   * @param placeType a {@code String} array containing the place types
   * @since 2.2.0
   */
  public void setPlaceType(String[] placeType) {
    this.placeType = placeType;
  }


  /**
   * Create a CarmenFeature object from JSON.
   *
   * @param json String of JSON making up a carmen feature.
   * @return Carmen Feature
   * @since 2.0.0
   */
  public static CarmenFeature fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(Geometry.class, new CarmenGeometryDeserializer());
    return gson.create().fromJson(json, CarmenFeature.class);
  }

  /**
   * Util to transform center into a Position object
   *
   * @return a {@link Position} representing the center.
   * @since 1.0.0
   */
  public Position asPosition() {
    return Position.fromCoordinates(center[0], center[1]);
  }

  /**
   * Human-readable text representing the full result hierarchy
   * (e.g. "Austin, Texas, United States").
   *
   * @return String with human-readable text.
   * @since 1.0.0
   */
  @Override
  public String toString() {
    return getPlaceName();
  }
}
