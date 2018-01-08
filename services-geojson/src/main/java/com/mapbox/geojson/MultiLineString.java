package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
import java.util.ArrayList;
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
@AutoValue
public abstract class MultiLineString implements Geometry<List<List<Point>>>, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "MultiLineString";

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
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
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
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(null, coordinates);
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
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(lineString.coordinates());
    return new AutoValue_MultiLineString(null, coordinates);
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
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(bbox, coordinates);
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
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(lineString.coordinates());
    return new AutoValue_MultiLineString(bbox, coordinates);
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
    return new AutoValue_MultiLineString(null, points);
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
    return new AutoValue_MultiLineString(bbox, points);
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
   * Provides the list of list of {@link Point}s that make up the MultiLineString geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public abstract List<List<Point>> coordinates();

  /**
   * Returns a list of LineStrings which are currently making up this MultiLineString.
   *
   * @return a list of {@link LineString}s
   * @since 3.0.0
   */
  public List<LineString> lineStrings() {
    List<LineString> lineStrings = new ArrayList<>();
    for (List<Point> points : coordinates()) {
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
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
    gson.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
    gson.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
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
    return new AutoValue_MultiLineString.GsonTypeAdapter(gson);
  }
}
