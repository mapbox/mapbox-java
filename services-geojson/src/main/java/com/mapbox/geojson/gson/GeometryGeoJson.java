package com.mapbox.geojson.gson;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

/**
 * This is a utility class that helps create a Geometry instance from a JSON string.
 * @since 4.0.0
 */
public class GeometryGeoJson {

  /**
   * Create a new instance of Geometry class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geometry
   * @return a new instance of Geometry class defined by the values passed inside
   *   this static factory method
   * @since 4.0.0
   */
  public static Geometry fromJson(@NonNull String json) {

//    final RuntimeTypeAdapterFactory<Geometry> geometryFactory = RuntimeTypeAdapterFactory
//            .of(Geometry.class, "type")
//            .registerSubtype(GeometryCollection.class, "GeometryCollection")
//            .registerSubtype(Point.class, "Point")
//            .registerSubtype(MultiPoint.class, "MultiPoint")
//            .registerSubtype(LineString.class, "LineString")
//            .registerSubtype(MultiLineString.class, "MultiLineString")
//            .registerSubtype(Polygon.class, "Polygon")
//            .registerSubtype(MultiPolygon.class, "MultiPolygon")
//            ;

    GsonBuilder gson = new GsonBuilder();

    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());

    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());

    return gson.create().fromJson(json, Geometry.class);
  }
}
