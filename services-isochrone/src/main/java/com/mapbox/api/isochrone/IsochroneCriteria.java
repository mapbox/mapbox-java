package com.mapbox.api.isochrone;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants that should be used when using the Isochrone API.
 *
 * @since 4.6.0
 */
public class IsochroneCriteria {


  /**
   * Mapbox default username.
   *
   * @since 4.7.0
   */
  public static final String PROFILE_DEFAULT_USER = "mapbox";

  /**
   * For walking routing. This profile shows routes that are short and safe for cyclist, avoiding
   * highways and preferring streets with bike lanes.
   *
   * @since 4.6.0
   */
  public static final String PROFILE_WALKING = "walking";

  /**
   * For car routing. This profile shows the fastest routes by
   * preferring high-speed roads like highways.
   *
   * @since 4.6.0
   */
  public static final String PROFILE_DRIVING = "driving";

  /**
   * For bicycle routing. This profile shows routes that are short and safe for cyclist, avoiding
   * highways and preferring streets with bike lanes.
   *
   * @since 4.6.0
   */
  public static final String PROFILE_CYCLING = "cycling";

  /**
   * For fastest travel by car using current and historic traffic conditions.
   * Traffic information is available for the supported geographies listed in our Traffic Data page.
   */
  public static final String PROFILE_DRIVING_TRAFFIC = "driving-traffic";

  /**
   * Queries for a specific geometry type selector.
   *
   * @since 4.6.0
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef( {
      PROFILE_WALKING,
      PROFILE_DRIVING,
      PROFILE_CYCLING,
      PROFILE_DRIVING_TRAFFIC
  })
  public @interface IsochroneProfile {
  }

}
