package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.mapbox.services.commons.models.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@AutoValue
public abstract class Polygon implements Serializable {

  private final String type = "Polygon";

  public static Polygon fromJson(@NonNull String json) {
    return new Gson().fromJson(json, Polygon.class);
  }

  public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable LineString... inner) {
    isLinearRing(outer);
    // If inner rings are set to null, return early.
    if (inner == null) {
      return new AutoValue_Polygon(outer, null);
    }
    for (LineString lineString : inner) {
      isLinearRing(lineString);
    }
    return new AutoValue_Polygon(outer, Arrays.asList(inner));
  }

  public static Polygon fromLngLats(@NonNull List<List<Point>> coordinates) {
    LineString outer = LineString.fromLngLats(coordinates.get(0));
    if (coordinates.size() < 1) {
      // No inner coordinates provided, returning early.
      return new AutoValue_Polygon(outer, null);
    }
    List<LineString> inner = new ArrayList<>();
    // Start at index 1 since the 0 index is our outer linestring.
    for (List<Point> points : coordinates.subList(1, coordinates.size())) {
      inner.add(LineString.fromLngLats(points));
    }
    return new AutoValue_Polygon(outer, inner);
  }

  @NonNull
  public abstract LineString outer();

  @Nullable
  public abstract List<LineString> inner();

  @NonNull
  public List<List<Point>> coordinates() {
    List<List<Point>> coordinates = new ArrayList<>();
    coordinates.add(outer().coordinates());

    if (inner() != null) {
      for (LineString lineString : inner()) {
        coordinates.add(lineString.coordinates());
      }
    }
    return coordinates;
  }

  public String type() {
    return type;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }



  private static boolean isLinearRing(LineString lineString) {
    if (lineString.coordinates().size() < 4) {
      throw new RuntimeException("LinearRings need to be made up of 4 or more coordinates.");
    }
    if (!(lineString.coordinates().get(0).equals(
      lineString.coordinates().get(lineString.coordinates().size() - 1)))) {
      throw new RuntimeException("LinearRings require first and last coordinate to be identical");
    }
    return true;
  }
}