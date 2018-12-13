package com.mapbox.geojson;

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
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.IOException;
import java.io.Serializable;
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
public final class MultiLineString
  implements CoordinateContainer<List<List<Point>>>, Serializable {

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
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
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
    gson.registerTypeAdapter(Point.class, new PointSerializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxSerializer());
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
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof MultiLineString) {
      MultiLineString that = (MultiLineString) o;
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

  public static final class GsonTypeAdapter extends TypeAdapter<MultiLineString> {
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<BoundingBox> boundingBox_adapter;
    private volatile TypeAdapter<List<List<Point>>> list__list__point_adapter;
    private final Gson gson;
    public GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, MultiLineString object) throws IOException {
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
        TypeAdapter<List<List<Point>>> list__list__point_adapter = this.list__list__point_adapter;
        if (list__list__point_adapter == null) {
          this.list__list__point_adapter = list__list__point_adapter = (TypeAdapter<List<List<Point>>>) gson.getAdapter(TypeToken.getParameterized(List.class, TypeToken.getParameterized(List.class, Point.class).getType()));
        }
        list__list__point_adapter.write(jsonWriter, object.coordinates());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public MultiLineString read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      List<List<Point>> coordinates = null;
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
            TypeAdapter<List<List<Point>>> list__list__point_adapter = this.list__list__point_adapter;
            if (list__list__point_adapter == null) {
              this.list__list__point_adapter = list__list__point_adapter = (TypeAdapter<List<List<Point>>>) gson.getAdapter(TypeToken.getParameterized(List.class, TypeToken.getParameterized(List.class, Point.class).getType()));
            }
            coordinates = list__list__point_adapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new MultiLineString(type, bbox, coordinates);
    }
  }
}
