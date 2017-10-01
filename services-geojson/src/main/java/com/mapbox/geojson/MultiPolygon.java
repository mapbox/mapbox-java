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
 * A MultiPolygon is a TYPE of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#multipolygon'>Official GeoJson MultiPolygon Specifications</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MultiPolygon implements Geometry<List<List<List<Point>>>>, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "MultiPolygon";

  public static MultiPolygon fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, MultiPolygon.class);
  }

  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons) {
    List<List<List<Point>>> coordinates = new ArrayList<>();
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new AutoValue_MultiPolygon(null, coordinates);
  }

  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons,
                                          @Nullable BoundingBox bbox) {
    List<List<List<Point>>> coordinates = new ArrayList<>();
    for (Polygon polygon : polygons) {
      coordinates.add(polygon.coordinates());
    }
    return new AutoValue_MultiPolygon(bbox, coordinates);
  }

  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> coordinates) {
    return new AutoValue_MultiPolygon(null, coordinates);
  }

  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> coordinates,
                                         @Nullable BoundingBox bbox) {

    return new AutoValue_MultiPolygon(bbox, coordinates);
  }

  public List<Polygon> polygons() {
    List<Polygon> polygons = new ArrayList<>();
    for (List<List<Point>> points : coordinates()) {
      polygons.add(Polygon.fromLngLats(points));
    }
    return polygons;
  }

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

  @NonNull
  @Override
  public abstract List<List<List<Point>>> coordinates();

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
