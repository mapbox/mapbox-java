package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.IntRange;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.services.commons.geojson.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Object representing an intersection along the step.
 *
 * @since 1.3.0
 */
@AutoValue
public abstract class StepIntersection implements Serializable {

  public static Builder builder() {
    return new AutoValue_StepIntersection.Builder();
  }

  /**
   * A {@link Point} object representing the intersection location.
   *
   * @return a [longitude, latitude] {@link Point} describing the location of the turn
   * @since 3.0.0
   */
  public abstract double[] location();

  // TODO ensure the location object becomes a point

  /**
   * An integer array of bearing values available at the step intersection.
   *
   * @return An array of bearing values (for example [0,90,180,270]) that are available at the
   * intersection. The bearings describe all available roads at the intersection.
   * @since 1.3.0
   */
  @SuppressWarnings("mutable")
  @IntRange(from = 0, to = 360)
  public abstract int[] bearings();
  // TODO test integer range

  /**
   * An array of strings signifying the classes of the road exiting the intersection. Possible
   * values:
   * <ul>
   * <li><strong>toll</strong>: the road continues on a toll road</li>
   * <li><strong>ferry</strong>: the road continues on a ferry</li>
   * <li><strong>restricted</strong>: the road continues on with access restrictions</li>
   * <li><strong>motorway</strong>: the road continues on a motorway</li>
   * </ul>
   *
   * @return a {@code String[]} containing the classes of the road exiting the intersection
   * @since 3.0.0
   */
  public abstract List<String> classes();

  /**
   * An array of entry flags, corresponding in a 1:1 relationship to the bearings. A value of true
   * indicates that the respective road could be entered on a valid route. false indicates that the
   * turn onto the respective road would violate a restriction.
   *
   * @return an array of entry flags, corresponding in a 1:1 relationship to the bearings
   * @since 1.3.0
   */
  @SuppressWarnings("mutable")
  public abstract boolean[] entry();

  /**
   * Index into bearings/entry array. Used to calculate the bearing before the turn. Namely, the
   * clockwise angle from true north to the direction of travel before the maneuver/passing the
   * intersection. To get the bearing in the direction of driving, the bearing has to be rotated by
   * a value of 180. The value is not supplied for departure
   * maneuvers.
   *
   * @return index into bearings/entry array
   * @since 1.3.0
   */
  public abstract int in();

  /**
   * Index out of the bearings/entry array. Used to extract the bearing after the turn. Namely, The
   * clockwise angle from true north to the direction of travel after the maneuver/passing the
   * intersection. The value is not supplied for arrive maneuvers.
   *
   * @return index out of the bearings/entry array
   * @since 1.3.0
   */
  public abstract int out();

  /**
   * Array of lane objects that represent the available turn lanes at the intersection. If no lane
   * information is available for an intersection, the lanes property will not be present. Lanes are
   * provided in their order on the street, from left to right.
   *
   * @return array of lane objects that represent the available turn lanes at the intersection
   * @since 2.0.0
   */
  public abstract List<IntersectionLanes> lanes();

  public static TypeAdapter<StepIntersection> typeAdapter(Gson gson) {
    return new AutoValue_StepIntersection.GsonTypeAdapter(gson);
  }

  // TODO ensure the location object becomes a point

  @AutoValue.Builder
  public abstract static class Builder {

    private Point location;
    private List<String> classes = new ArrayList<>();
    private List<IntersectionLanes> lanes = new ArrayList<>();

    public abstract Builder bearings(@IntRange(from = 0, to = 360) int[] bearing);
    // TODO test integer range

    public Builder classes(String[] classes) {
      this.classes.addAll(Arrays.asList(classes));
      return this;
    }

    abstract Builder classes(List<String> classes);

    public abstract Builder entry(boolean[] entry);

    public abstract Builder in(int in);

    public abstract Builder out(int out);

    public Builder lanes(IntersectionLanes[] lanes) {
      this.lanes.addAll(Arrays.asList(lanes));
      return this;
    }

    abstract Builder lanes(List<IntersectionLanes> lanes);

//    public Builder location(double[] location) {
//      this.location = Point.fromCoordinates(location);
//      return this;
//    }

    public abstract Builder location(double[] location);

//    abstract Builder location(Point location);

    public abstract StepIntersection build();
  }
}
