package com.mapbox.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.gson.BoundingBoxDeserializer;
import com.mapbox.geojson.gson.BoundingBoxSerializer;
import com.mapbox.geojson.gson.GeometryDeserializer;
import com.mapbox.geojson.gson.MapboxAdapterFactory;
import com.mapbox.geojson.gson.PointDeserializer;
import com.mapbox.geojson.gson.PointSerializer;

import java.io.Serializable;
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
@AutoValue
public abstract class GeometryCollection implements GeoJson, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "GeometryCollection";

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a GeometryCollection object from scratch it is better to use one of the other provided
   * static factory methods such as {@link #fromGeometries(List)}.
   *
   * @param json a formatted valid JSON string defining a GeoJson Geometry Collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static GeometryCollection fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(MapboxAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    gson.registerTypeAdapter(BoundingBox.class, new BoundingBoxDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    return gson.create().fromJson(json, GeometryCollection.class);
  }

  /**
   * Create a new instance of this class by giving the collection a list of {@link Geometry}.
   *
   * @param geometries a non-null list of geometry which makes up this collection
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries) {
    return new AutoValue_GeometryCollection(null, geometries);
  }

  /**
   * Create a new instance of this class by giving the collection a list of {@link Geometry}.
   *
   * @param geometries a non-null list of geometry which makes up this collection
   * @param bbox       optionally include a bbox definition as a double array
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 1.0.0
   */
  public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries,
                                                  @Nullable BoundingBox bbox) {
    return new AutoValue_GeometryCollection(bbox, geometries);
  }

  /**
   * This describes the TYPE of GeoJson this object is, thus this will always return
   * {@link GeometryCollection}.
   *
   * @return a String which describes the TYPE of geometry, for this object it will always return
   *   {@code GeometryCollection}
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
   * This provides the list of geometry making up this Geometry Collection. Note that if the
   * Geometry Collection was created through {@link #fromJson(String)} this list could be null.
   * Otherwise, the list can't be null but the size of the list can equal 0.
   *
   * @return a list of {@link Geometry} which make up this Geometry Collection
   * @since 1.0.0
   */
  @NonNull
  public abstract List<Geometry> geometries();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this GeometryCollection
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
  public static TypeAdapter<GeometryCollection> typeAdapter(Gson gson) {
    return new AutoValue_GeometryCollection.GsonTypeAdapter(gson);
  }
}
