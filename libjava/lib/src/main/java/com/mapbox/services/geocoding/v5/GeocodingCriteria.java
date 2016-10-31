package com.mapbox.services.geocoding.v5;

/**
 * Constants that should be used when requesting geocoding.
 *
 * @since 1.0.0
 */
public final class GeocodingCriteria {

  /**
   * Default geocoding mode.
   *
   * @since 1.0.0
   */
  public static final String MODE_PLACES = "mapbox.places";

  /**
   * Geocoding mode for for enterprise/batch geocoding.
   *
   * @since 1.0.0
   */
  public static final String MODE_PLACES_PERMANENT = "mapbox.places-permanent";

  /**
   * Filter results by country.
   *
   * @since 1.0.0
   */
  public static final String TYPE_COUNTRY = "country";

  /**
   * Filter results by region.
   *
   * @since 1.0.0
   */
  public static final String TYPE_REGION = "region";

  /**
   * Filter results by postcode.
   *
   * @since 1.0.0
   */
  public static final String TYPE_POSTCODE = "postcode";

  /**
   * Filter results by place.
   *
   * @since 1.0.0
   */
  public static final String TYPE_PLACE = "place";

  /**
   * Filter results by neighborhood.
   *
   * @since 1.0.0
   */
  public static final String TYPE_NEIGHBORHOOD = "neighborhood";

  /**
   * Filter results by address.
   *
   * @since 1.0.0
   */
  public static final String TYPE_ADDRESS = "address";

  /**
   * Filter results by POI.
   *
   * @since 1.0.0
   */
  public static final String TYPE_POI = "poi";


  /**
   * Filter results by POI landmark subtype.
   *
   * @since 1.3.2
   */
  public static final String TYPE_POI_LANDMARK = "poi.landmark";
}
