package com.mapbox.services.api.geocoding.v5;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
   * Geocoding mode for enterprise/batch geocoding.
   *
   * @since 1.0.0
   */
  public static final String MODE_PLACES_PERMANENT = "mapbox.places-permanent";

  /**
   * Retention policy for the various geocoding modes.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    MODE_PLACES,
    MODE_PLACES_PERMANENT
  })
  public @interface GeocodingModeCriteria {
  }

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
   * Filter results by district.
   *
   * @since 2.2.0
   */
  public static final String TYPE_DISTRICT = "district";

  /**
   * Filter results by place.
   *
   * @since 1.0.0
   */
  public static final String TYPE_PLACE = "place";

  /**
   * Filter results by locality.
   *
   * @since 2.2.0
   */
  public static final String TYPE_LOCALITY = "locality";

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

  /**
   * Retention policy for the various filter result types.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    TYPE_COUNTRY,
    TYPE_REGION,
    TYPE_POSTCODE,
    TYPE_DISTRICT,
    TYPE_PLACE,
    TYPE_LOCALITY,
    TYPE_NEIGHBORHOOD,
    TYPE_ADDRESS,
    TYPE_POI,
    TYPE_POI_LANDMARK
  })
  public @interface GeocodingTypeCriteria {
  }
}
