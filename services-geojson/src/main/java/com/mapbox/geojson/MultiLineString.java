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

@AutoValue
public abstract class MultiLineString implements Geometry<List<List<Point>>>, Serializable {

  @Expose
  @SerializedName("type")
  private static final String TYPE = "MultiLineString";

  public static MultiLineString fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, MultiLineString.class);
  }

  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings) {
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(null, coordinates);
  }

  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings,
                                                @Nullable BoundingBox bbox) {
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(bbox, coordinates);
  }

  public static MultiLineString fromLngLats(@NonNull List<List<Point>> coordinates) {
    return new AutoValue_MultiLineString(null, coordinates);
  }

  public static MultiLineString fromLngLats(@NonNull List<List<Point>> coordinates,
                                            @Nullable BoundingBox bbox) {
    return new AutoValue_MultiLineString(bbox, coordinates);
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
  public abstract List<List<Point>> coordinates();

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
    return gson.create().toJson(this);
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<MultiLineString> typeAdapter(Gson gson) {
    return new AutoValue_MultiLineString.GsonTypeAdapter(gson);
  }
}
