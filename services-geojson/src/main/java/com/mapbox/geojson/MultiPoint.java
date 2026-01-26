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
import java.util.List;
import java.util.Objects;

/**
 * A MultiPoint represents two or more geographic points that share a relationship and is one of the
 * seven geometries found in the GeoJson spec. <p> This adheres to the RFC 7946 internet standard
 * when serialized into JSON. When deserialized, this class becomes an immutable object which should
 * be initiated using its static factory methods. The list of points must be equal to or greater
 * than 2. </p><p> A sample GeoJson MultiPoint's provided below (in it's serialized state).
 * <pre>
 * {
 *   "TYPE": "MultiPoint",
 *   "coordinates": [
 *     [100.0, 0.0],
 *     [101.0, 1.0]
 *   ]
 * }
 * </pre>
 * Look over the {@link Point} documentation to get more
 * information about formatting your list of point objects correctly.
 *
 * @since 1.0.0
 */
@Keep
public final class MultiPoint implements
        FlattenedCoordinateContainer<List<Point>, FlattenListOfPoints> {

  private static final String TYPE = "MultiPoint";

  private final String type;

  private final BoundingBox bbox;

  @NonNull
  private final FlattenListOfPoints flattenListOfPoints;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a MultiPoint object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLats(List)}. For a valid MultiPoint to exist, it must
   * have at least 2 coordinate entries.
   *
   * @param json a formatted valid JSON string defining a GeoJson MultiPoint
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static MultiPoint fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    return gson.create().fromJson(json, MultiPoint.class);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Point}s which follow the
   * correct specifications described in the Point documentation. Note that there should not be any
   * duplicate points inside the list. <p> Note that if less than 2 points are passed in, a runtime
   * exception will occur. </p>
   *
   * @param points a list of {@link Point}s which make up the LineString geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPoint fromLngLats(@NonNull List<Point> points) {
    return new MultiPoint(TYPE, null, points);
  }

  /**
   * Create a new instance of this class by defining a list of {@link Point}s which follow the
   * correct specifications described in the Point documentation. Note that there should not be any
   * duplicate points inside the list. <p> Note that if less than 2 points are passed in, a runtime
   * exception will occur. </p>
   *
   * @param points a list of {@link Point}s which make up the LineString geometry
   * @param bbox   optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static MultiPoint fromLngLats(@NonNull List<Point> points, @Nullable BoundingBox bbox) {
    return new MultiPoint(TYPE, bbox, points);
  }

  static MultiPoint fromLngLats(@NonNull double[][] coordinates) {
    ArrayList<Point> converted = new ArrayList<>(coordinates.length);
    for (int i = 0; i < coordinates.length; i++) {
      converted.add(Point.fromLngLat(coordinates[i]));
    }

    return new MultiPoint(TYPE, null, converted);
  }

  MultiPoint(String type, @Nullable BoundingBox bbox, List<Point> coordinates) {
    this(type, bbox, new FlattenListOfPoints(coordinates));
  }

  MultiPoint(String type, @Nullable BoundingBox bbox, FlattenListOfPoints flattenListOfPoints) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    if (flattenListOfPoints == null) {
      throw new NullPointerException("Null coordinates");
    }
    this.flattenListOfPoints = flattenListOfPoints;
  }

  /**
   * This describes the TYPE of GeoJson this object is, thus this will always return {@link
   * MultiPoint}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code MultiPoint}
   * @since 1.0.0
   */
  @NonNull
  @Override
  public String type() {
    return type;
  }

  /**
   * A Feature Collection might have a member named {@code bbox} to include information on the
   * coordinate range for it's {@link Feature}s. The value of the bbox member MUST be a list of size
   * 2*n where n is the number of dimensions represented in the contained feature geometries, with
   * all axes of the most southwesterly point followed by all axes of the more northeasterly point.
   * The axes order of a bbox follows the axes order of geometries.
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
   * provides the list of {@link Point}s that make up the MultiPoint geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  @NonNull
  @Override
  public List<Point> coordinates() {
    return flattenListOfPoints.points();
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this MultiPoint geometry
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
  public static TypeAdapter<MultiPoint> typeAdapter(Gson gson) {
    return new MultiPoint.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "MultiPoint{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "coordinates=" + flattenListOfPoints.points()
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MultiPoint)) {
      return false;
    }
    MultiPoint that = (MultiPoint) o;
    return Objects.equals(type, that.type)
            && Objects.equals(bbox, that.bbox)
            && Objects.equals(flattenListOfPoints, that.flattenListOfPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, bbox, flattenListOfPoints);
  }

  @Override
  public FlattenListOfPoints flattenCoordinates() {
    return flattenListOfPoints;
  }

  /**
   * TypeAdapter for MultiPoint geometry.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter extends
          BaseGeometryTypeAdapter<MultiPoint, List<Point>, FlattenListOfPoints> {

    GsonTypeAdapter(Gson gson) {
      super(gson, new FlattenListOfPointsTypeAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, MultiPoint object) throws IOException {
      writeCoordinateContainerPrimitive(jsonWriter, object);
    }

    @Override
    public MultiPoint read(JsonReader jsonReader) throws IOException {
      return (MultiPoint) readCoordinateContainer(jsonReader);
    }

    @Override
    CoordinateContainer<List<Point>> createCoordinateContainer(
            String type,
            BoundingBox bbox,
            FlattenListOfPoints flattenListOfPoints) {
      return new MultiPoint(type == null ? "MultiPoint" : type, bbox, flattenListOfPoints);
    }
  }
}
