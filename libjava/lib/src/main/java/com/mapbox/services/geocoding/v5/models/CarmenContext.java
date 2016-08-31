package com.mapbox.services.geocoding.v5.models;

import com.google.gson.annotations.SerializedName;

/**
 * Array representing a hierarchy of parents. Each parent includes id, text keys along with
 * (if avaliable) a wikidata, short_code, and Maki key.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#geocoding">Mapbox geocoder documentation</a>
 * @since 1.0.0
 */
public class CarmenContext {

  private String id;
  private String text;

  @SerializedName("short_code")
  private String shortCode;
  private String wikidata;
  private String category;
  private String maki;

  /**
   * ID of the feature of the form {index}.{id} where index is the id/handle of the datasource
   * that contributed the result.
   *
   * @return String containing the ID.
   * @since 1.0.0
   */
  public String getId() {
    return id;
  }

  /**
   * @return Text representing the feature (e.g. "Austin").
   * @since 1.0.0
   */
  public String getText() {
    return text;
  }

  /**
   * @return String containing ISO 3166-1 country and ISO 3166-2 region codes
   * @since 1.0.0
   */
  public String getShortCode() {
    return shortCode;
  }

  /**
   * @return The Wikidata identifier for a country, region or place
   * @since 1.2.0
   */
  public String getWikidata() {
    return wikidata;
  }

  /**
   * @return Comma-separated list of categories applicable to a poi
   * @since 1.0.0
   */
  public String getCategory() {
    return category;
  }

  /**
   * Suggested icon mapping from the most current version of the Maki project for a poi feature,
   * based on its category
   *
   * @return String containing recommendation
   * @since 1.2.0
   */
  public String getMaki() {
    return maki;
  }
}
