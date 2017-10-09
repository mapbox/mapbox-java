package com.mapbox.turf;

import android.support.annotation.NonNull;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains methods that are useful for getting all coordinates from a specific GeoJson
 * geometry.
 *
 * @see <a href="http://turfjs.org/docs/">Turf documentation</a>
 * @since 2.0.0
 */
public class TurfMeta {

  /**
   * Get all coordinates from a {@link Point} object, returning a {@code List} of Point objects.
   * If you have a geometry collection, you need to break it down to individual geometry objects
   * before using {@link #coordAll}.
   *
   * @param point any {@link Point} object
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull Point point) {
    List<Point> coords = new ArrayList<>();
    coords.add(point);
    return coords;
  }

  /**
   * Get all coordinates from a {@link LineString} object, returning a {@code List} of Point
   * objects. If you have a geometry collection, you need to break it down to individual geometry
   * objects before using {@link #coordAll}.
   *
   * @param lineString any {@link LineString} object
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull LineString lineString) {
    List<Point> coords = new ArrayList<>();
    coords.addAll(lineString.coordinates());
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiPoint} object, returning a {@code List} of Point
   * objects. If you have a geometry collection, you need to break it down to individual geometry
   * objects before using {@link #coordAll}.
   *
   * @param multiPoint any {@link MultiPoint} object
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull MultiPoint multiPoint) {
    List<Point> coords = new ArrayList<>();
    coords.addAll(multiPoint.coordinates());
    return coords;
  }

  /**
   * Get all coordinates from a {@link Polygon} object, returning a {@code List} of Point objects.
   * If you have a geometry collection, you need to break it down to individual geometry objects
   * before using {@link #coordAll}.
   *
   * @param polygon          any {@link Polygon} object
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that
   *                         wraps the ring in its iteration
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull Polygon polygon, boolean excludeWrapCoord) {
    List<Point> coords = new ArrayList<>();
    int wrapShrink = excludeWrapCoord ? 1 : 0;

    for (int i = 0; i < polygon.coordinates().size(); i++) {
      for (int j = 0; j < polygon.coordinates().get(i).size() - wrapShrink; j++) {
        coords.add(polygon.coordinates().get(i).get(j));
      }
    }
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiLineString} object, returning a {@code List} of Point
   * objects. If you have a geometry collection, you need to break it down to individual geometry
   * objects before using {@link #coordAll}.
   *
   * @param multiLineString any {@link MultiLineString} object
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull MultiLineString multiLineString) {
    List<Point> coords = new ArrayList<>();
    for (int i = 0; i < multiLineString.coordinates().size(); i++) {
      coords.addAll(multiLineString.coordinates().get(i));
    }
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiPolygon} object, returning a {@code List} of Point
   * objects. If you have a geometry collection, you need to break it down to individual geometry
   * objects before using {@link #coordAll}.
   *
   * @param multiPolygon     any {@link MultiPolygon} object
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that
   *                         wraps the ring in its iteration
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull MultiPolygon multiPolygon, boolean excludeWrapCoord) {
    List<Point> coords = new ArrayList<>();
    int wrapShrink = excludeWrapCoord ? 1 : 0;

    for (int i = 0; i < multiPolygon.coordinates().size(); i++) {
      for (int j = 0; j < multiPolygon.coordinates().get(i).size(); j++) {
        for (int k = 0; k < multiPolygon.coordinates().get(i).get(j).size() - wrapShrink; k++) {
          coords.add(multiPolygon.coordinates().get(i).get(j).get(k));
        }
      }
    }
    return coords;
  }
}
