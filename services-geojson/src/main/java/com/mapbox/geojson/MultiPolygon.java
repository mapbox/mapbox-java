package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A multiPolygon is an array of Polygon coordinate arrays.
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
 * Look over the {@link Polygon} documentation to get more information about
 * formatting your list of Polygon objects correctly.
 *
 * @since 1.0.0
 */
@Keep
public final class MultiPolygon
  implements CoordinateContainer<List<List<List<Point>>>>, Serializable {

  private static final String TYPE = "MultiPolygon";

  private final String type;

  private final BoundingBox bbox;

  private final List<List<List<Point>>> coordinates;

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
    List<List<List<Point>>> coordinates = new ArrayList<>(polygons.size());
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new MultiPolygon(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Polygon} objects and passing
   * that list in as a parameter in this method. The Polygons should comply with the GeoJson
   * specifications described in the documentation. Optionally, pass in an instance of a
   * {@link BoundingBox} which better describes this MultiPolygon.
   *
   * @param polygons a list of Polygons which make up this MultiPolygon
   * @param bbox     optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons,
                                          @Nullable BoundingBox bbox) {
    List<List<List<Point>>> coordinates = new ArrayList<>(polygons.size());
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new MultiPolygon(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class by defining a single {@link Polygon} objects and passing
   * it in as a parameter in this method. The Polygon should comply with the GeoJson
   * specifications described in the documentation.
   *
   * @param polygon a single Polygon which make up this MultiPolygon
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromPolygon(@NonNull Polygon polygon) {
    List<List<List<Point>>> coordinates = Arrays.asList(polygon.coordinates());
    return new MultiPolygon(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by defining a single {@link Polygon} objects and passing
   * it in as a parameter in this method. The Polygon should comply with the GeoJson
   * specifications described in the documentation.
   *
   * @param polygon a single Polygon which make up this MultiPolygon
   * @param bbox    optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromPolygon(@NonNull Polygon polygon, @Nullable BoundingBox bbox) {
    List<List<List<Point>>> coordinates = Arrays.asList(polygon.coordinates());
    return new MultiPolygon(TYPE, bbox, coordinates);
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
    return new MultiPolygon(TYPE, null, points);
  }

  /**
   * Create a new instance of this class by defining a list of a list of a list of {@link Point}s
   * which follow the correct specifications described in the Point documentation.
   *
   * @param points a list of {@link Point}s which make up the MultiPolygon geometry
   * @param bbox   optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> points,
                                         @Nullable BoundingBox bbox) {
    return new MultiPolygon(TYPE, bbox, points);
  }

  static MultiPolygon fromLngLats(@NonNull double[][][][] coordinates) {
    List<List<List<Point>>> converted = new ArrayList<>(coordinates.length);
    for (int i = 0; i < coordinates.length; i++) {
      List<List<Point>> innerOneList = new ArrayList<>(coordinates[i].length);
      for (int j = 0; j < coordinates[i].length; j++) {
        List<Point> innerTwoList = new ArrayList<>(coordinates[i][j].length);
        for (int k = 0; k < coordinates[i][j].length; k++) {
          innerTwoList.add(Point.fromLngLat(coordinates[i][j][k]));
        }
        innerOneList.add(innerTwoList);
      }
      converted.add(innerOneList);
    }

    return new MultiPolygon(TYPE, null, converted);
  }

  MultiPolygon(String type, @Nullable BoundingBox bbox, List<List<List<Point>>> coordinates) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    if (coordinates == null) {
      throw new NullPointerException("Null coordinates");
    }
    this.coordinates = coordinates;
  }

  /**
   * Returns a list of polygons which make up this MultiPolygon instance.
   *
   * @return a list of {@link Polygon}s which make up this MultiPolygon instance
   * @since 3.0.0
   */
  public List<Polygon> polygons() {
    List<List<List<Point>>> coordinates = coordinates();
    List<Polygon> polygons = new ArrayList<>(coordinates.size());
    for (List<List<Point>> points : coordinates) {
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
  public String type()  {
    return type;
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
  public BoundingBox bbox()  {
    return bbox;
  }

  /**
   * Provides the list of list of list of {@link Point}s that make up the MultiPolygon geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public List<List<List<Point>>> coordinates()  {
    return coordinates;
  }

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
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
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
    return new MultiPolygon.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "Polygon{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "coordinates=" + coordinates
            + "}";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MultiPolygon) {
      MultiPolygon that = (MultiPolygon) obj;
      return (this.type.equals(that.type()))
              && ((this.bbox == null) ? (that.bbox() == null) : this.bbox.equals(that.bbox()))
              && (this.coordinates.equals(that.coordinates()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= type.hashCode();
    hashCode *= 1000003;
    hashCode ^= (bbox == null) ? 0 : bbox.hashCode();
    hashCode *= 1000003;
    hashCode ^= coordinates.hashCode();
    return hashCode;
  }

  /**
   * TypeAdapter for MultiPolygon geometry.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter
          extends BaseGeometryTypeAdapter<MultiPolygon, List<List<List<Point>>>> {

    GsonTypeAdapter(Gson gson) {
      super(gson, new ListofListofListOfPointCoordinatesTypeAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, MultiPolygon object) throws IOException {
      writeCoordinateContainer(jsonWriter, object);
    }

    @Override
    public MultiPolygon read(JsonReader jsonReader) throws IOException {
      return (MultiPolygon) readCoordinateContainer(jsonReader);
    }

    @Override
    CoordinateContainer<List<List<List<Point>>>>
      createCoordinateContainer(String type, BoundingBox bbox, List<List<List<Point>>> coords) {
      return new MultiPolygon(type == null ? "MultiPolygon" : type, bbox, coords);
    }
  }
}
