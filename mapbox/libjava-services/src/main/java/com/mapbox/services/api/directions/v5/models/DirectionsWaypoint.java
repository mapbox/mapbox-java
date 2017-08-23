package com.mapbox.services.api.directions.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.services.commons.geojson.Point;

import java.io.Serializable;

/**
 * An input coordinate snapped to the roads network.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class DirectionsWaypoint implements Serializable {

  /**
   * Provides the way name which the waypoint's coordinate is snapped to.
   *
   * @return string with the name of the way the coordinate snapped to
   * @since 1.0.0
   */
  public abstract String name();

  /**
   * A {@link Point} representing this waypoint location
   *
   * @return GeoJSON Point representing this waypoint location
   * @since 3.0.0
   */
  public abstract double[] location();

  public static TypeAdapter<DirectionsWaypoint> typeAdapter(Gson gson) {
    return new AutoValue_DirectionsWaypoint.GsonTypeAdapter(gson);
  }

  // TODO ensure the location object becomes a point

  @AutoValue.Builder
  public abstract static class Builder {

    private Point location;

    public abstract Builder name(String name);

//    public Builder location(double[] location) {
//      this.location = Point.fromCoordinates(location);
//      return this;
//    }

    public abstract Builder location(double[] location);

    public abstract DirectionsWaypoint build();

  }
}
