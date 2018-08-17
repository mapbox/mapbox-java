package com.mapbox.api.tilequery;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Constants that should be used when using the Tilequery API.
 *
 * @since 3.5.0
 */
public class TilequeryCriteria {

  /**
   * Queries for a specific geometry type (polygon).
   *
   * @since 3.5.0
   */
  public static final String TILEQUERY_GEOMETRY_POLYGON = "polygon";

  /**
   * Queries for a specific geometry type (linestring).
   *
   * @since 3.5.0
   */
  public static final String TILEQUERY_GEOMETRY_LINESTRING = "linestring";

  /**
   * Queries for a specific geometry type (point).
   *
   * @since 3.5.0
   */
  public static final String TILEQUERY_GEOMETRY_POINT = "point";

  /**
   * Queries for a specific geometry type selector.
   *
   * @since 3.5.0
   */
  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    TILEQUERY_GEOMETRY_POLYGON,
    TILEQUERY_GEOMETRY_LINESTRING,
    TILEQUERY_GEOMETRY_POINT
  })
  public @interface TilequeryGeometry {
  }

}
