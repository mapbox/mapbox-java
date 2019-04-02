package com.mapbox.api.isochrone;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants that should be used when using the Isochrone API.
 *
 * @since 4.6.0
 */
public class IsochroneCriteria {


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
   * Queries for a specific geometry type selector.
   *
   * @since 4.6.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
      PROFILE_WALKING,
      PROFILE_DRIVING,
      PROFILE_CYCLING
  })
  public @interface IsochroneProfile {
  }

}
