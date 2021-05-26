package com.mapbox.api.staticmap.v1;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constant values related to the Static Map API can be found in this class.
 *
 * @since 3.0.0
 */
public final class StaticMapCriteria {

  /**
   * The Static Maps marker shape and size will be small.
   *
   * @since 3.0.0
   */
  public static final String SMALL_PIN = "pin-s";

  /**
   * The Static Maps marker shape and size will be medium.
   *
   * @since 3.0.0
   */
  public static final String MEDIUM_PIN = "pin-m";

  /**
   * The Static Maps marker shape and size will be large.
   *
   * @since 3.0.0
   */
  public static final String LARGE_PIN = "pin-l";

  /**
   * Mapbox Streets is a comprehensive, general-purpose map that emphasizes accurate, legible
   * styling of road and transit networks.
   *
   * @since 3.0.0
   */
  public static final String STREET_STYLE = "streets-v11";

  /**
   * Mapbox Outdoors is a general-purpose map with curated tilesets and specialized styling tailored
   * to hiking, biking, and the most adventurous use cases.
   *
   * @since 3.0.0
   */
  public static final String OUTDOORS_STYLE = "outdoors-v11";


  /**
   * Mapbox Light style's a subtle, full-featured map designed to provide geographic context while
   * highlighting the data on your analytics dashboard, data visualization, or data overlay.
   *
   * @since 3.0.0
   */
  public static final String LIGHT_STYLE = "light-v10";

  /**
   * Mapbox Dark style's a subtle, full-featured map designed to provide geographic context while
   * highlighting the data on your analytics dashboard, data visualization, or data overlay.
   *
   * @since 3.0.0
   */
  public static final String DARK_STYLE = "dark-v10";

  /**
   * Mapbox Satellite is our full global base map that is perfect as a blank canvas or an overlay
   * for your own data.
   *
   * @since 3.0.0
   */
  public static final String SATELLITE_STYLE = "satellite-v9";

  /**
   * Mapbox Satellite Streets combines our Mapbox Satellite with vector data from Mapbox Streets.
   * The comprehensive set of road, label, and POI information brings clarity and context to the
   * crisp detail in our high-resolution satellite imagery.
   *
   * @since 3.0.0
   */
  public static final String SATELLITE_STREETS_STYLE = "satellite-streets-v11";

  /**
   * Navigation specific style that shows only the necessary information while a user is driving.
   *
   * @since 3.0.0
   */
  public static final String NAVIGATION_PREVIEW_DAY = "navigation-preview-day-v3";

  /**
   * Navigation specific style that shows only the necessary information while a user is driving.
   *
   * @since 3.0.0
   */
  public static final String NAVIGATION_PREVIEW_NIGHT = "navigation-preview-night-v3";

  /**
   * Navigation specific style that shows only the necessary information while a user is driving.
   *
   * @since 3.0.0
   */
  public static final String NAVIGATION_GUIDANCE_DAY = "navigation-guidance-day-v4";

  /**
   * Navigation specific style that shows only the necessary information while a user is driving.
   *
   * @since 3.0.0
   */
  public static final String NAVIGATION_GUIDANCE_NIGHT = "navigation-guidance-night-v4";

  private StaticMapCriteria() {
    throw new AssertionError("No Instances.");
  }

  /**
   * Retention policy for the pin parameter in the Static Map Marker Annotation API.
   *
   * @since 3.0.0
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef( {
    SMALL_PIN,
    MEDIUM_PIN,
    LARGE_PIN
  })
  public @interface MarkerCriteria {
  }
}
