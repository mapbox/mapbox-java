package com.mapbox.api.tilequery;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TilequeryCriteria {

  public static final String TILEQUERY_GEOMETRY_POLYGON = "polygon";
  public static final String TILEQUERY_GEOMETRY_LINESTRING = "linestring";
  public static final String TILEQUERY_GEOMETRY_POINT = "point";

  @Retention(RetentionPolicy.SOURCE)
  @StringDef( {
    TILEQUERY_GEOMETRY_POLYGON,
    TILEQUERY_GEOMETRY_LINESTRING,
    TILEQUERY_GEOMETRY_POINT
  })
  public @interface TilequeryGeometry {
  }

}
