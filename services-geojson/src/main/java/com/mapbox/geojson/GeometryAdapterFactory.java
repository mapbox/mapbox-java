package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.TypeAdapterFactory;

import com.mapbox.geojson.internal.typeadapters.RuntimeTypeAdapterFactory;

/**
 * A Geometry type adapter factory for convenience for serialization/deserialization.
 * @since 4.6.0
 */
@Keep
public abstract class GeometryAdapterFactory implements TypeAdapterFactory  {

  private static TypeAdapterFactory geometryTypeFactory;


  /**
   * Create a new instance of Geometry type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 4.4.0
   */
  public static TypeAdapterFactory create() {

    if (geometryTypeFactory == null) {
      geometryTypeFactory = RuntimeTypeAdapterFactory.of(Geometry.class, "type", true)
        .registerSubtype(GeometryCollection.class, "GeometryCollection")
        .registerSubtype(Point.class, "Point")
        .registerSubtype(MultiPoint.class, "MultiPoint")
        .registerSubtype(LineString.class, "LineString")
        .registerSubtype(MultiLineString.class, "MultiLineString")
        .registerSubtype(Polygon.class, "Polygon")
         .registerSubtype(MultiPolygon.class, "MultiPolygon");
    }
    return geometryTypeFactory;
  }
}
