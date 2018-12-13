package com.mapbox.geojson;

import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MAX_LONGITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LATITUDE;
import static com.mapbox.geojson.constants.GeoJsonConstants.MIN_LONGITUDE;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.CoordinateTypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.GeometryTypeAdapter;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;
import com.mapbox.geojson.shifter.CoordinateShifterManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
public final class Point implements CoordinateContainer<List<Double>>, Serializable {

  private static final String TYPE = "Point";

  private final String type;

  private final BoundingBox bbox;

  private final List<Double> coordinates;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a Point object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLat(double, double)}. Longitude values should not exceed
   * the spec defined -180 to 180 range and latitude's limit of -90 to 90. While no limit is placed
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
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(new TypeToken<List<Double>>(){}.getType(),
            new CoordinateTypeAdapter());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    return gson.create().fromJson(json, Point.class);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude) {

    List<Double> coordinates =
      CoordinateShifterManager.getCoordinateShifter().shiftLonLat(longitude, latitude);
    return new Point(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    @Nullable BoundingBox bbox) {

    List<Double> coordinates =
      CoordinateShifterManager.getCoordinateShifter().shiftLonLat(longitude, latitude);
    return new Point(TYPE, bbox, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @param altitude  a double value which can be negative or positive infinity representing either
   *                  elevation or altitude
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    double altitude) {

    List<Double> coordinates =
      CoordinateShifterManager.getCoordinateShifter().shiftLonLatAlt(longitude, latitude, altitude);

    return new Point(TYPE, null, coordinates);
  }

  /**
   * Create a new instance of this class defining a longitude and latitude value in that respective
   * order. Longitude values are limited to a -180 to 180 range and latitude's limited to -90 to 90
   * as the spec defines. While no limit is placed on decimal precision, for performance reasons
   * when serializing and deserializing it is suggested to limit decimal precision to within 6
   * decimal places. An optional altitude value can be passed in and can vary between negative
   * infinity and positive infinity.
   *
   * @param longitude a double value between -180 to 180 representing the x position of this point
   * @param latitude  a double value between -90 to 90 representing the y position of this point
   * @param altitude  a double value which can be negative or positive infinity representing either
   *                  elevation or altitude
   * @param bbox      optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.0.0
   */
  public static Point fromLngLat(
    @FloatRange(from = MIN_LONGITUDE, to = MAX_LONGITUDE) double longitude,
    @FloatRange(from = MIN_LATITUDE, to = MAX_LATITUDE) double latitude,
    double altitude, @Nullable BoundingBox bbox) {

    List<Double> coordinates =
      CoordinateShifterManager.getCoordinateShifter().shiftLonLatAlt(longitude, latitude, altitude);
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

  Point(String type, @Nullable BoundingBox bbox, List<Double> coordinates) {
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
   * This returns a double value ranging from -180 to 180 representing the x or easting position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -180 to 180 representing the x or easting position of this
   *   point
   * @since 3.0.0
   */
  public double longitude() {
    return coordinates().get(0);
  }

  /**
   * This returns a double value ranging from -90 to 90 representing the y or northing position of
   * this point. ideally, this value would be restricted to 6 decimal places to correctly follow the
   * GeoJson spec.
   *
   * @return a double value ranging from -90 to 90 representing the y or northing position of this
   *   point
   * @since 3.0.0
   */
  public double latitude() {
    return coordinates().get(1);
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
    if (coordinates().size() < 3) {
      return Double.NaN;
    }
    return coordinates().get(2);
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
   * Provide a single double array containing the longitude, latitude, and optionally an
   * altitude/elevation. {@link #longitude()}, {@link #latitude()}, and {@link #altitude()} are all
   * avaliable which make getting specific coordinates more direct.
   *
   * @return a double array which holds this points coordinates
   * @since 3.0.0
   */
  @NonNull
  @Override
  public List<Double> coordinates()  {
    return coordinates;
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
    gson.registerTypeAdapter(Geometry.class, new GeometryTypeAdapter());
    gson.registerTypeAdapter(new TypeToken<List<Double>>(){}.getType(),
            new CoordinateTypeAdapter());
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
  public static TypeAdapter<Point> typeAdapter(Gson gson) {
    return new Point.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
//    return "Point{"
//            + "type=" + type + ", "
//            + "bbox=" + bbox + ", "
//            + "coordinates=" + coordinates
//            + "}";
      return "" + coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Point) {
      Point that = (Point) o;
      return (this.type.equals(that.type()))
              && ((this.bbox == null) ? (that.bbox() == null) : this.bbox.equals(that.bbox()))
              && (this.coordinates.equals(that.coordinates()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= type.hashCode();
    h$ *= 1000003;
    h$ ^= (bbox == null) ? 0 : bbox.hashCode();
    h$ *= 1000003;
    h$ ^= coordinates.hashCode();
    return h$;
  }

  public static final class GsonTypeAdapter extends TypeAdapter<Point> {
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<BoundingBox> boundingBox_adapter;
    private volatile TypeAdapter<List<Double>> list__double_adapter;
    private final Gson gson;
    public GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, Point object) throws IOException {
      if (object == null) {
        jsonWriter.nullValue();
        return;
      }
      jsonWriter.beginObject();
      jsonWriter.name("type");
      if (object.type() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<String> string_adapter = this.string_adapter;
        if (string_adapter == null) {
          this.string_adapter = string_adapter = gson.getAdapter(String.class);
        }
        string_adapter.write(jsonWriter, object.type());
      }
      jsonWriter.name("bbox");
      if (object.bbox() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<BoundingBox> boundingBox_adapter = this.boundingBox_adapter;
        if (boundingBox_adapter == null) {
          this.boundingBox_adapter = boundingBox_adapter = gson.getAdapter(BoundingBox.class);
        }
        boundingBox_adapter.write(jsonWriter, object.bbox());
      }
      jsonWriter.name("coordinates");
      if (object.coordinates() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<Double>> list__double_adapter = this.list__double_adapter;
        if (list__double_adapter == null) {
          this.list__double_adapter = list__double_adapter =
            (TypeAdapter<List<Double>>) gson.getAdapter(TypeToken.getParameterized(List.class, Double.class));
        }
        list__double_adapter.write(jsonWriter, object.coordinates());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public Point read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      List<Double> coordinates = null;
      while (jsonReader.hasNext()) {
        String _name = jsonReader.nextName();
        if (jsonReader.peek() == JsonToken.NULL) {
          jsonReader.nextNull();
          continue;
        }
        switch (_name) {
          case "type": {
            TypeAdapter<String> string_adapter = this.string_adapter;
            if (string_adapter == null) {
              this.string_adapter = string_adapter = gson.getAdapter(String.class);
            }
            type = string_adapter.read(jsonReader);
            break;
          }
          case "bbox": {
            TypeAdapter<BoundingBox> boundingBox_adapter = this.boundingBox_adapter;
            if (boundingBox_adapter == null) {
              this.boundingBox_adapter = boundingBox_adapter = gson.getAdapter(BoundingBox.class);
            }
            bbox = boundingBox_adapter.read(jsonReader);
            break;
          }
          case "coordinates": {
            TypeAdapter<List<Double>> list__double_adapter = this.list__double_adapter;
            if (list__double_adapter == null) {
              this.list__double_adapter = list__double_adapter =
                      (TypeAdapter<List<Double>>) gson.getAdapter(TypeToken.getParameterized(List.class, Double.class));
            }
            coordinates = list__double_adapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new Point(type == null ? "Point" : type, bbox, coordinates);
    }
  }
}
