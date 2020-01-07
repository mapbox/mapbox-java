package com.mapbox.turf;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
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
public final class TurfMeta {

  private TurfMeta() {
    // Private constructor preventing initialization of this class
  }

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
    return coordAll(new ArrayList<Point>(), point);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(Point)}.
   *
   * @param coords the {@code List} of {@link Point}s.
   * @param point  any {@link Point} object
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull Point point) {
    coords.add(point);
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
    return coordAll(new ArrayList<Point>(), multiPoint);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(MultiPoint)}.
   *
   * @param coords     the {@code List} of {@link Point}s.
   * @param multiPoint any {@link MultiPoint} object
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull MultiPoint multiPoint) {
    coords.addAll(multiPoint.coordinates());
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
    return coordAll(new ArrayList<Point>(), lineString);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(LineString)}.
   *
   * @param coords     the {@code List} of {@link Point}s.
   * @param lineString any {@link LineString} object
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull LineString lineString) {
    coords.addAll(lineString.coordinates());
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
  public static List<Point> coordAll(@NonNull Polygon polygon, @NonNull boolean excludeWrapCoord) {
    return coordAll(new ArrayList<Point>(), polygon, excludeWrapCoord);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(Polygon, boolean)}.
   *
   * @param coords           the {@code List} of {@link Point}s.
   * @param polygon          any {@link Polygon} object
   * @param excludeWrapCoord whether or not to include the final
   *                         coordinate of LinearRings that
   *                         wraps the ring in its iteration
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords,
                                      @NonNull Polygon polygon,
                                      @NonNull boolean excludeWrapCoord) {
    int wrapShrink = excludeWrapCoord ? 1 : 0;
    for (int i = 0; i < polygon.coordinates().size(); i++) {
      for (int j = 0; j < polygon.coordinates().get(i).size() - wrapShrink; j++) {
        coords.add(polygon.coordinates().get(i).get(j));
      }
    }
    return coords;
  }

  /**
   * Get all coordinates from a {@link MultiLineString} object, returning
   * a {@code List} of Point objects. If you have a geometry collection, you
   * need to break it down to individual geometry objects before using
   * {@link #coordAll}.
   *
   * @param multiLineString any {@link MultiLineString} object
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull MultiLineString multiLineString) {
    return coordAll(new ArrayList<Point>(), multiLineString);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(MultiLineString)}.
   *
   * @param coords          the {@code List} of {@link Point}s.
   * @param multiLineString any {@link MultiLineString} object
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords,
                                      @NonNull MultiLineString multiLineString) {
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
   *                         wraps the ring in its iteration. Used to handle {@link Polygon} and
   *                         {@link MultiPolygon} geometries.
   * @return a {@code List} made up of {@link Point}s
   * @since 2.0.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull MultiPolygon multiPolygon,
                                     @NonNull boolean excludeWrapCoord) {
    return coordAll(new ArrayList<Point>(), multiPolygon, excludeWrapCoord);
  }

  /**
   * Private helper method to go with {@link TurfMeta#coordAll(MultiPolygon, boolean)}.
   *
   * @param coords           the {@code List} of {@link Point}s.
   * @param multiPolygon     any {@link MultiPolygon} object
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that
   *                         wraps the ring in its iteration. Used to handle {@link Polygon} and
   *                         {@link MultiPolygon} geometries.
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAll(@NonNull List<Point> coords,
                                      @NonNull MultiPolygon multiPolygon,
                                      @NonNull boolean excludeWrapCoord) {
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

  /**
   * Get all coordinates from a {@link Feature} object, returning a {@code List} of {@link Point}
   * objects.
   *
   * @param feature          the {@link Feature} that you'd like to extract the Points from.
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that
   *                         wraps the ring in its iteration. Used if the {@link Feature}
   *                         passed through the method is a {@link Polygon} or {@link MultiPolygon}
   *                         geometry.
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull Feature feature,
                                     @NonNull boolean excludeWrapCoord) {
    return addCoordAll(new ArrayList<Point>(), feature, excludeWrapCoord);
  }

  /**
   * Get all coordinates from a {@link FeatureCollection} object, returning a
   * {@code List} of {@link Point} objects.
   *
   * @param featureCollection the {@link FeatureCollection} that you'd like
   *                          to extract the Points from.
   * @param excludeWrapCoord  whether or not to include the final coordinate of LinearRings that
   *                          wraps the ring in its iteration. Used if a {@link Feature} in the
   *                          {@link FeatureCollection} that's passed through this method, is a
   *                          {@link Polygon} or {@link MultiPolygon} geometry.
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  public static List<Point> coordAll(@NonNull FeatureCollection featureCollection,
                                     @NonNull boolean excludeWrapCoord) {
    List<Point> finalCoordsList = new ArrayList<>();
    for (Feature singleFeature : featureCollection.features()) {
      addCoordAll(finalCoordsList, singleFeature, excludeWrapCoord);
    }
    return finalCoordsList;
  }

  /**
   * Private helper method to be used with other methods in this class.
   *
   * @param pointList the {@code List} of {@link Point}s.
   * @param feature the {@link Feature} that you'd like
   *                          to extract the Points from.
   * @param excludeWrapCoord  whether or not to include the final
   *                          coordinate of LinearRings that wraps the ring
   *                          in its iteration. Used if a {@link Feature} in the
   *                          {@link FeatureCollection} that's passed through
   *                          this method, is a {@link Polygon} or {@link MultiPolygon}
   *                          geometry.
   * @return a {@code List} made up of {@link Point}s.
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> addCoordAll(@NonNull List<Point> pointList, @NonNull Feature feature,
                                         @NonNull boolean excludeWrapCoord) {
    return coordAllFromSingleGeometry(pointList, feature.geometry(), excludeWrapCoord);
  }

  /**
   * Get all coordinates from a {@link FeatureCollection} object, returning a
   * {@code List} of {@link Point} objects.
   *
   * @param pointList        the {@code List} of {@link Point}s.
   * @param geometry         the {@link Geometry} object to extract the {@link Point}s from
   * @param excludeWrapCoord whether or not to include the final coordinate of LinearRings that
   *                         wraps the ring in its iteration. Used if the {@link Feature}
   *                         passed through the method is a {@link Polygon} or {@link MultiPolygon}
   *                         geometry.
   * @return a {@code List} made up of {@link Point}s
   * @since 4.8.0
   */
  @NonNull
  private static List<Point> coordAllFromSingleGeometry(@NonNull List<Point> pointList,
                                                        @NonNull Geometry geometry,
                                                        @NonNull boolean excludeWrapCoord) {
    if (geometry instanceof Point) {
      pointList.add((Point) geometry);
    } else if (geometry instanceof MultiPoint) {
      pointList.addAll(((MultiPoint) geometry).coordinates());
    } else if (geometry instanceof LineString) {
      pointList.addAll(((LineString) geometry).coordinates());
    } else if (geometry instanceof MultiLineString) {
      coordAll(pointList, (MultiLineString) geometry);
    } else if (geometry instanceof Polygon) {
      coordAll(pointList, (Polygon) geometry, excludeWrapCoord);
    } else if (geometry instanceof MultiPolygon) {
      coordAll(pointList, (MultiPolygon) geometry, excludeWrapCoord);
    } else if (geometry instanceof GeometryCollection) {
      // recursive
      for (Geometry singleGeometry : ((GeometryCollection) geometry).geometries()) {
        coordAllFromSingleGeometry(pointList, singleGeometry, excludeWrapCoord);
      }
    }
    return pointList;
  }

  /**
   * Unwrap a coordinate {@link Point} from a {@link Feature} with a {@link Point} geometry.
   *
   * @param obj any value
   * @return a coordinate
   * @see <a href="http://turfjs.org/docs/#getcoord">Turf getCoord documentation</a>
   * @since 3.2.0
   */
  public static Point getCoord(Feature obj) {
    if (obj.geometry() instanceof Point) {
      return (Point) obj.geometry();
    }
    throw new TurfException("A Feature with a Point geometry is required.");
  }
}
