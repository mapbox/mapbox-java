package com.mapbox.services.api.utils.turf;

import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.geojson.MultiPoint;
import com.mapbox.services.commons.geojson.MultiPolygon;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.geojson.Polygon;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class TurfMeta {

  /**
   * Get all coordinates from a {@link Point} object, returning a {@code List} of Position objects. If you have a
   * geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param point any {@link Point} object.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(Point point) {
    List<Position> coords = new ArrayList<>();
    coords.add(point.getCoordinates());
    return coords;
  }

  /**
   * Get all coordinates from a {@link LineString} object, returning a {@code List} of Position objects. If you have a
   * geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param lineString any {@link LineString} object.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(LineString lineString) {
    List<Position> coords = new ArrayList<>();
    coords.addAll(lineString.getCoordinates());
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiPoint} object, returning a {@code List} of Position objects. If you have a
   * geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param multiPoint any {@link MultiPoint} object.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(MultiPoint multiPoint) {
    List<Position> coords = new ArrayList<>();
    coords.addAll(multiPoint.getCoordinates());
    return coords;
  }

  /**
   * Get all coordinates from a {@link Polygon} object, returning a {@code List} of Position objects. If you have a
   * geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param polygon          any {@link Polygon} object.
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that wraps the ring in its
   *                         iteration.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(Polygon polygon, boolean excludeWrapCoord) {
    List<Position> coords = new ArrayList<>();
    int wrapShrink = excludeWrapCoord ? 1 : 0;

    for (int i = 0; i < polygon.getCoordinates().size(); i++) {
      for (int j = 0; j < polygon.getCoordinates().get(i).size() - wrapShrink; j++) {
        coords.add(polygon.getCoordinates().get(i).get(j));
      }
    }
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiLineString} object, returning a {@code List} of Position objects. If you
   * have a geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param multiLineString any {@link MultiLineString} object.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(MultiLineString multiLineString) {
    List<Position> coords = new ArrayList<>();
    for (int i = 0; i < multiLineString.getCoordinates().size(); i++) {
      for (int j = 0; j < multiLineString.getCoordinates().get(i).size(); j++) {
        coords.add(multiLineString.getCoordinates().get(i).get(j));
      }
    }
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiPolygon} object, returning a {@code List} of Position objects. If you have
   * a geometry collection, you need to break it down to individual geometry objects before using {@code coordAll}.
   *
   * @param multiPolygon     any {@link MultiPolygon} object.
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that wraps the ring in its
   *                         iteration.
   * @return A {@code List} made up of {@link Position}s.
   * @since 2.0.0
   */
  public static List<Position> coordAll(MultiPolygon multiPolygon, boolean excludeWrapCoord) {
    List<Position> coords = new ArrayList<>();
    int wrapShrink = excludeWrapCoord ? 1 : 0;

    for (int i = 0; i < multiPolygon.getCoordinates().size(); i++) {
      for (int j = 0; j < multiPolygon.getCoordinates().get(i).size(); j++) {
        for (int k = 0; k < multiPolygon.getCoordinates().get(i).get(j).size() - wrapShrink; k++) {
          coords.add(multiPolygon.getCoordinates().get(i).get(j).get(k));
        }
      }
    }
    return coords;
  }
}
