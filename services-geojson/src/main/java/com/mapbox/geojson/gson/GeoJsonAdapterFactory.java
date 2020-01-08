package com.mapbox.geojson.gson;

import androidx.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

/**
 * A GeoJson type adapter factory for convenience for
 * serialization/deserialization.
 *
 * @since 3.0.0
 */
@Keep
public abstract class GeoJsonAdapterFactory implements TypeAdapterFactory {

  /**
   * Create a new instance of this GeoJson type adapter factory, this is passed into the Gson
   * Builder.
   *
   * @return a new GSON TypeAdapterFactory
   * @since 3.0.0
   */
  public static TypeAdapterFactory create() {
    return new GeoJsonAdapterFactoryIml();
  }

  /**
   * GeoJsonAdapterFactory implementation.
   *
   * @since 3.0.0
   */
  public static final class GeoJsonAdapterFactoryIml extends GeoJsonAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<?> rawType = type.getRawType();
      if (BoundingBox.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) BoundingBox.typeAdapter(gson);
      } else if (Feature.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) Feature.typeAdapter(gson);
      } else if (FeatureCollection.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) FeatureCollection.typeAdapter(gson);
      } else if (GeometryCollection.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) GeometryCollection.typeAdapter(gson);
      } else if (LineString.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) LineString.typeAdapter(gson);
      } else if (MultiLineString.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) MultiLineString.typeAdapter(gson);
      } else if (MultiPoint.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) MultiPoint.typeAdapter(gson);
      } else if (MultiPolygon.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) MultiPolygon.typeAdapter(gson);
      } else if (Polygon.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) Polygon.typeAdapter(gson);
      } else if (Point.class.isAssignableFrom(rawType)) {
        return (TypeAdapter<T>) Point.typeAdapter(gson);
      }
      return null;
    }
  }
}

