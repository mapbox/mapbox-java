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
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.GeometryTypeAdapter;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A GeoJson object with TYPE "GeometryCollection" is a Geometry object.
 * <p>
 * A GeometryCollection has a member with the name "geometries". The value of "geometries" is a List
 * Each element of this list is a GeoJson Geometry object. It is possible for this list to be empty.
 * <p>
 * Unlike the other geometry types, a GeometryCollection can be a heterogeneous composition of
 * smaller Geometry objects. For example, a Geometry object in the shape of a lowercase roman "i"
 * can be composed of one point and one LineString.
 * <p>
 * GeometryCollections have a different syntax from single TYPE Geometry objects (Point,
 * LineString, and Polygon) and homogeneously typed multipart Geometry objects (MultiPoint,
 * MultiLineString, and MultiPolygon) but have no different semantics.  Although a
 * GeometryCollection object has no "coordinates" member, it does have coordinates: the coordinates
 * of all its parts belong to the collection. The "geometries" member of a GeometryCollection
 * describes the parts of this composition. Implementations SHOULD NOT apply any additional
 * semantics to the "geometries" array.
 * <p>
 * To maximize interoperability, implementations SHOULD avoid nested GeometryCollections.
 * Furthermore, GeometryCollections composed of a single part or a number of parts of a single TYPE
 * SHOULD be avoided when that single part or a single object of multipart TYPE (MultiPoint,
 * MultiLineString, or MultiPolygon) could be used instead.
 * <p>
 * An example of a serialized GeometryCollections given below:
 * <pre>
 * {
 *   "TYPE": "GeometryCollection",
 *   "geometries": [{
 *     "TYPE": "Point",
 *     "coordinates": [100.0, 0.0]
 *   }, {
 *     "TYPE": "LineString",
 *     "coordinates": [
 *       [101.0, 0.0],
 *       [102.0, 1.0]
 *     ]
 *   }]
 * }
 * </pre>
 *
 * @since 1.0.0
 */
public final class GeometryCollection implements Geometry, Serializable {

  private static final String TYPE = "GeometryCollection";

  private final String type;

  private final BoundingBox bbox;

  private final List<Geometry> geometries;

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a GeometryCollection object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromGeometries(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geometry Collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *         method
   * @since 1.0.0
   */
  public static GeometryCollection fromJson(String json) {

//    final RuntimeTypeAdapterFactory<Geometry> geometryFactory = RuntimeTypeAdapterFactory
//            .of(Geometry.class, "type")
//            .registerSubtype(GeometryCollection.class, "GeometryCollection")
//            .registerSubtype(Point.class, "Point")
//            .registerSubtype(MultiPoint.class, "MultiPoint")
//            .registerSubtype(LineString.class, "LineString")
//            .registerSubtype(MultiLineString.class, "MultiLineString")
//            .registerSubtype(Polygon.class, "Polygon")
//            .registerSubtype(MultiPolygon.class, "MultiPolygon");

    GsonBuilder gson = new GsonBuilder();
//    gson.registerTypeAdapterFactory(geometryFactory);

    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());

    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());

