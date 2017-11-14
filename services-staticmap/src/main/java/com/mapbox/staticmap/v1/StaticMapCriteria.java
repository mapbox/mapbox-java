package com.mapbox.staticmap.v1;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;


public final class StaticMapCriteria {

  private StaticMapCriteria() {
    // Empty private constructor since only static methods are found inside class.
  }

  public static final String SMALL_PIN = "pin-s";

  public static final String MEDIUM_PIN = "pin-m";

  public static final String LARGE_PIN = "pin-l";

  @Retention(SOURCE)
  @StringDef({
    SMALL_PIN,
    MEDIUM_PIN,
    LARGE_PIN
  })
  public @interface MarkerCriteria {
  }


  static final String CAMERA_AUTO = "auto";

  static final String BEFORE_LAYER = "before_layer";

  public static final String STREET_STYLE = "streets-v10";
  public static final String OUTDOORS_STYLE = "outdoors-v10";
  public static final String LIGHT_STYLE = "light-v9";
  public static final String DARK_STYLE = "dark-v9";
  public static final String SATELLITE_STYLE = "satellite-v9";
  public static final String SATELLITE_STREETS_STYLE = "satellite-streets-v10";

  public static final String NAVIGATION_PREVIEW_DAY = "navigation-preview-day-v2";
  public static final String NAVIGATION_PREVIEW_NIGHT = "navigation-preview-night-v2";
  public static final String NAVIGATION_GUIDANCE_DAY = "navigation-guidance-day-v2";
  public static final String NAVIGATION_GUIDANCE_NIGHT = "navigation-guidance-night-v2";
}
