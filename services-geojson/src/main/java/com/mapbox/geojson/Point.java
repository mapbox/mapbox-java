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
import com.mapbox.geojson.shifter.CoordinateShifterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A point represents a single geographic position and is one of the seven Geometries found in the
 * GeoJson spec.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods.
 * </p><p>
 * Coordinates are in x, y order (easting, northing for projected coordinates), longitude, and
 * latitude for geographic coordinates), precisely in that order and using double values. Altitude
 * or elevation MAY be included as an optional third parameter while creating this object.
 * <p>
 * The size of a GeoJson text in bytes is a major interoperability consideration, and precision of
 * coordinate values has a large impact on the size of texts when serialized. For geographic
 * coordinates with units of degrees, 6 decimal places (a default common in, e.g., sprintf) amounts
 * to about 10 centimeters, a precision well within that of current GPS systems. Implementations
 * should consider the cost of using a greater precision than necessary.
 * <p>
 * Furthermore, pertaining to altitude, the WGS 84 datum is a relatively coarse approximation of the
 * geoid, with the height varying by up to 5 m (but generally between 2 and 3 meters) higher or
 * lower relative to a surface parallel to Earth's mean sea level.
 * <p>
 * A sample GeoJson Point's provided below (in its serialized state).
 * <pre>
 * {
 *   "type": "Point",
 *   "coordinates": [100.0, 0.0]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
@Keep
public final class Point implements FlattenedCoordinateContainer<List<Double>, double[]> {

  static final String TYPE = "Point";

  @NonNull
  private final String type;

  @Nullable
  private final BoundingBox bbox;

  private final double longitude;
  private final double latitude;
  private final double altitude;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Point object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLat(double, double)}. While no limit is placed
   * on decimal precision, for performance reasons when serializing and deserializing it is
   * suggested to limit decimal precision to within 6 decimal places.
   *
   * @param json a formatted valid JSON string defining a GeoJson Point
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static Point fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    return gson.create().fromJson(json, Point.class);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places.
   *
   * @param longitude a double value representing the x position of this point
   * @param latitude  a double value representing the y position of this point
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(double longitude, double latitude) {

    double[] coordinates =
      CoordinateShifterManager.getCoordinateShifter().shift(longitude, latitude);
    return new Point(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value representing the x position of this point
   * @param latitude  a double value representing the y position of this point
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(double longitude, double latitude,
    @Nullable BoundingBox bbox) {

    double[] coordinates =
      CoordinateShifterManager.getCoordinateShifter().shift(longitude, latitude);
    return new Point(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value representing the x position of this point
   * @param latitude  a double value representing the y position of this point
   * @param altitude  a double value which can be negative or positive infinity representing either
   *                  elevation or altitude
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(double longitude, double latitude, double altitude) {

    double[] coordinates =
      CoordinateShifterManager.getCoordinateShifter().shift(longitude, latitude, altitude);

    return new Point(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value representing the x position of this point
   * @param latitude  a double value representing the y position of this point
   * @param altitude  a double value which can be negative or positive infinity representing either
   *                  elevation or altitude
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(double longitude, double latitude,
    double altitude, @Nullable BoundingBox bbox) {

    double[] coordinates =
      CoordinateShifterManager.getCoordinateShifter().shift(longitude, latitude, altitude);
    return new Point(TYPE, bbox, coordinates);
  }

  static Point fromLngLat(@NonNull double[] coords) {
    if (coords.length == 2) {
      return Point.fromLngLat(coords[0], coords[1]);
    } else if (coords.length > 2) {
      return  Point.fromLngLat(coords[0], coords[1], coords[2]);
    }
    return null;
  }

  Point(@NonNull String type, @Nullable BoundingBox bbox, @NonNull double[] coordinates) {
    this.type = type;
    this.bbox = bbox;
    if (coordinates.length == 0) {
      throw new NullPointerException("Null coordinates");
    }
    longitude = coordinates[0];
    latitude = coordinates[1];
    altitude = coordinates.length > 2 ? coordinates[2] : Double.NaN;
  }

  /**
   * This returns a double value representing the x or easting position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value representing the x or easting position of this
   *   point
   * @since 3.0.0
   */
  public double longitude() {
    return longitude;
  }

  /**
   * This returns a double value representing the y or northing position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value representing the y or northing position of this
   *   point
   * @since 3.0.0
   */
  public double latitude() {
    return latitude;
  }

  /**
   * Optionally, the coordinate spec in GeoJson allows for altitude values to be placed inside the
   * coordinate array. {@link #hasAltitude()} can be used to determine if this value was set during
   * initialization of this Point instance. This double value should only be used to represent
   * either the elevation or altitude value at this particular point.
   *
   * @return a double value ranging from negative to positive infinity
   * @since 3.0.0
   */
  public double altitude() {
    return altitude;
  }

  /**
   * Optionally, the coordinate spec in GeoJson allows for altitude values to be placed inside the
   * coordinate array. If an altitude value was provided while initializing this instance, this will
   * return true.
   *
   * @return true if this instance of point contains an altitude value
   * @since 3.0.0
   */
  public boolean hasAltitude() {
    return !Double.isNaN(altitude());
  }

  /**
   * This describes the TYPE of GeoJson geometry this object is, thus this will always return
   * {@link Point}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code Point}
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
   * Provide a list of Doubles containing the longitude, latitude, and optionally an
   * altitude/elevation. {@link #longitude()}, {@link #latitude()}, and {@link #altitude()} are all
   * available which make getting specific coordinates more direct.
   *
   * @return a double array which holds this points coordinates
   * @since 3.0.0
   * @deprecated Please use {@link #flattenCoordinates()} instead.
   */
  @NonNull
  @Override
  @Deprecated
  public List<Double> coordinates()  {
    int size = 2;
    if (!Double.isNaN(altitude())) {
      size++;
    }
    ArrayList<Double> list = new ArrayList<>(size);
    list.add(longitude);
    list.add(latitude);
    if (!Double.isNaN(altitude())) {
      list.add(altitude);
    }
    return list;
  }

  /**
   * Provide a single double array containing the longitude, latitude, and optionally an
   * altitude. {@link #longitude()}, {@link #latitude()}, and {@link #altitude()} are all
   * available which make getting specific coordinates more direct.
   *
   * @return a double array which holds this points coordinates
   * @since 3.0.0
   */
  @Override
  public double[] flattenCoordinates() {
    if (Double.isNaN(altitude)) {
      return new double[]{longitude, latitude};
    } else {
      return new double[]{longitude, latitude, altitude};
    }
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Point geometry
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
  public static TypeAdapter<Point> typeAdapter(Gson gson) {
    return new Point.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    String coordinatesStr;
    if (Double.isNaN(altitude)) {
      coordinatesStr = "[" + longitude + ", " + latitude + "]";
    } else {
      coordinatesStr = "["
              + longitude + ", "
              + latitude + ", "
              + altitude
              + "]";
    }
    return "Point{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "coordinates=" + coordinatesStr
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Point)) {
      return false;
    }
    Point point = (Point) o;
    return Double.compare(longitude, point.longitude) == 0
            && Double.compare(latitude, point.latitude) == 0
            && Double.compare(altitude, point.altitude) == 0
            && Objects.equals(type, point.type)
            && Objects.equals(bbox, point.bbox);
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + Objects.hashCode(bbox);
    result = 31 * result + Double.hashCode(longitude);
    result = 31 * result + Double.hashCode(latitude);
    result = 31 * result + Double.hashCode(altitude);
    return result;
  }

  /**
   * TypeAdapter for Point geometry.
   *
   * @since 4.6.0
   */
  static final class GsonTypeAdapter extends
          BaseGeometryTypeAdapter<Point, List<Double>, double[]> {

    GsonTypeAdapter(Gson gson) {
      super(gson, new ListOfDoublesCoordinatesTypeAdapter());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, Point object) throws IOException {
      writeFlattenedCoordinateContainer(jsonWriter, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Point read(JsonReader jsonReader) throws IOException {
      return (Point)readCoordinateContainer(jsonReader);
    }

    @Override
    CoordinateContainer<List<Double>> createCoordinateContainer(String type,
                                                                BoundingBox bbox,
                                                                double[] coordinates) {
      return new Point(type == null ? "Point" : type, bbox, coordinates);
    }
  }
}