    return gson.create().fromJson(json, GeometryCollection.class);
  }

  /**
   * Create a new instance of this class by giving the collection a list of {@link Geometry}.
   *
   * @param geometries a non-null list of geometry which makes up this collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *         method
   * @since 1.0.0
   */
  public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries) {
    return new GeometryCollection(TYPE, null, geometries);
  }

  /**
   * Create a new instance of this class by giving the collection a list of {@link Geometry}.
   *
   * @param geometries a non-null list of geometry which makes up this collection
   * @param bbox       optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *         method
   * @since 1.0.0
   */
  public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries,
                                                  @Nullable BoundingBox bbox) {
    return new GeometryCollection(TYPE, bbox, geometries);
  }

  /**
   * Create a new instance of this class by giving the collection a single GeoJSON {@link Geometry}.
   *
   * @param geometry a non-null object of type geometry which makes up this collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *         method
   * @since 3.0.0
   */
  public static GeometryCollection fromGeometry(@NonNull Geometry geometry) {
    List<Geometry> geometries = Arrays.asList(geometry);
    return new GeometryCollection(TYPE, null, geometries);
  }

  /**
   * Create a new instance of this class by giving the collection a single GeoJSON {@link Geometry}.
   *
   * @param geometry a non-null object of type geometry which makes up this collection
   * @param bbox     optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *         method
   * @since 3.0.0
   */
  public static GeometryCollection fromGeometry(@NonNull Geometry geometry,
                                                @Nullable BoundingBox bbox) {
    List<Geometry> geometries = Arrays.asList(geometry);
    return new GeometryCollection(TYPE, bbox, geometries);
  }

  GeometryCollection(String type, @Nullable BoundingBox bbox, List<Geometry> geometries) {
    if (type == null) {
      throw new NullPointerException("Null type");
    }
    this.type = type;
    this.bbox = bbox;
    if (geometries == null) {
      throw new NullPointerException("Null geometries");
    }
    this.geometries = geometries;
  }

  /**
   * This describes the TYPE of GeoJson this object is, thus this will always return
   * {@link GeometryCollection}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   * {@code GeometryCollection}
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
   * This provides the list of geometry making up this Geometry Collection. Note that if the
   * Geometry Collection was created through {@link #fromJson(String)} this list could be null.
   * Otherwise, the list can't be null but the size of the list can equal 0.
   *
   * @return a list of {@link Geometry} which make up this Geometry Collection
   * @since 1.0.0
   */
  @NonNull
  public List<Geometry> geometries() {
    return geometries;
  }

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this GeometryCollection
   * @since 1.0.0
   */
  @Override
  public String toJson() {

//    final RuntimeTypeAdapterFactory<Geometry> geometryFactory = RuntimeTypeAdapterFactory
//            .of(Geometry.class, "type")
//            .registerSubtype(GeometryCollection.class, "GeometryCollection")
//            .registerSubtype(Point.class, "Point")
//            .registerSubtype(MultiPoint.class, "MultiPoint")
//            .registerSubtype(LineString.class, "LineString")
//            .registerSubtype(MultiLineString.class, "MultiLineString")
//            .registerSubtype(Polygon.class, "Polygon")
//            .registerSubtype(MultiPolygon.class, "MultiPolygon");

    GsonBuilder gson = new GsonBuilder();
    // gson.registerTypeAdapterFactory(geometryFactory);

    gson.registerTypeAdapter(Geometry.class, new GeometryTypeAdapter());
   // gson.registerTypeAdapter(Point.class, new PointSerializer());
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
  public static TypeAdapter<GeometryCollection> typeAdapter(Gson gson) {
    return new GeometryCollection.GsonTypeAdapter(gson);
  }

  @Override
  public String toString() {
    return "GeometryCollection{"
            + "type=" + type + ", "
            + "bbox=" + bbox + ", "
            + "geometries=" + geometries
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof GeometryCollection) {
      GeometryCollection that = (GeometryCollection) o;
      return (this.type.equals(that.type()))
              && ((this.bbox == null) ? (that.bbox() == null) : this.bbox.equals(that.bbox()))
              && (this.geometries.equals(that.geometries()));
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
    h$ ^= geometries.hashCode();
    return h$;
  }

  public static final class GsonTypeAdapter extends TypeAdapter<GeometryCollection> {
    private volatile TypeAdapter<String> string_adapter;
    private volatile TypeAdapter<BoundingBox> boundingBox_adapter;
    private volatile TypeAdapter<List<Geometry>> list__geometry_adapter;
    private final Gson gson;
    public GsonTypeAdapter(Gson gson) {
      this.gson = gson;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void write(JsonWriter jsonWriter, GeometryCollection object) throws IOException {
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
      jsonWriter.name("geometries");
      if (object.geometries() == null) {
        jsonWriter.nullValue();
      } else {
        TypeAdapter<List<Geometry>> list__geometry_adapter = this.list__geometry_adapter;
        if (list__geometry_adapter == null) {
          this.list__geometry_adapter = list__geometry_adapter = (TypeAdapter<List<Geometry>>) gson.getAdapter(TypeToken.getParameterized(List.class, Geometry.class));
        }
        list__geometry_adapter.write(jsonWriter, object.geometries());
      }
      jsonWriter.endObject();
    }
    @Override
    @SuppressWarnings("unchecked")
    public GeometryCollection read(JsonReader jsonReader) throws IOException {
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        return null;
      }
      jsonReader.beginObject();
      String type = null;
      BoundingBox bbox = null;
      List<Geometry> geometries = null;
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
          case "geometries": {
            TypeAdapter<List<Geometry>> list__geometry_adapter = this.list__geometry_adapter;
            if (list__geometry_adapter == null) {
              this.list__geometry_adapter = list__geometry_adapter = (TypeAdapter<List<Geometry>>) gson.getAdapter(TypeToken.getParameterized(List.class, Geometry.class));
            }
            geometries = list__geometry_adapter.read(jsonReader);
            break;
          }
          default: {
            jsonReader.skipValue();
          }
        }
      }
      jsonReader.endObject();
      return new GeometryCollection(type == null ? "GeometryCollection" : type, bbox, geometries);
    }
  }
}
