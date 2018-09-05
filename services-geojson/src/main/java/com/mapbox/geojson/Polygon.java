package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a GeoJson Polygon which may or may not include polygon holes.
 * <p>
 * To specify a constraint specific to Polygons, it is useful to introduce the concept of a linear
 * ring:
 * <ul>
 * <li>A linear ring is a closed LineString with four or more coordinates.</li>
 * <li>The first and last coordinates are equivalent, and they MUST contain identical values; their
 * representation SHOULD also be identical.</li>
 * <li>A linear ring is the boundary of a surface or the boundary of a hole in a surface.</li>
 * <li>A linear ring MUST follow the right-hand rule with respect to the area it bounds, i.e.,
 * exterior rings are counterclockwise, and holes are clockwise.</li>
 * </ul>
 * Note that most of the rules listed above are checked when a Polygon instance is created (the
 * exception being the last rule). If one of the rules is broken, a {@link RuntimeException} will
 * occur.
 * <p>
 * Though a linear ring is not explicitly represented as a GeoJson geometry TYPE, it leads to a
 * canonical formulation of the Polygon geometry TYPE. When initializing a new instance of this
 * class, a LineString for the outer and optionally an inner are checked to ensure a valid linear
 * ring.
 * <p>
 * An example of a serialized polygon with no holes is given below:
 * <pre>
 * {
 *   "TYPE": "Polygon",
 *   "coordinates": [
 *     [[100.0, 0.0],
 *     [101.0, 0.0],
 *     [101.0, 1.0],
 *     [100.0, 1.0],
 *     [100.0, 0.0]]
 *   ]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class Polygon implements CoordinateContainer<List<List<Point>>>, Serializable {

  private static final String TYPE = "Polygon";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Polygon object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromOuterInner(LineString, LineString...)}. For a valid
   * For a valid Polygon to exist, it must follow the linear ring rules and the first list of
   * coordinates are considered the outer ring by default.
   *
   * @param json a formatted valid JSON string defining a GeoJson Polygon
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Polygon fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    return gson.create().fromJson(json, Polygon.class);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Point}s which follow the
   * correct specifications described in the Point documentation. Note that the first and last point
   * in the list should be the same enclosing the linear ring.
   *
   * @param coordinates a list of a list of points which represent the polygon geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromLngLats(@NonNull List<List<Point>> coordinates) {
    return new AutoValue_Polygon(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Point}s which follow the
   * correct specifications described in the Point documentation. Note that the first and last point
   * in the list should be the same enclosing the linear ring.
   *
   * @param coordinates a list of a list of points which represent the polygon geometry
   * @param bbox        optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromLngLats(@NonNull List<List<Point>> coordinates,
                                    @Nullable BoundingBox bbox) {
    return new AutoValue_Polygon(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class by passing in three dimensional double array which defines
   * the geometry of this polygon.
   *
   * @param coordinates a three dimensional double array defining this polygons geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  static Polygon fromLngLats(@NonNull double[][][] coordinates) {
    List<List<Point>> converted = new ArrayList<>(coordinates.length);
    for (double[][] coordinate : coordinates) {
      List<Point> innerList = new ArrayList<>(coordinate.length);
      for (double[] pointCoordinate : coordinate) {
        innerList.add(Point.fromLngLat(pointCoordinate));
      }
      converted.add(innerList);
    }
    return new AutoValue_Polygon(TYPE, null, converted);
  }

  /**
   * Create a new instance of this class by passing in an outer {@link LineString} and optionally
   * one or more inner LineStrings. Each of these LineStrings should follow the linear ring rules.
   * <p>
   * Note that if a LineString breaks one of the linear ring rules, a {@link RuntimeException} will
   * be thrown.
   *
   * @param outer a LineString which defines the outer perimeter of the polygon
   * @param inner one or more LineStrings representing holes inside the outer perimeter
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable LineString... inner) {
    isLinearRing(outer);
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(outer.coordinates());
    // If inner rings are set to null, return early.
    if (inner == null) {
      return new AutoValue_Polygon(TYPE, null, coordinates);
    }
    for (LineString lineString : inner) {
      isLinearRing(lineString);
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_Polygon(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by passing in an outer {@link LineString} and optionally
   * one or more inner LineStrings. Each of these LineStrings should follow the linear ring rules.
   * <p>
   * Note that if a LineString breaks one of the linear ring rules, a {@link RuntimeException} will
   * be thrown.
   *
   * @param outer a LineString which defines the outer perimeter of the polygon
   * @param bbox  optionally include a bbox definition as a double array
   * @param inner one or more LineStrings representing holes inside the outer perimeter
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable BoundingBox bbox,
                                       @Nullable LineString... inner) {
    isLinearRing(outer);
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(outer.coordinates());
    // If inner rings are set to null, return early.
    if (inner == null) {
      return new AutoValue_Polygon(TYPE, bbox, coordinates);
    }
    for (LineString lineString : inner) {
      isLinearRing(lineString);
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_Polygon(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class by passing in an outer {@link LineString} and optionally
   * one or more inner LineStrings contained within a list. Each of these LineStrings should follow
   * the linear ring rules.
   * <p>
   * Note that if a LineString breaks one of the linear ring rules, a {@link RuntimeException} will
   * be thrown.
   *
   * @param outer a LineString which defines the outer perimeter of the polygon
   * @param inner one or more LineStrings inside a list representing holes inside the outer
   *              perimeter
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromOuterInner(@NonNull LineString outer,
                                       @Nullable @Size(min = 1) List<LineString> inner) {
    isLinearRing(outer);
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(outer.coordinates());
    // If inner rings are set to null, return early.
    if (inner == null || inner.isEmpty()) {
      return new AutoValue_Polygon(TYPE, null, coordinates);
    }
    for (LineString lineString : inner) {
      isLinearRing(lineString);
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_Polygon(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by passing in an outer {@link LineString} and optionally
   * one or more inner LineStrings contained within a list. Each of these LineStrings should follow
   * the linear ring rules.
   * <p>
   * Note that if a LineString breaks one of the linear ring rules, a {@link RuntimeException} will
   * be thrown.
   *
   * @param outer a LineString which defines the outer perimeter of the polygon
   * @param bbox  optionally include a bbox definition as a double array
   * @param inner one or more LineStrings inside a list representing holes inside the outer
   *              perimeter
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable BoundingBox bbox,
                                       @Nullable @Size(min = 1) List<LineString> inner) {
    isLinearRing(outer);
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(outer.coordinates());
    // If inner rings are set to null, return early.
    if (inner == null) {
      return new AutoValue_Polygon(TYPE, bbox, coordinates);
    }
    for (LineString lineString : inner) {
      isLinearRing(lineString);
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_Polygon(TYPE, bbox, coordinates);
  }

  /**
   * Convenience method to get the outer {@link LineString} which defines the outer perimeter of
   * the polygon.
   *
   * @return a {@link LineString} defining the outer perimeter of this polygon
   * @since 3.0.0
   */
  @Nullable
  public LineString outer() {
    return LineString.fromLngLats(coordinates().get(0));
  }

  /**
   * Convenience method to get a list of inner {@link LineString}s defining holes inside the
   * polygon. It is not guaranteed that this instance of Polygon contains holes and thus, might
   * return a null or empty list.
   *
   * @return a List of {@link LineString}s defining holes inside the polygon
   * @since 3.0.0
   */
  @Nullable
  public List<LineString> inner() {
    List<List<Point>> coordinates = coordinates();
    if (coordinates.size() <= 1) {
      return new ArrayList(0);
    }
    List<LineString> inner = new ArrayList<>(coordinates.size() - 1);
    for (List<Point> points : coordinates.subList(1, coordinates.size())) {
      inner.add(LineString.fromLngLats(points));
    }
    return inner;
  }

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link Polygon}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code Polygon}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public abstract String type();

  /**
   * A Feature Collection might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link Feature}s. The value of the bbox member MUST be a list of
   * size 2*n where n is the number of dimensions represented in the contained feature geometries,
   * with all axes of the most southwesterly point followed by all axes of the more northeasterly
   * point. The axes order of a bbox follows the axes order of geometries.
   *
   * @return a list of double coordinate values describing a bounding box
   * @since 3.0.0
   */
  @Nullable
  @Override
  public abstract BoundingBox bbox();

  /**
   * Provides the list of {@link Point}s that make up the Polygon geometry. The first list holds the
   * different LineStrings, first being the outer ring and the following entries being inner holes
   * (if they exist).
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public abstract List<List<Point>> coordinates();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Polygon geometry
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    return gson.create().toJson(this);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<Polygon> typeAdapter(Gson gson) {
    return new AutoValue_Polygon.GsonTypeAdapter(gson);
  }

  /**
   * Checks to ensure that the LineStrings defining the polygon correctly and adhering to the linear
   * ring rules.
   *
   * @param lineString {@link LineString} the polygon geometry
   * @return true if number of coordinates are 4 or more, and first and last coordinates
   *   are identical, else false
   * @throws GeoJsonException if number of coordinates are less than 4,
   *   or first and last coordinates are not identical
   * @since 3.0.0
   */
  private static boolean isLinearRing(LineString lineString) {
    if (lineString.coordinates().size() < 4) {
      throw new GeoJsonException("LinearRings need to be made up of 4 or more coordinates.");
    }
    if (!(lineString.coordinates().get(0).equals(
      lineString.coordinates().get(lineString.coordinates().size() - 1)))) {
      throw new GeoJsonException("LinearRings require first and last coordinate to be identical.");
    }
    return true;
  }
}
