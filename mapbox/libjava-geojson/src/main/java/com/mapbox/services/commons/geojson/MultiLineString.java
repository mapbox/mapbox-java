package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.services.commons.geojson.custom.BoundingBox;
import com.mapbox.services.commons.geojson.custom.MapboxAdapterFactory;
import com.mapbox.services.commons.geojson.custom.PointDeserializer;
import com.mapbox.services.commons.geojson.custom.PointSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class MultiLineString implements Geometry<List<List<Point>>>, Serializable {

  private static final String TYPE = "MultiLineString";

  public static MultiLineString fromJson(@NonNull String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(MapboxAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, MultiLineString.class);
  }

  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings) {
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(TYPE, null, coordinates);
  }

  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings,
                                                @Nullable BoundingBox bbox) {
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings) {
      coordinates.add(lineString.coordinates());
    }
    return new AutoValue_MultiLineString(TYPE, bbox, coordinates);
  }

  public static MultiLineString fromLngLats(@NonNull List<List<Point>> coordinates) {
    return new AutoValue_MultiLineString(TYPE, null, coordinates);
  }

  public static MultiLineString fromLngLats(@NonNull List<List<Point>> coordinates,
                                            @Nullable BoundingBox bbox) {
    return new AutoValue_MultiLineString(TYPE, bbox, coordinates);
  }

  @NonNull
  @Override
  public abstract String type();

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


  public String toJson() {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Point.class, new PointSerializer());
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
