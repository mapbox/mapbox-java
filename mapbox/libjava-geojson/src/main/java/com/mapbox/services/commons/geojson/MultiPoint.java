package com.mapbox.services.commons.geojson;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionSerializer;
import com.mapbox.services.commons.models.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A MultiPoint represents two or more geographic points that share a relationship and is one of
 * the seven geometries found in the GeoJSON spec.
 * <p>
 * This adheres to the RFC 7946 internet standard when serialized into JSON. When deserialized, this
 * class becomes an immutable object which should be initiated using its static factory methods. The
 * list of points must be equal to or greater than 2.
 * </p><p>
 * A sample GeoJSON MultiPoint's provided below (in it's serialized state).
 * <pre>
 * {
 *   "type": "MultiPoint",
 *   "coordinates": [
 *     [100.0, 0.0],
 *     [101.0, 1.0]
 *   ]
 * }
 * </pre>
 * Look over the {@link com.mapbox.services.commons.geojson.Point} documentation to get more
 * information about formatting your list of point objects correctly.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class MultiPoint implements Serializable {

  private static final String type = "MultiPoint";

  /**
   * Create a new instance of this class by defining a list of {@link Point}s which follow the
   * correct specifications described in the Point documentation. Note that there should not be any
   * duplicate points inside the list.
   * <p>
   * Note that if less than 2 points are passed in, a runtime exception will occur.
   * </p>
   *
   * @param points a list of {@link Point}s which make up the LineString geometry
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 3.0.0
   */
  public static MultiPoint fromLngLats(@NonNull List<Point> points) {
    if (points.size() < 2) {
      throw new RuntimeException("A single point can be defined using the Point geometry.");
    }
    return new AutoValue_MultiPoint(points);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String. If you are
   * creating a MultiPoint object from scratch it is better to use one of the other provided static
   * factory methods such as {@link #fromLngLats(List)}. For a valid MultiPoint to exist, it must
   * have at least 2 coordinate entries.
   *
   * @param json a formatted valid JSON string defining a GeoJSON MultiPoint
   * @return a new instance of this class defined by the values passed inside this static factory
   * method
   * @since 1.0.0
   */
  public static MultiPoint fromJson(String json) {
    return new Gson().fromJson(json, MultiPoint.class);
  }

  public String type() {
    return type;
  }

  /**
   * provides the list of {@link Point}s that make up the MultiPoint geometry.
   *
   * @return a list of points
   * @since 3.0.0
   */
  public abstract List<Point> coordinates();

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJSON
   * string.
   *
   * @return a JSON string which represents this MultiPoint geometry
   * @since 1.0.0
   */
  public String toJson() {
    return new Gson().toJson(this);
  }
}
