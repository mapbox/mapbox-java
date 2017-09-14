package com.mapbox.geojson;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point {

  public static Point create(String string) {
    return new AutoValue_Point(string);
  }

  public abstract String string();

}
