package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A MultiPolygon is a type of {@link Geometry}.
 *
 * @see <a href='http://geojson.org/geojson-spec.html#multipolygon'>Official GeoJSON MultiPolygon Specifications</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MultiPolygon implements Geometry<List<List<List<Point>>>>, Serializable {

  private static final String type = "MultiPolygon";

  public static MultiPolygon fromJson(String json) {
    return new Gson().fromJson(json, MultiPolygon.class);
  }

  public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons) {
    return new AutoValue_MultiPolygon(polygons);
  }

  public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> coordinates) {
    List<Polygon> polygons = new ArrayList<>();
    for (List<List<Point>> points : coordinates) {
      polygons.add(Polygon.fromLngLats(points));
    }
    return new AutoValue_MultiPolygon(polygons);
  }

  public abstract List<Polygon> polygons();

  public List<List<List<Point>>> coordinates() {
    List<List<List<Point>>> coordinates = new ArrayList<>();
    for (Polygon polygon : polygons()) {
      coordinates.add(polygon.coordinates());
    }
    return coordinates;
  }

  @Override
  public String type() {
    return type;
  }

  @Override
  public String toJson() {
    return new Gson().toJson(this);
  }
}
