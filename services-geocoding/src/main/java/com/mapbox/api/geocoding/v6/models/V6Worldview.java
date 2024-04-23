package com.mapbox.api.geocoding.v6.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Worldview parameters are used to identify geographic features whose characteristics
 * are defined differently by audiences belonging to various regional, cultural,
 * or political groups.
 *
 * The Geocoding API only supports worldviews for the country and region data types,
 * but may expand to additional data types in the future.
 *
 * Learn more about worldviews
 * <a href="https://docs.mapbox.com/api/search/geocoding/#worldviews">here</a>.
 */
public class V6Worldview {

  /**
   * Features for an Argentinian audience.
   */
  public static final String ARGENTINA = "ar";

  /**
   * Features for a mainland Chinese audience.
   */
  public static final String CHINA = "cn";

  /**
   * Features for an Indian audience.
   */
  public static final String INDIA = "in";

  /**
   * Features for a Japanese audience.
   */
  public static final String JAPAN = "jp";

  /**
   * Features for a Moroccan audience.
   */
  public static final String MOROCCO = "ma";

  /**
   * Features for a Russian audience.
   */
  public static final String RUSSIA = "ru";

  /**
   * Features for a Turkish audience.
   */
  public static final String TURKEY = "tr";

  /**
   * Features for an American audience.
   */
  public static final String USA = "us";

  private V6Worldview() {
    // private constructor to prevent the class being instantiated
  }

  /**
   * Retention policy for the worldviews.
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef( {
    ARGENTINA,
    CHINA,
    INDIA,
    JAPAN,
    MOROCCO,
    RUSSIA,
    TURKEY,
    USA,
  })
  public @interface Worldview {
  }
}
