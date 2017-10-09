package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A multPolygon is an array of Polygon coordinate arrays.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods.
 * </p><p>
 * When representing a Polygon that crosses the antimeridian, interoperability is improved by
 * modifying their geometry. Any geometry that crosses the antimeridian SHOULD be represented by
 * cutting it in two such that neither part's representation crosses the antimeridian.
 * </p><p>
 * For example, a line extending from 45 degrees N, 170 degrees E across the antimeridian to 45
 * degrees N, 170 degrees W should be cut in two and represented as a MultiLineString.
 * </p><p>
 * A sample GeoJson MultiPolygon's provided below (in it's serialized state).
 * <pre>
 * {
 *   "type": "MultiPolygon",
 *   "coordinates": [
 *     [
 *       [
 *         [102.0, 2.0],
 *         [103.0, 2.0],
 *         [103.0, 3.0],
 *         [102.0, 3.0],
 *         [102.0, 2.0]
 *       ]
 *     ],
 *     [
 *       [
 *         [100.0, 0.0],
 *         [101.0, 0.0],
 *         [101.0, 1.0],
 *         [100.0, 1.0],
 *         [100.0, 0.0]
 *       ],
 *       [
 *         [100.2, 0.2],
 *         [100.2, 0.8],
 *         [100.8, 0.8],
 *         [100.8, 0.2],
 *         [100.2, 0.2]
 *       ]
 *     ]
 *   ]
 * }
 * </pre>
 * Look over the {@link com.mapbox.geojson.Polygon} documentation to get more information about
 * formatting your list of Polygon objects correctly.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class MultiPolygon implements Geometry<List<List<List<Point>>>>, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "MultiPolygon";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a MultiPolygon object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromPolygons(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson MultiPolygon
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static MultiPolygon fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, MultiPolygon.class);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Polygon} objects and passing
   * that list in as a parameter in this method. The Polygons should comply with the GeoJson
   * specifications described in the documentation.
   *
   * @param polygons a list of Polygons which make up this MultiPolygon
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons) {
    List<List<List<Point>>> coordinates = new ArrayList<>();
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new AutoValue_MultiPolygon(null, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Polygon} objects and passing
   * that list in as a parameter in this method. The Polygons should comply with the GeoJson
   * specifications described in the documentation. Optionally, pass in an instance of a
   * {@link BoundingBox} which better describes this MultiPolygon.
   *
   * @param polygons a list of Polygons which make up this MultiPolygon
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons,
                                          @Nullable BoundingBox bbox) {
    List<List<List<Point>>> coordinates = new ArrayList<>();
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new AutoValue_MultiPolygon(bbox, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of a list of a list of {@link Point}s
   * which follow the correct specifications described in the Point documentation.
   *
   * @param points a list of {@link Point}s which make up the MultiPolygon geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> points) {
    return new AutoValue_MultiPolygon(null, points);
  }

  /**
   * Create a new instance of this class by defining a list of a list of a list of {@link Point}s
   * which follow the correct specifications described in the Point documentation.
   *
   * @param points a list of {@link Point}s which make up the MultiPolygon geometry
   * @param bbox   optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> points,
                                         @Nullable BoundingBox bbox) {

    return new AutoValue_MultiPolygon(bbox, points);
  }

  /**
   * Returns a list of polygons which make up this MultiPolygon instance.
   *
   * @return a list of {@link Polygon}s which make up this MultiPolygon instance
   * @since 3.0.0
   */
  public List<Polygon> polygons() {
    List<Polygon> polygons = new ArrayList<>();
    for (List<List<Point>> points : coordinates()) {
      polygons.add(Polygon.fromLngLats(points));
    }
    return polygons;
  }

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link MultiPolygon}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code MultiPolygon}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public String type() {
    return TYPE;
  }

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
   * Provides the list of list of list of {@link Point}s that make up the MultiPolygon geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public abstract List<List<List<Point>>> coordinates();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this MultiPolygon geometry
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    gson.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    return gson.create().toJson(this);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MultiPolygon> typeAdapter(Gson gson) {
    return new AutoValue_MultiPolygon.GsonTypeAdapter(gson);
  }
}
