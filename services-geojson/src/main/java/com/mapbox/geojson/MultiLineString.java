package com.mapbox.geojson;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A multilinestring is an array of LineString coordinate arrays.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods.
 * </p><p>
 * When representing a LineString that crosses the antimeridian, interoperability is improved by
 * modifying their geometry. Any geometry that crosses the antimeridian SHOULD be represented by
 * cutting it in two such that neither part's representation crosses the antimeridian.
 * </p><p>
 * For example, a line extending from 45 degrees N, 170 degrees E across the antimeridian to 45
 * degrees N, 170 degrees W should be cut in two and represented as a MultiLineString.
 * </p><p>
 * A sample GeoJson MultiLineString's provided below (in it's serialized state).
 * <pre>
 * {
 *   "type": "MultiLineString",
 *   "coordinates": [
 *     [
 *       [100.0, 0.0],
 *       [101.0, 1.0]
 *     ],
 *     [
 *       [102.0, 2.0],
 *       [103.0, 3.0]
 *     ]
 *   ]
 * }
 * </pre>
 * Look over the {@link LineString} documentation to get more information about
 * formatting your list of linestring objects correctly.
 *
 * @since 1.0.0
 */
@Keep
public final class MultiLineString
  implements CoordinateContainer<List<List<Point>>> {

  private static final String TYPE = "MultiLineString";

  private final String type;

  private final BoundingBox bbox;

  private final List<List<Point>> coordinates;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a MultiLineString object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromLineStrings(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson MultiLineString
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static MultiLineString fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    return gson.create().fromJson(json, MultiLineString.class);
  }

  /**
   * Create a new instance of this class by defining a list of {@link LineString} objects and
   * passing that list in as a parameter in this method. The LineStrings should comply with the
   * GeoJson specifications described in the documentation.
   *
   * @param lineStrings a list of LineStrings which make up this MultiLineString
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings) {
    List<List<Point>> coordinates = new ArrayList<>(lineStrings.size());
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new MultiLineString(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of {@link LineString} objects and
   * passing that list in as a parameter in this method. The LineStrings should comply with the
   * GeoJson specifications described in the documentation. Optionally, pass in an instance of a
   * {@link BoundingBox} which better describes this MultiLineString.
   *
   * @param lineStrings a list of LineStrings which make up this MultiLineString
   * @param bbox        optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings,
                                                @Nullable BoundingBox bbox) {
    List<List<Point>> coordinates = new ArrayList<>(lineStrings.size());
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new MultiLineString(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class by passing in a single {@link LineString} object. The
   * LineStrings should comply with the GeoJson specifications described in the documentation.
   *
   * @param lineString a single LineString which make up this MultiLineString
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLineString(@NonNull LineString lineString) {
    List<List<Point>> coordinates = Arrays.asList(lineString.coordinates());
    return new MultiLineString(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class by passing in a single {@link LineString} object. The
   * LineStrings should comply with the GeoJson specifications described in the documentation.
   *
   * @param lineString a single LineString which make up this MultiLineString
   * @param bbox       optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLineString(@NonNull LineString lineString,
                                               @Nullable BoundingBox bbox) {
    List<List<Point>> coordinates = Arrays.asList(lineString.coordinates());
    return new MultiLineString(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class by defining a list of a list of {@link Point}s which follow
   * the correct specifications described in the Point documentation. Note that there should not be
   * any duplicate points inside the list and the points combined should create a LineString with a
   * distance greater than 0.
   *
   * @param points a list of {@link Point}s which make up the MultiLineString geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLngLats(@NonNull List<List<Point>> points) {
    return new MultiLineString(TYPE, null, points);
  }

  /**
   * Create a new instance of this class by defining a list of a list of {@link Point}s which follow
   * the correct specifications described in the Point documentation. Note that there should not be
   * any duplicate points inside the list and the points combined should create a LineString with a
   * distance greater than 0.
   *
   * @param points a list of {@link Point}s which make up the MultiLineString geometry
   * @param bbox   optionally include a bbox definition
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiLineString fromLngLats(@NonNull List<List<Point>> points,
                                            @Nullable BoundingBox bbox) {
    return new MultiLineString(TYPE, bbox, points);
  }

  static MultiLineString fromLngLats(double[][][] coordinates) {
    List<List<Point>> multiLine = new ArrayList<>(coordinates.length);
    for (int i = 0; i < coordinates.length; i++) {
      List<Point> lineString = new ArrayList<>(coordinates[i].length);
      for (int j = 0; j < coordinates[i].length; j++) {
        lineString.add(Point.fromLngLat(coordinates[i][j]));
      }
      multiLine.add(lineString);
    }

    return new MultiLineString(TYPE, null, multiLine);
  }

  MultiLineString(String type, @Nullable BoundingBox bbox, List<List<Point>> coordinates) {
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
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link MultiLineString}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code MultiLineString}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public String type() {
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
  public BoundingBox bbox() {
    return bbox;
  }

  /**
   * Provides the list of list of {@link Point}s that make up the MultiLineString geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public List<List<Point>> coordinates() {
    return coordinates;
  }

  /**
   * Returns a list of LineStrings which are currently making up this MultiLineString.
   *
   * @return a list of {@link LineString}s
   * @since 3.0.0
   */
  public List<LineString> lineStrings() {
    List<List<Point>> coordinates = coordinates();
    List<LineString> lineStrings = new ArrayList<>(coordinates.size());
    for (List<Point> points : coordinates) {
      lineStrings.add(LineString.fromLngLats(points));
    }
    return lineStrings;
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this MultiLineString geometry
   * @since 1.0.0
   */
  @Override
  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    return gson.create().toJson(this);
  }

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MultiLineString> typeAdapter(Gson gson) {
    return new MultiLineString.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "MultiLineString{"
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
    if (obj instanceof MultiLineString) {
      MultiLineString that = (MultiLineString) obj;
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
   * TypeAdapter for MultiLineString geometry.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter
          extends BaseGeometryTypeAdapter<MultiLineString, List<List<Point>>> {

    GsonTypeAdapter(Gson gson) {
      super(gson, new ListOfListOfPointCoordinatesTypeAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, MultiLineString object) throws IOException {
      writeCoordinateContainer(jsonWriter, object);
    }

    @Override
    public MultiLineString read(JsonReader jsonReader) throws IOException {
      return (MultiLineString) readCoordinateContainer(jsonReader);
    }

    @Override
    CoordinateContainer<List<List<Point>>> createCoordinateContainer(String type,
                                                                     BoundingBox bbox,
                                                                     List<List<Point>> coords) {
      return new MultiLineString(type == null ? "MultiLineString" : type, bbox, coords);
    }
  }
}
