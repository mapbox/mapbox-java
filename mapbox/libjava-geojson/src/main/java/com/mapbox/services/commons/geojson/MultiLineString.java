package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class MultiLineString implements Serializable {

  private static final String type = "MultiLineString";

  public static MultiLineString fromJson(@NonNull String json) {
    return new Gson().fromJson(json, MultiLineString.class);
  }

  public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings) {
    return new AutoValue_MultiLineString(lineStrings);
  }

  public static MultiLineString fromLngLats(@NonNull List<List<Point>> coordinates) {
    List<LineString> lineStrings = new ArrayList<>();
    for (List<Point> points : coordinates) {
      lineStrings.add(LineString.fromLngLats(points, null));
    }
    return new AutoValue_MultiLineString(lineStrings);
  }

  public abstract List<LineString> lineStrings();

  public List<List<Point>> coordinates() {
    List<List<Point>> coordinates = new ArrayList<>();
    for (LineString lineString : lineStrings()) {
      coordinates.add(lineString.coordinates());
    }
    return coordinates;
  }

  public String type() {
    return type;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }
}
